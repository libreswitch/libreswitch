#include "main.h"
#include "utils.h"
#include "remote.h"
#include "breakpoint.h"
#include "arm-dcc.h"

#define PTHREADSYMNR  64

/* List of pthread labels requested by the gdbserver for debugging of multi-threaded applications*/
static const char *symbol_list_arr[] =
{
  "__pthread_threads_events",
  "__pthread_last_event",
  "__pthread_handles_num",
  "__pthread_handles",
  "pthread_keys",
  "__linuxthreads_pthread_threads_max",
  "__linuxthreads_pthread_keys_max",
  "__linuxthreads_pthread_sizeof_descr",
  "__linuxthreads_create_event",
  "__linuxthreads_death_event",
  "__linuxthreads_reap_event",
  "__linuxthreads_initial_report_events",
  "__linuxthreads_version",
  "nptl_version",		/* from here nptl symbols */
  "__nptl_threads_events",
  "_thread_db_sizeof_td_thr_events_t",
  "_thread_db_td_thr_events_t_event_bits",
  "__nptl_create_event",
  "__stack_user",
  "_thread_db_list_t_next",
  "_thread_db_const_thread_area",
  "_thread_db_register32",
  "_thread_db_register32_thread_area",
  "_thread_db_sizeof_pthread",
  "_thread_db_pthread_specific",
  "_thread_db_pthread_schedpolicy",
  "_thread_db_pthread_schedparam_sched_priority",
  "_thread_db_pthread_tid",
  "_thread_db_pthread_cancelhandling",
  "_thread_db_pthread_report_events",
  "_thread_db_pthread_start_routine",
  "_thread_db_pthread_eventbuf_eventmask_event_bits",
  "stack_used",
  "_thread_db_pthread_list",
  "_thread_db_pthread_eventbuf",
  "_thread_db_pthread_eventbuf_eventmask",
  "_thread_db_pthread_nextevent",
  "_thread_db_sizeof_list_t",
  "_thread_db_list_t_prev",
  "_thread_db_sizeof_td_eventbuf_t",
  "_thread_db_td_eventbuf_t_eventnum",
  "_thread_db_td_eventbuf_t_eventdata",
  "__nptl_death_event",
  "__nptl_nthreads",
  "_thread_db___nptl_nthreads",
  "__nptl_last_event",
  "_thread_db___nptl_last_event",
  "__pthread_keys",
  "_thread_db___pthread_keys",
  "_thread_db_sizeof_pthread_key_struct",
  "_thread_db_pthread_key_struct_seq",
  "_thread_db_pthread_key_struct_destr",
  "_thread_db_sizeof_pthread_key_data",
  "_thread_db_pthread_key_data_seq",
  "_thread_db_pthread_key_data_data",
  "_thread_db_sizeof_pthread_key_data_level2",
  "_thread_db_pthread_key_data_level2_data",
  "_thread_db_link_map_l_tls_modid",
  "_thread_db_dtv_dtv",
  "_thread_db_pthread_dtvp",
  "_thread_db_const_thread_area",
  "_thread_db_register64",
  "_thread_db_register32",
  "_thread_db_register64_thread_area",
  NULL
};

static int symbol_addr[PTHREADSYMNR+2];

//! get the requested symbol name from the qSymbol packet
void GetSymbolName(char* name, char *sym)
{
	int i = 0;

	while (i < strlen(name))
	{
		sym[i/2] = fromhex(name[i]) << 4 | fromhex(name[i+1]);
		i+=2;
	}
	sym[i/2] = '\0';
}

static int GdbCheckSymbols (int fd, int offset)
{
	char buf[4095], reply[4095], symbol[80];
	char *name, *ptr;
	int i, nptl = 0;
	int begin;
	time_t startingTime;

	begin = 0;

	debugout("GdbCheckSymbols, offset = %x\n", offset);

	putpkt ("qSymbol::", 9, fd);
	getpkt (buf, sizeof(buf), 500, fd);
	debugout("qSymbol, got %s\n", buf);

	while (strncmp (buf, "qSymbol:", 8) == 0)
	{
		ptr = strchr(buf, '#');
		if (ptr) *ptr = 0;
			name = &buf[8];
		GetSymbolName(name, symbol);
		debugout("sym: %s\n", symbol);
		for (i = 0; i < PTHREADSYMNR; i++)
		{
			if (!strcmp(symbol, symbol_list_arr[i]))
			{
				if (symbol_addr[i+begin] == 0)
					sprintf(reply, "qSymbol::%s", name);
				else
				{
					if (!nptl && !strcmp(symbol, "nptl_version"))
						nptl = 1;
					sprintf(reply, "qSymbol:%x:%s", offset + symbol_addr[i+begin] ,name);
				}
				goto transmit;
			}
		}
		debugout("symbol %s not in list\n", symbol);
		sprintf(reply, "qSymbol::%s", name);
transmit:
		putpkt (reply, strlen(reply), fd);
		buf[0] = 0;
		startingTime = time(NULL);
		while (1)
		{
			if (getpkt (buf, sizeof(buf), 1000, fd) > 0)
				break;
			if (((time(NULL)-startingTime)*2000) >= 1)
			{
				debugout("TIMEOUT in GdbCheckSymbols!!\n");
				return -1;
			}
		}
	}
	if (!strcmp(buf, "OK") && nptl)
		return 2;	/* NPTL */
	return 0;
}


//! get the addresses of the pthread labels and store them in symbol_addr
int GetThreadAddress(char *buf)
{
	char *ptr1, *ptr2;
	int i, j, n;

	for (i = 0; i < PTHREADSYMNR; i++)
	symbol_addr[i] = 0;

	i = 0;

	ptr1 = buf + 2;

	while ((ptr2 = strchr(ptr1, ';')))
	{
		ptr2++;
		ptr1 = strchr(ptr2, ';');
		if (ptr1 == NULL)
			ptr1 = strchr(ptr2, '%');
		n = ptr1 - ptr2;
		for (j = 0; j < n; j++)
			symbol_addr[i] |= fromhex(ptr2[j]) << 4*(n-j-1);
		ptr1 = ptr2;
		i++;
	}
	return 0;
}


//! read /proc/<pid>/maps and get the relocation info of libpthread
static int GetMapping(int pid, unsigned int *pthread_offset)
{
	char buf[4095], *ptr1, *ptr2;
	int  fd, i;

	snprintf(buf, 32, "/proc/%d/maps", pid);
	if ( (fd = open(buf, O_RDONLY, 0) ) == -1 )
	{
		printf("cannot open %s\n", buf);
		return -1;
	}
	if (read(fd, buf, sizeof buf - 1) <= 0)
	{
		debugout("cannot read maps\n");
		return -1;
	}
	close(fd);
	debugout("maps:\n%s\n", buf);
	if (!strstr(buf, "libpthread"))
	{
		debugout("libpthread not found in maps\n");
		return -1;  /* process has no threads */
	}
	ptr1 = buf;
	while (1)
	{
		ptr2 = strchr(ptr1, '\n');
		if (ptr2 == NULL)
			return -1;
		ptr2++;
		ptr1 = strchr(ptr2, '\n');
		if (ptr1  > strstr(ptr2, "libpthread"))
		{
			*ptr1 = 0;
			if (strchr(ptr2, 'x'))
				break;
		}
		ptr1 = ptr2;
	}
	ptr1 = strchr(ptr2, '-');
	*ptr1 = 0;
	*pthread_offset = 0;
	for (i = 0; i < 8; i+=2)
		*pthread_offset |= fromhex(ptr2[i]) << 4*(7-i) | fromhex(ptr2[i + 1]) << 4*(6-i);
	debugout("***\n* pthread offset = %x\n***\n", *pthread_offset);
	return 0;
}

/* return values		*/
/* -1: error			*/
/* -2: timeout, process running	*/
/*  0: OK, linuxthreads		*/
/*  1: OK, NPTL			*/
/* -3: stopped at breakpoint    */
int InitializeThreads(int attach, ProcessEntry *p_entry, int current)
{
	char tmp[100], tmp2[20];
	char *ptr;
	time_t t0;
	int i, tid, pc, cnt = 0, nptl = 0, orig = 0, stopped = 0;

	debugout("***********************\n");
	debugout("* initializing th. db *\n");
	debugout("***********************\n");

	if (!attach)
	{
retry:
		sprintf(tmp, "m%x,%x", p_entry->dl_debug_state, opcode_len);
		putpkt(tmp, strlen(tmp), p_entry->fd);
		ReceiveBlock(tmp2, 12, 0, p_entry->fd);
		tmp2[13] = 0;
		for (i = 0; i < opcode_len*2; i+=2)
			orig |= fromhex(tmp2[i+1]) << 4*((opcode_len*2-1)-i) | fromhex(tmp2[i + 2]) << 4*((opcode_len*2-2)-i);
		send(p_entry->fd, "+", 1, 0);

		if (GdbWriteMemory(p_entry->fd, p_entry->dl_debug_state, breakpoint_instr, opcode_len) < 0)
			return -1;
		putpkt("vCont;c", 7, p_entry->fd);
		t0 = time(0);
		while (1)
		{
			if (getpkt(tmp, sizeof(tmp), 500, p_entry->fd) > 1)
			{
				debugout("received status %s\n", tmp);
				break;
			}
			if ((long) (difftime (time (0), t0) > 1))
			{
				debugout("timeout\n");
				return -2;
			}
		}
		debugout("Init.Threads, status: %s\n", tmp);
		pc = getPC(tmp, 'T');
		send(p_entry->fd, "+", 1, 0);
		// we are now at _dl_debug_state?
		ptr = strchr(tmp2, '#');
		if (ptr)
			*ptr = 0;
		if (GdbWriteMemory(p_entry->fd, p_entry->dl_debug_state, orig, opcode_len) < 0)
			return -1;
		if (pc != p_entry->dl_debug_state)
		{
			debugout("Init.Threads: process stopped at %x\n", pc);
			tid = getthread(tmp);
			if (!tid) tid = p_entry->pid;
				SignalStop(p_entry->pid, tid, '0', '5');
			RemoveBreakpoints(current, p_entry->fd);
			stopped = 1;
		}
	}
	else
	{
		cnt = 1;
		attach = 0;
	}

	if (GetMapping(p_entry->pid, &(p_entry->pthread_offset)) == 0)
	{
		debugout("GetMapping: libpthread found\n");
		if (GdbCheckSymbols(p_entry->fd, p_entry->pthread_offset) == 2)
			nptl = 1;
		send_dcc("+", 1, 1000, 0);
		return nptl;
	}
	if (stopped)
		return -3;
	if (cnt == 0)
	{
		putpkt("vCont;s", 7, p_entry->fd);
		getpkt(tmp, sizeof(tmp), 500, p_entry->fd);
		cnt = 1;
		goto retry;
	}
	printf("t32server: debugging of multi-threaded applications not supported\n");
	send_dcc("-", 1, 1000, 0);
	return -1;
}

