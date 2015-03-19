/*********************************************************************************/
/* 11.06.2008                                                                   */
/* Khaled Jmal - Lauterbach Datentechnik GmbH                                   */
/*                                                                              */
/* t32server  	                                                                */
/* this file contains the main routine and two threads :                        */
/*  - mainloop which polls the host for commands and execute them or forward    */
/*    them to the corresponding gdbserver                                       */
/*  - statusloop which poll the running gdbserver looking if a process          */
/*        stopped.                                                              */
/*********************************************************************************/

#include "main.h"
#include "fileio.h"
#include "utils.h"
#include "remote.h"
#include "breakpoint.h"
#include "arm-dcc.h"


#include <linux/auxvec.h>
#include <getopt.h>
#include <sched.h>
#include <ctype.h>
#include <pthread.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <dirent.h>

#define MAXPROC 256

static pthread_t pmain, pstatus;
static pthread_mutex_t mutex[MAXPROC] = {PTHREAD_MUTEX_INITIALIZER};
static pthread_mutex_t cond_mutex = PTHREAD_MUTEX_INITIALIZER;
static pthread_cond_t r_cond   = PTHREAD_COND_INITIALIZER;

static const char version[5] = "6.0-magma";

#define SYMBOLS 's'
#define BKPT    'b'

static char gdbserver_path[100];

static int event = 0;	/* signals that a process terminated */
extern char serial_dcc;
extern int armfamily;
static unsigned int ArmHwCap = 0;
unsigned int ArmRegs = 336;
/* These are in <asm/elf.h> in current kernels.  */
#define HWCAP_VFP       64
#define HWCAP_IWMMXT    512
#define HWCAP_NEON      4096
#define HWCAP_VFPv3     8192
#define HWCAP_VFPv3D16  16384

static int current;
static int nStarted;
static int nRunning = 0;
static int forbidden[MAXPROC];
static int nForbidden = 0;
static int attached;
static int nError = 0;
static int current_thread = -1;
static int nptl_list[MAXPROC];
static char ret[2] = "00";

static int threadinit[MAXPROC];
static int havesymbols = 0;

int remote_debug = 0;

/* TASK.List variables */
static char P_cmd[16];
static char P_state;
static int P_pid;
static int P_ppid, P_pgrp;

/* open /proc/<pid>/stat and read the name (P_cmd), the pid (P_pid),
 * the state (P_state) the parent id (P_ppid) and the group id (P_pgrp)
 * of the process
 * from procpc-x.x/minimal.c
 */
static int stat2proc(int pid)
{
	char buf[800]; /* about 40 fields, 64-bit decimal is about 20 chars */
	int num;
	int fd;
	char * ptr;
	struct stat sb; /* stat() used to get EUID */

	snprintf(buf, 32, "/proc/%d/stat", pid);
	if ( (fd = open(buf, O_RDONLY, 0) ) == -1 ) return 0;
	num = read(fd, buf, sizeof buf - 1);
	fstat(fd, &sb);
	close(fd);
	if(num<80) return 0;
		buf[num] = '\0';
	ptr = strrchr(buf, ')');      /* split into "PID (cmd" and "<rest>" */
	*ptr = '\0';                  /* replace trailing ')' with NUL */
	/* parse these two strings separately, skipping the leading "(". */
	memset(P_cmd, 0, sizeof P_cmd);          /* clear */
	sscanf(buf, "%d (%15c", &P_pid, P_cmd);  /* comm[16] in kernel */
	num = sscanf(ptr + 2,                    /* skip space after ')' too */
		"%c "
		"%d %d ",
		&P_state,
		&P_ppid, &P_pgrp);

	if(P_pid != pid || P_pid == 0)
		return 0;
	ptr = strchr(P_cmd, ':');
	if (ptr != NULL) *ptr = ' ';
	ptr = strchr(P_cmd, ';');
	if (ptr != NULL) *ptr = ' ';
	ptr = strchr(P_cmd, ',');
	if (ptr != NULL) *ptr = ' ';
		return 1;
}

/*
 * in the case of NPTL, the threads have no entries in /proc.
 * We determine the threads using the "qfThreadInfo" gdb command
 * NOTE: only the threads of the current process are displayed
 * gdb: remote_threads_info() in remote.c
 */
static int GdbThreadsInfo (int fd)
{
	char *bufp, buf[4095];
	int tid, cnt = 0;

	putpkt ("qfThreadInfo", 12, fd);
	getpkt (buf, sizeof(buf), 0, fd);
	bufp = buf;
	if (bufp[0] != '\0')		/* q packet recognized */
	{
		while (*bufp++ == 'm')	/* reply contains one or more TID */
		{
			do
			{
				tid = strtoul (bufp, &bufp, 16);
				if (tid != 0) {
					nptl_list[cnt++] = tid;
				}
			} while (*bufp++ == ',');	/* comma-separated list */
			putpkt ("qsThreadInfo", 12, fd);
			getpkt (buf, sizeof(buf), 0, fd);
			bufp = buf;
		}
		return cnt;	/* done */
	}
	return cnt;
}

//! get the reply of the gdbserver in form of $T....
int GdbGetState(int i, int done, char *reply)
{
	int cnt, tmp, j, rest, newthread = 0, len;
	char *ptr;

	len = STOPPKT-done;

	if (plist[i].pthread_offset == 0 && current_thread == -1)
	{
		cnt = ReceiveBlock(reply, len, 0, plist[i].fd);
		if (cnt <= 0)
			return -1;
		ptr = strstr(reply, "th");
		if (ptr)
		{
			tmp = ReceiveBlock(reply+len, 8, 0, plist[i].fd);
			if (tmp < 0)
				return -1;
			cnt+=tmp;
			newthread = 1;
			goto getrest;
		}
		debugout("[get status], pthread_offset = 0\n");
		goto done;
	}
	cnt = ReceiveBlock(reply, len+8, 0, plist[i].fd);
	if (cnt <= 0)
		return -1;
getrest:
	reply[cnt] = 0;
	ptr = NULL;
	j = 0;
	while (ptr == NULL && j < 2)
	{
		ptr = strchr(reply, '#');
		if (ptr)
		{
			if (ptr[1] == 0)
				rest = 2;
			else if (ptr[2] == 0)
				rest = 1;
			else
				goto done;
			tmp = ReceiveBlock(reply+cnt, rest, 0, plist[i].fd);
			cnt += tmp;
			break;
		}
		tmp = ReceiveBlock(reply+cnt, 3, 0, plist[i].fd);
		cnt+=tmp;
		reply[cnt] = 0;
		j++;
	}
	if (newthread)
		current_thread = getthread(reply);
done:
	debugout("[get status]: (\"%s\") cnt = %i\n", reply, cnt);
	return cnt;
}

//! get the list of active processes by reading the /proc entries
static int GetTaskList(int sendit)
{
	struct dirent *ent;          /* dirent handle */
	DIR *dir, *dir2;
	char process_string[50];
	char buf[100], buf2[50];
	int fd, i, j, l, nthreads = 0;
	char list[8000];
	char *p;

	if (plist[current].fd != -1 && plist[current].status != RUNNING)
	{
		send(plist[current].fd, "+", 1, 0);
		if (plist[current].status == RUNNING || putpkt("?", 1, plist[current].fd)< 0) /* get the current thread */
			goto GetTaskList;
		if (GdbGetState(current, 0, buf2) > 0)
		{
			send(plist[current].fd, "+", 1, 0);
			current_thread = getthread(buf2);
			if (current_thread == 0)
				current_thread = -1;
		}
		if (plist[current].nptl)
			nthreads = GdbThreadsInfo(plist[current].fd);

	}
GetTaskList:
	debugout("***\n* current_thread = %i\n***\n", current_thread);

	dir = opendir("/proc");
	p  = list;

	while(( ent = readdir(dir) ))
	{
		if(*ent->d_name<'0' || *ent->d_name>'9') continue;
		if(!stat2proc(atoi(ent->d_name))) continue;
		if (P_state == 'Z')  /* Zombie Process */
		{
			sprintf(plist[i].name, "[%s]", P_cmd);
			sprintf(P_cmd, "%s", plist[i].name);
		}

		/* get kernel processes (/proc/<pid>/cmdline empty) */
		snprintf(buf, 32, "/proc/%d/cmdline", P_pid);
		fd = open(buf, O_RDONLY, 0);
		if (fd < 0) continue;
		if (!sendit && (P_pid == getpid() || P_pid == 1 || read(fd, buf, sizeof buf - 1) <= 0))
		{
			forbidden[nForbidden] = P_pid;
			nForbidden++;
		}
		close(fd);

		sprintf(process_string, "%s:%d;", P_cmd, P_pid);
		for (i = 0; i< strlen(process_string); i++)
			*p++=process_string[i];

		if (P_pid == 1) continue;

		snprintf(buf, 32, "/proc/%d/task", P_pid);
		dir2 = opendir(buf);
		if (dir != NULL)
		{
			int ppid = P_pid;

			while (( ent = readdir(dir2) )) {
			if(*ent->d_name<'0' || *ent->d_name>'9') continue;
			if(!stat2proc(atoi(ent->d_name))) continue;
			if (ppid == P_pid) continue;
			if (current_thread == P_pid)
				sprintf(process_string, "%s:%d,%d*;", P_cmd, ppid, P_pid);
			else
				sprintf(process_string, "%s:%d,%d;", P_cmd, ppid, P_pid);
			for (i = 0; i< strlen(process_string); i++)
				*p++=process_string[i];
		}
		closedir(dir2);
		}
		else
		{
			for (i = 0; i < nStarted; i++)
			{
				if (P_pid == atoi(plist[i].name))
					sprintf(plist[i].name, "%s", P_cmd);
				if (i == current && plist[i].nptl && P_pid == plist[i].pid)
				{
					for (j = 0; j < nthreads; j++)
					{
						if (nptl_list[j] == P_pid) continue;
						if (current_thread == nptl_list[j])
							sprintf(process_string, "%s:%d,%d*;", P_cmd, plist[i].pid, nptl_list[j]);
						else
							sprintf(process_string, "%s:%d,%d;", P_cmd, plist[i].pid, nptl_list[j]);
						for (l = 0; l < strlen(process_string); l++)
							*p++=process_string[l];
					}
					continue;
				}
				if (P_pid != plist[i].pid && (P_ppid == plist[i].pid || P_ppid == plist[i].ptid)
					&& !strcmp(P_cmd, plist[i].name))
				{
					if (P_ppid == plist[i].pid)
						plist[i].ptid = P_pid;
					if (current_thread == P_pid)
						sprintf(process_string, "%s:%d,%d*;", P_cmd, plist[i].pid, P_pid);
					else
						sprintf(process_string, "%s:%d,%d;", P_cmd, plist[i].pid, P_pid);
				}
			}
		}

	}
	closedir(dir);
	*p++='#';
	*p = '\0';
	if (sendit)
		TransmitBlock(list, strlen(list), 1000);
	return 0;
}

//! break the process and get the status packet from the gdbserver
void GdbBreak(int n)
{
	char buf[100];

	nRunning--;
	plist[n].status = STOPPED;
	pthread_mutex_lock (&mutex[n]);
	if (send(plist[n].fd, "\003", 1, 0) <= 0)
	    printf("break error\n");
	GdbGetState(n, 0, buf);
	pthread_mutex_unlock (&mutex[n]);
	send(plist[n].fd, "+", 1, 0);
	RemoveBreakpoints(n, plist[n].fd);
	if (nRunning < 0) nRunning = 0;
	plist[n].status = STOPPED;
}

/* stop packet for arm dcc begins with the character '!' (0x21)
 * and contain:
 * - the stop signal (1 byte)
 * - pid (3 bytes)
 * - tid (3bytes)
 * pid and tid are separated by the character ',' (0x3b)
 * The stop packet ends with 0
 */
//! only for arm
void SignalStopDCC(int pid, int tid, int signal) /* ARM */
{
 	int wdata1, wdata2, wdata3, status;

	debugout("***\n*** signal stop dcc %x %x %i\n***\n", pid, tid, signal);
	wdata1 = 0x21 | (signal << 8) | (pid & 0xff0000) | 0x02000000;
	wdata2 = (pid & 0xff00) >> 8 | (pid & 0xff) << 8 | (tid & 0xff0000) | 0x02000000;
	wdata3 =  (tid & 0xff00) >> 8 | (tid & 0xff) << 8 | 0x3b0000 | 0x02000000;
	do {
		status = get_dcc_status(armfamily);

	} while (!(status & 0x1) && (status & 0x2));

	if (!(status & 0x2))
	{
		write_dcc_reg(armfamily, wdata1, 0);
		do {
		    status = get_dcc_status(armfamily);

		} while (status & 0x2);
		write_dcc_reg(armfamily, wdata2, 0);
		do {
		    status = get_dcc_status(armfamily);

		} while (status & 0x2);
		write_dcc_reg(armfamily, wdata3, 0);
	}
}
//! only for arm
static int StepOverBreakpointArm(char *buf)	/* ARM */
{
	char tmp[60];
	char *ptr;

	debugout("*** Step over breakpoint ***\n");

	if (buf[8] == ':')
	{
		debugout("**** ****\n");
		sprintf(tmp, "vCont;s:%s", &buf[9]);
		ptr = strchr(tmp, '#');
		if (ptr) *ptr = 0;
		putpkt(tmp, strlen(tmp), plist[current].fd);
	}
	else
	{
// 		if (current_thread > 0)
// 			sprintf(tmp, "vCont;s:%x", current_thread);
// 		else
// 			sprintf(tmp, "vCont;s");
		sprintf(tmp, "s");
		putpkt(tmp, strlen(tmp), plist[current].fd);
	}
	if (GdbGetState(current, 0, tmp) < 0)
	    return -1;
	send(plist[current].fd, "+", 1, 0);
	plist[current].signal = 0;
	return 0;
}
//! only for arm
static int ProcessBreakpointCommandArm(char *buf)	/* ARM */
{
    	char *ptr1, *ptr2;
    	int task = 0, addr = 0, j, res;

    	ptr1 = strchr(buf, ',');
    	if (!ptr1)
	    return -1;
    	*ptr1++ = 0;
    	ptr2 = strchr(ptr1, '%');
    	if (ptr2)
    	    *ptr2 = 0;
    	for (j = 0; j < strlen(ptr1); j++)
	    task |= fromhex(ptr1[j]) << 4*(strlen(ptr1)-j-1);
    	ptr1 = buf + 3;
    	for (j = 0; j < strlen(ptr1); j++)
	    addr |= fromhex(ptr1[j]) << 4*(strlen(ptr1)-j-1);
    	for (j = 0; j < nStarted; j++)
    	{
	    if (plist[j].pid == task)
		break;
    	}
    	if (plist[j].fd < 0 || j == nStarted)
    	{
		return -1;
    	}
    	if (buf[2] == '2' && addr == plist[j].pc)
	    plist[j].signal = 0;
    	res = UpdateBreakpointList(buf[2], addr, j, plist[j].fd);
    	if (res == 0)
    	{
    		if (buf[2] == '1' && addr == plist[j].pc)
	    	    plist[j].signal = SIGTRAP;
		return 0;
    	}
    	return -1;
}
//! only for arm
inline void GetStatusEventArm(void)	/* ARM */
{
	int i;
	char reply[20];

	for (i = 0; i < nStarted; i++)
	{
		if (plist[i].status == EXITED && plist[i].pid != -1)
		{
			debugout("t32server: process %i exited\n", plist[i].pid);
			kill(plist[i].gdbid, 0);
			waitpid(plist[i].gdbid, NULL, 0);
			sprintf(reply, "$W%i:%c%c", plist[i].pid, ret[0], ret[1]);
			TransmitBlock(reply, strlen(reply), 1000);
			nRunning--;
			if (nRunning < 0) nRunning  = 0;
			close(plist[i].fd);
			plist[i].pid = -1;
			plist[i].fd = -1;
		}
	}
}


//! signal the stop to TRACE32
void SignalStop(int pid, int tid, char sig1, char sig2)
{
	int signal;
	signal = (fromhex(sig1) << 8) | fromhex(sig2);
	SignalStopDCC(pid, tid, signal);
	return;
}

//! start a new process or attach to a running one
inline int RunTask(char *buf)
{
	char attach[9] = "--attach";
	char connection[20], name[50], *args[10], tmp[4095], tmp2[4095];
	int  i, j, id, freeargs = 0;
	struct stat sb;
	pid_t pid, pid2;
	char *ptr1, *ptr2;

	current_thread = -1;

	sprintf(connection, "127.0.0.1:%i", nStarted+2345+nError);
	args[0] = gdbserver_path;
	args[1] = connection;
	i = 3;

	while (buf[i] != '%')
	{
	    name[i-3] = buf[i];
	    i++;
	    if (i >= 250) break;
	}
	name[i-3] = '\0';

	if (buf[1] == 'r') // Run
	{
	    i = 3;
	    ptr1 = strchr(name, ' ');
	    if (ptr1 != NULL)
	    {
	        *ptr1 = 0;
	    }
	    else
	    {
		sprintf(tmp, "%s", name);
	        args[2] = tmp;
		goto got_argumets;
	    }
	    freeargs = 1;
	    args[2] = malloc(50);
	    sprintf(args[2], "%s", name);
	    ptr1++;
	    ptr2 = ptr1;
	    while (ptr2 != NULL)
	    {
		ptr2 = strchr(ptr1, ' ');
		if (ptr2 != NULL)
		    *ptr2 = 0;
		args[i] = malloc(50);
		sprintf(args[i], "%s", ptr1);
		ptr1 = ptr2;
		if (ptr1 != NULL)
		    ptr1++;
		i++;
	    }
got_argumets:
	    for (j = 0; j < i; j++)
		    debugout("[RunTask]: args[%i] = %s\n", j, args[j]);
	    if (stat(args[2], &sb)<0)
	    {
		if (strstr(args[2], "/bin/"))
		{
		    TransmitBlock("-", 1, 1000);
		    return -1;
	        }
	        printf("cannot stat %s, trying /bin/%s\n", args[2], args[2]);
	    	sprintf(tmp2, "/bin/%s", args[2]);
	    	if (stat(tmp2, &sb)<0)
	    	{
		    printf("process %s not found\n", tmp2);
		    TransmitBlock("-", 1, 1000);
		    return -1;
	    	}
	    	else
		    sprintf(args[2], "%s", tmp2);
	    }
	    args[i] = NULL;
	}
	else         // attach
	{
	    id = atoi(name);
	    for (i = 0; i < nStarted; i++)
	    {
	        if (plist[i].gdbid == id)
		{
		    TransmitBlock("-", 1, 1000);
		    return -1;
		}
	    }
	    for (j = 0; j < nForbidden; j++)
	    {
	        if (forbidden[j] == atoi(name))
	        {
		    TransmitBlock("-", 1, 1000);
		    return -1;
	        }
	    }
	    //TransmitBlock("+", 1, 1000);
	    attached = 1;
	    args[2] = attach;
	    args[3] = name;
	    args[4] = NULL;
	    i = 0;
	}
	pid = fork();
	if (pid<0)
	    return -1;
	if (pid==0)
	{
	    if (execv(gdbserver_path, args) < 0)
 		exit(1);
	}
	if (freeargs)
	{
		for (j = 2; j < i; j++)
	    	    free(args[j]);
	}
	sleep(1);
	pid2 = waitpid(pid, NULL, WNOHANG);
	if (pid2 == pid || pid2 == -1)
	{
	    printf("!!!error executing process!!!\n");
	    TransmitBlock("-", 1, 1000);
	    return -1;
	}
	plist[nStarted].fd = GdbNetOpen(nStarted+2345+nError);
	if (plist[nStarted].fd < 0)
	{
 	    TransmitBlock("-", 1, 1000);
	    printf("!! t32server error: cannot connect to gdbserver (error %i) !!\n", errno);
	    nError++;
	    return -1;
	}
	if (buf[1] == 'a')
	    TransmitBlock("+", 1, 1000);
	current = nStarted;
	plist[current].gdbid = pid;
	if (buf[1] == 'r')
	{
	     /* get the id of the created process */
	     putpkt("?",1, plist[current].fd);
	     getpkt(tmp2, sizeof(tmp2), 500, plist[current].fd); /* get_status */
	     debugout("[RunTask]: getting pid from %s\n", tmp2);
	     id = getthread(tmp2);
	     if (id == 0) id = pid+1;
	     {
			int data;

		if (((id) << 4) < 0x100)
		    data = (id) << 4;
		else if (((id) << 4) < 0x10000)
		    data = ((id) << 4) | 0x01000000;
		else data = ((id) << 4) | 0x02000000;
		if (send_dcc_fast(data, 0, 0, 1) < 0)
		    printf("TASK.RUN: error sending pid!!\n");
	     }
	     plist[nStarted].pid = id;
	}
	else
	{
	     plist[nStarted].pid = atoi(name);
	}
	/* get the name of the process */
	if (buf[1] == 'r')
	{
	    if (!strchr(args[2], '/'))
	    {
		strcpy(plist[current].name, name);
		return 0;
	    }
	    ptr2 = strrchr(args[2], '/');
	}
	else
	{
	    if (!strchr(args[3], '/'))
	    {
	    	strcpy(plist[current].name, name);
			return 0;
	    }
	    ptr2 = strrchr(args[3], '/');
	}
	if (ptr2)
	{
	    ptr2++;
	    strcpy(plist[current].name, ptr2);
	} else
	    strcpy(plist[current].name, name);

	return 0;
}

//! Set the current task/gdbserver
inline void SetTask(char *buf)
{
	char *ptr;
	char tmp[4095];
	int process_id = 0, thread_id, i;

	thread_id = 0;
	ptr = strchr(buf, ',');
	if (ptr)
	{
	    *ptr = 0;
	    ptr++;
	    ptr[strlen(ptr)] = 0;
	    thread_id = atoi(ptr);
	    debugout("[SetTask]: selected thread = %i\n", thread_id);
	    sprintf(tmp, "Hg%x", thread_id);
	    plist[current].signal = 0;
	}
	i = 3;
	while (buf[i] != '$' && buf[i] <= '9' && buf[i] >= '0')
	{
		process_id = process_id*10 + (buf[i] - '0');
		i++;
	}
	for (i = 0; i < nStarted; i++)
	{
	    if (plist[i].pid == process_id)
	    {
		current = i;
		break;
	    }
	}
	if (thread_id && plist[current].fd != -1 && plist[current].pthread_offset != 0)
	{
	    if (plist[current].status == RUNNING || putpkt(tmp, strlen(tmp), plist[current].fd) < 0)
	 	    return;
	    if (GdbGetError(plist[current].fd))
	        return;
	    sprintf(tmp, "Hc%x", thread_id);
	    putpkt(tmp, strlen(tmp), plist[current].fd);
	    if (GdbGetError(plist[current].fd))
	        return;
	}
}

/*
 * return values:
 * -1: error
 *  1: process running
 *  0: no error, process not running
 *  2: process stopped at breakpoint when initializing the threads
 */
static int ProcessGdbCommand(char *cmdstr, int n)
{
	char reply[4096];
	int recvlen = 0, cnt, tid, res;
	char cmd;

	if (cmdstr[1] == 'k' && plist[current].status == RUNNING)
		GdbBreak(current);

	if (IS_CONT(cmdstr))
	{
		{
			if (plist[current].signal == SIGTRAP) /* step over breakpoint, only for DCC */
			{
				   if (StepOverBreakpointArm(cmdstr) == -1) /* error => running */
				   	return 1;
			}
			/* set all breakpoints of the current task */
			SetBreakpoints(current, plist[current].fd);
		}
		if (plist[current].dl_debug_state != 0) /* we haven't initialized the threads yet, so lets do it */
		{
			res = InitializeThreads(attached, &plist[current], current);
			threadinit[current] = 1;
			plist[current].dl_debug_state = 0;
			if (res == -2) /* process didn't stop on _dl_debug_state, process running */
				return 1;
			if (res == -3)
				return 2;
			if (res == 1)
				plist[current].nptl = 1;
		}
	}
	/* send the command to the gdbserver */
	if (send(plist[current].fd, cmdstr, strchr(cmdstr, '#') + 3 - cmdstr, 0) <= 0)
	{
		debugout("send error, errno = %i, current fd = %i\n", errno, plist[current].fd);
		return -1;
	}
	/* command successfully sent to the gdbserver, get the reply now */
	if(!IS_CONT(cmdstr))
	{
		if (cmdstr[1] == 'k') /* we sent a kill command to the gdbserver, be sure the gdbserver is dead! */
		{
			kill(plist[current].gdbid, 0);
			waitpid(plist[current].gdbid, NULL, 0);
			plist[current].status = EXITED;
			plist[current].fd = -1;
			return 0;
		}
		/* compute the expected length of the gdbserver's reply */
		recvlen = compute_len(cmdstr, n, plist[current].pthread_offset);
		/* get the reply now */
		if (IS_STEP(cmdstr))
		    cmd = 's';
		else
		    cmd = cmdstr[1];
		cnt = GdbGetReply(cmd, reply, current, recvlen, plist[current].fd);
		if (cnt > 0)
		{
			if (IS_STEP(cmdstr))
			{
				if (cnt == 1)
				    return 1;
				tid = getthread(reply);
				if (!tid)
				    tid = plist[current].pid;
				else
				    current_thread = tid;
				SignalStop(plist[current].pid, tid, reply[2], reply[3]);
				return 0;
			}
			/* !STEP */
			TransmitBlock(reply, strlen(reply), 1000);
			if (cmdstr[1] == 'g')
			{
				plist[current].pc = getPC(reply, 'g');
				if (!plist[current].signal)
				    plist[current].signal = GetBreakSignal(current, plist[current].pc);
			}
			reply[0] = '\0';
		}
		else if (IS_STEP(cmdstr))
		    return 1;
		return 0;
	}
	else
	    return 1;	/* running */
}

/****************************************************************/
/* Process T32 special commands. The T32 commands begin with &, */
/* the second character defines the ccommand:			*/
/* t: get the task list	(TASK.List)				*/
/* p: set the current task, current thread (TASK.SELect)	*/
/* r: start a new process (TASK.RUN)				*/
/* a: attach to a running process (TASK.SELect)			*/
/* f: transfer a file (TASK.COPYUP, TASK.COPYDOWN)		*/
/* c: get the value of _dl_debug_state				*/
/* d: set/reset remote debugging		 		*/
/* s: get the addresses of the pthread labels (only for ARM)	*/
/* - only for ARM DCC:						*/
/* b: set/remove a breakpoint from the breakpoint list 		*/
/* - Only for ETH:						*/
/* k: only for Front End, (SYStem. Mode Down)			*/
/* - only for !ARM						*/
/* y: get the addresses of the pthread labels			*/
/* -only for POWERPC					        */
/* b: Break.MONitor						*/
/* -only for MIPS				 		*/
/* s: Step 							*/
/****************************************************************/
static int ProcessT32Command(char *cmdstr)
{
	int i;
	char *ptr;
	switch(cmdstr[1])
	{
	    case 't': // get task list "&t%"
		GetTaskList(1);
		cmdstr[0] = '\0';
		break;
	    case 'r': case 'a': // Run, Attach   "&r:<name>%", &a:<pid>%"
		if (RunTask(cmdstr) == 0)
		    nStarted++;
		cmdstr[0] = '\0';
		break;
	    case 'p': // change current process "&p:<pid>%" "&p:<pid>,<tid>%"
		SetTask(cmdstr);
		break;
	    case 'k': // system down &k%
		for (i = 0; i < nStarted; i++)
		{
	    		if (plist[i].pid > 0)
	    		{
				if (plist[i].status == RUNNING)
		        	send(plist[i].fd, "\003", 1, 0);
		    		send(plist[i].fd, "+", 1, 0);
		   		send(plist[i].fd, "$k#6b", 5, 0);
		    		close(plist[i].fd);
	    		}
		}
  		exit(0);
	    case 'c': // get map info "&c<dl_debug_state>%"
		if (attached)
		{
			plist[current].dl_debug_state = 0;
			if (InitializeThreads(attached, &plist[current], current) == 1)
				plist[current].nptl = 1;
			return 0;
		}
		ptr = strchr(cmdstr, '%');
	    	if (ptr == NULL)
		    return 0;
		*ptr = 0;
		plist[current].dl_debug_state = 0;
		for(i = 0; i < 8; i+=2)
		{
		    plist[current].dl_debug_state |= fromhex(cmdstr[2+i]) << 4*(7-i) | fromhex(cmdstr[2+i+1]) << (4*(6-i)) ;
    		}
		debugout("***\n* dl_debug_state: %x\n***\n", plist[current].dl_debug_state);
		return 1; //goto start;
	    case SYMBOLS: /* "&s/y-%" or "&s/y;label1;label2; ... ;labeln%" */
		if (cmdstr[2] == '-')
		{
		    debugout("debugging of multi-threaded applications not supported (no symbols found)\n");
		}
		else
		{
		    GetThreadAddress(cmdstr);
		    havesymbols = 1;
		}
		return 1;
	    case BKPT: /* set or delete a breakpoint "&b1<address>,<task>%" "b2<address>,<task>%" */
		if (ProcessBreakpointCommandArm(cmdstr))
		    TransmitBlock("/000", 1, 10);
		else
		    TransmitBlock("/001", 1, 10);
		return 0;
	    case 'g': /* from gdbserver version 7.0, the length of the register packet is depended of the platform. */
	    {
			int data;
			if (ArmRegs < 0x100)
				data = ArmRegs;
			else if (ArmRegs < 0x10000)
				data = ArmRegs | 0x01000000;
			else data = ArmRegs | 0x02000000;
				debugout("Arm Regs: %i, data = 0x%x\n", ArmRegs, data);
			if (send_dcc_fast(data, 0, 0, 1) < 0)
				debugout("error sending Arm Regs!!\n");
	    	break;
	    }
	    case 'f': /* file transfer */
 		if (cmdstr[2] == 't')  /* get file from host */
 		    GetFile(&cmdstr[4]);
 		else if(cmdstr[2] == 'r')
		    SendFile(&cmdstr[4]);
		 return 0;
	    case 'd':
		remote_debug = !remote_debug;
	}
	return 0;
}

void *mainloop(void *dummy)
{
	int n, res;
	char buf[4096], reply[4096];
	char *newpkt;

	newpkt = NULL;
	while (1)
	{
start:
		reply[0] = '\0';
		if (event > 0) /* process stopped, remove all breakpoints */
		{
			RemoveBreakpoints(event - 1, plist[event - 1].fd);
			event = 0;
		}
		n = GetT32Cmd(buf, &newpkt, current);
		if (n == -1)
			goto check;
		if (buf[0] == '$')  // forward a gdb command
		{
			res = ProcessGdbCommand(buf, n);
			switch (res)
			{
				case 1:	/* process running */
					plist[current].status = RUNNING;
					nRunning++;
					debugout("signal condition\n");
					pthread_cond_signal(&r_cond);
					break;
				case 2:
					plist[current].signal = SIGTRAP; /* ?? */
					break;
				case -1: 	/* Error */
					goto start;
 			}
		}
		else if (buf[0] == '&') /* t32 commands */
		{
			res = ProcessT32Command(buf);
			if (res == 1) goto start;
		}
check:
		if (event == -1) /* process terminates */
		{
			GetStatusEventArm();
			event = 0;
		}
	}
	return 0;

}

//! check if a running process has stopped
inline int CheckProcessStatus(int i)
{
	int pid, tid;
	char tmp[20];
	char buf[100];

	if (ReceiveBlock(buf, 5, 10, plist[i].fd) > 1)
	{
 		debugout("[t32server received status string %s]\n", buf);
		send(plist[i].fd, "+", 1, 0);
		if (buf[0] != '$' && buf[1] != '$')
			return -1;
		if ((!strncmp(buf, "+$T", 3)) || (!strncmp(buf, "$T", 2)))
		{
			if (strstr(buf, "$T020") )
				return 0;
			GdbGetState(i, 5, buf+5);
			send(plist[i].fd, "+", 1, 0);

			if (strstr(buf, "$T4d"))
				return -1;

			pid = plist[i].pid;
			tid = getthread(buf);

			if (strstr(buf, "$T14") /*&& plist[i].pthread_offset != 0*/)
			{
				plist[i].status = STOPPED;
				if (tid == 0)
					sprintf(tmp, "vCont;C14");
				else
					sprintf(tmp, "vCont;C14:%x;c", tid);
				putpkt(tmp, strlen(tmp), plist[i].fd);
				plist[i].status = RUNNING;
				debugout("sending %s\n", tmp);
				return 0;
			}
			nRunning--;
			if (nRunning < 0) nRunning  = 0;

			if (buf[0] == '+')
			{
				buf[2] = buf[3];
				buf[3] = buf[4];
			}
			if (buf[2] == '0' && buf[3] == '5')
				plist[i].signal = SIGTRAP;
			if (!tid) tid = pid;
			SignalStop(pid, tid, buf[2], buf[3]);
			event = i + 1;
			plist[i].status = BREAK;
			return 0;
		}
		if (buf[0] == '+')
		{
			buf[0] = buf[1];
			buf[1] = buf[2];
			buf[2] = buf[3];
			buf[3] = buf[4];
		}
		if(buf[0] == '$' && (buf[1] == 'W' || buf[1] == 'X'))
		{
			current_thread = -1;
			read_frame(tmp, sizeof(tmp), plist[i].fd);
			event = -1;
			plist[i].fd = -1;
			plist[i].status = EXITED;
			ret[0] = buf[2];
			ret[1] = buf[3];
		}
	}
	return 0;
}

//! poll the gdbserver for running processes
void *stateloop(void *dummy)
{
	int i;

wait:
	debugout("stateloop: waiting....\n");
	pthread_cond_wait(&r_cond, &cond_mutex);
	debugout("- there is at least one running process -\n");
	while (1)
	{
		for (i = 0; i < nStarted; i++)
		{
			if (plist[i].status == RUNNING)
			{
				pthread_mutex_lock (&mutex[i]);
				CheckProcessStatus(i);
				pthread_mutex_unlock (&mutex[i]);
			}
		}
		if (nRunning == 0) /* no process is running, so wait */
			goto wait;
	}
	return 0; /* will never be reached */
}

#ifdef __ARM_EABI__
  static const char archstr[] = "ARM EABI over DCC or ethernet";
#else
  static const char archstr[] = "ARM over DCC or ethernet";
#endif


void t32server_version ()
{
	printf("t32server version %s\n", version);
	printf("Lauterbach Datentechnik GmbH 2009\n");
	printf("t32server was configured for %s\n", archstr);
}

void t32server_usage()
{
	printf("\tusage: t32server [OPTIONS]\n"
		"\tOPTIONS:\n"
		"\t\t--version: display the version of the t32server.\n"
		"\t\t--help: display the t32server usage.\n"
		"\t\t--gdbserver-path=<path>: location of your gdbserver\n"
		"\t\t--serial-dcc: use run mode and terminal over dcc.\n"
		"\n");
}

//! main routine
int main(int argc, char *argv[])
{
	int i, n;
	char **next_arg = &argv[1];
	struct stat sb;

	cpu_set_t cur_mask, new_mask;

	CPU_ZERO(&new_mask);
	CPU_SET(0, &new_mask);

	if (sched_setaffinity(0, sizeof (new_mask), &new_mask)) {
		printf("failed to set affinity.\n");
		goto done;
	}

	if (sched_getaffinity(0, sizeof (cur_mask), &cur_mask) < 0) {
		printf( "failed to get affinity.\n");
	}
done:

	sprintf(gdbserver_path, "/usr/bin/gdbserver");
	while (*next_arg != NULL)
	{
		if (strcmp (*next_arg, "--version") == 0)
		{
			t32server_version ();
			exit (0);
		}
		else if (strcmp (*next_arg, "--help") == 0)
		{
			t32server_usage();
			exit (0);
		}
		else if (strncmp (*next_arg, "--gdbserver-path=", 17) == 0)
		{
			char *ptr;

			sprintf(gdbserver_path, "%s/gdbserver", *next_arg+17);
			ptr = strchr(gdbserver_path, ' ');
			if (ptr) *ptr = 0;
		}

		else if (strcmp (*next_arg, "--diag-6004") == 0)
			remote_debug = 1;
		else if (strcmp (*next_arg, "--serial-dcc") == 0)
		{
			serial_dcc = 1;
		}
		else if (strchr(*next_arg, ':'))
		{
			t32server_usage();
	  		exit (0);
		}
		else
		{
			fprintf (stderr, "Unknown argument: %s\n", *next_arg);
			exit (1);
		}
		next_arg++;
		continue;
	}
	/* check if localhost is configured */
	n = GdbNetOpen(2344);
	if (n == -3)
	{
		printf("please make sure that the address 127.0.0.1 is configured\n");
		printf("t32server exiting\n");
		return -1;
	}
	else if (n > 0)
		close(n);

	/* check if there is a gdbserver under /bin */
	if (stat(gdbserver_path, &sb) < 0)
	{
		printf("cannot find %s, t32server exiting\n", gdbserver_path);
		return -1;
	}

	/** everything OK so let's begin **/

	/* print the t32server version */
	printf("t32server version %s\n", version);
	if (serial_dcc)
	{
		if (init_dcc_console())
			serial_dcc = 0;
	}
	event = 0;
	/* initialize process list */
	for (i = 0; i < MAXPROC; i++)
	{
		plist[i].fd = -1;
		plist[i].pid = -1;
		plist[i].status = -1;
		plist[i].pthread_offset = 0;
		plist[i].dl_debug_state = 0;
		plist[i].ptid = -1;
		plist[i].signal = 0;
		plist[i].pc = 0;
		forbidden[i] = -1;
		plist[i].nptl = 0;
		threadinit[i] = 0;
	}
	nStarted = 0;

	debugout("checking arm version\n");
	get_arm_version(); /* this functions stays in a loop until the arm version is defined (sent after Go.MONitor) */

	GetTaskList(0);

	{
		char tmpbuf[500];
		unsigned char data[10];
		int fd;
		sprintf(tmpbuf, "/proc/%d/auxv", getpid());
		fd = open (tmpbuf, O_RDONLY);
		if (fd >=0)
		{
			while (read (fd, data, 8) == 8)
			{
				unsigned int *data_p = (unsigned int *)data;
				if (data_p[0] == AT_HWCAP)
				{
					ArmHwCap = data_p[1];
					break;
				}
			}
			close (fd);
			if (ArmHwCap & HWCAP_IWMMXT)
				ArmRegs = 23*8 + 16*16 ;
			else if (ArmHwCap & HWCAP_VFP)
			{
				if (ArmHwCap & HWCAP_NEON)
				ArmRegs = 18*8 + 32*16;
				else if ((ArmHwCap & (HWCAP_VFPv3 | HWCAP_VFPv3D16)) == HWCAP_VFPv3)
					ArmRegs = 18*8 + 32*16;
				else
					ArmRegs = 18*8 + 16*16;
			}
			else
				ArmRegs = 336;
		}
	}
	/* create both threads */
	pthread_create(&pmain, NULL, mainloop, NULL);
	pthread_create(&pstatus, NULL, stateloop, NULL);

	pthread_join(pmain, NULL);
	pthread_join(pstatus, NULL);

	return 0;
}
/* end of main.c */
