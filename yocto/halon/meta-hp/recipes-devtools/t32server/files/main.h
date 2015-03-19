#ifndef MAIN_
#define MAIN_

#define _GNU_SOURCE

#include <stdio.h>
#include <unistd.h>
#include <sys/wait.h>	/* main.c, remote.c */
#include <errno.h>	/* fileio.c, main.c, remote.c */

#include <string.h>	/* fileio.c, main.c, remote.c */
#include <fcntl.h>	/* fileio.c, main.c, thread.c */

#include <sys/socket.h>	/* main.c, included in remote.c */

#include <time.h>

#define RUNNING 0
#define STOPPED 1
#define EXITED  2
#define BREAK   3
#define STEPPED 4

typedef struct {
	int fd;
	unsigned int pid;		/* pid of the process */
	unsigned int gdbid;		/* pid of the corresponding gdbserver */
	unsigned int ptid;		/* parent pid */
	unsigned int status;    	/* ruunig, stopped, stopped on a breakpoint or exited */
	unsigned int pthread_offset; 	/* offset of the pthread library */
	unsigned int dl_debug_state;
	char name[16];			/* process name */
	unsigned int signal;   		/* this is redundant, TODO: replace this with the status */
	unsigned int pc;       		/* current program counter */
	char nptl;			/* flag, nptl thread library linked in process */
} ProcessEntry;

ProcessEntry plist[256];

extern int remote_debug;
extern int commPort;
extern char serial_dcc;
extern int armfamily;


/* thread.c */
int InitializeThreads(int attached, ProcessEntry *p_entry, int current);
int GetThreadAddress(char *buf);

/* main.c */
void SignalStop(int pid, int tid, char sig1, char sig2);
int GdbGetState(int i, int done, char *reply);
void GdbBreak(int n) ;

#endif /*MAIN_*/
