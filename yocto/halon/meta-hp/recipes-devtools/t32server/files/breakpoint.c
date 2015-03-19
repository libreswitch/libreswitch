/*********************************************************************************/
/* 12.06.2008                                                                    */
/* Khaled Jmal - Lauterbach Datentechnik GmbH                                    */
/*                                                                               */
/* This file is currently only used for ARM DCC. It contains breakpoint handling */
/* routines.                                                                     */
/* Because of the slow speed of the DCC port, the breakpoint list is stores on   */
/* target. This is actually not necessary for Ethernet                           */
/*********************************************************************************/

#include <stdio.h>
#include <string.h>
#include "breakpoint.h"
#include "utils.h"
#include "remote.h"

CArmMonitorBreakpoints m_vMonitorBreakpoint[256];
static int m_nMonitorBreakpointNumber = 0;

/* check if the breakpoint can be set */
static int CheckBreakpoint(int addr, int fd, int *original)
{
	char buf[4095];
	int j, error;

	debugout("**** check breakpoint at 0x%x ***\n", addr);

	*original = 0;
	sprintf(buf, "m%x,4", addr);
	if (putpkt(buf, strlen(buf), fd) < 0)
		return -1;
	if (getpkt (buf, sizeof(buf), 500, fd) < 0)
		return -1;
	/* check_reply(buf)?? */
	if (strchr(buf, 'E'))
	{
		printf("*** cannot write breakpoint at 0x%x ***\n", addr);
		return -1;
	}
	for (j = 0; j < strlen(buf); j++)
	*original |= fromhex(buf[j]) << 4*(strlen(buf)-j-1);

	error = GdbWriteMemory(fd, addr, breakpoint_instr, opcode_len);
	if (error) return error;
	error = GdbWriteMemory(fd, addr, *original, opcode_len);
	return error;
}

/* remove all breakpoints when process is stopped */
int RemoveBreakpoints(int process, int fd)
{
	int i;

	debugout("** RemoveBreakpoints %i\n", m_nMonitorBreakpointNumber);
	for (i = 0; i < m_nMonitorBreakpointNumber; i++)
	{
		if (m_vMonitorBreakpoint[i].desc == process)
		{
			debugout("** removing breakpoint at address 0x%x for process %i, fd = %i\n", m_vMonitorBreakpoint[i].address, process, fd);
			GdbWriteMemory(fd, m_vMonitorBreakpoint[i].address, m_vMonitorBreakpoint[i].original, opcode_len);
		}
	}
	return 0;
}

/* set all breakpoint for a process befor a "go" */
int SetBreakpoints(int process, int fd)
{
	int i;

	debugout("**** set all breakpoints (n = %i) ***\n", m_nMonitorBreakpointNumber);
	for (i = 0; i < m_nMonitorBreakpointNumber; i++)
	{
		if (m_vMonitorBreakpoint[i].desc == process)
		{
			debugout("* Nr. %i at 0x%x \n", i, m_vMonitorBreakpoint[i].address);
			GdbWriteMemory(fd, m_vMonitorBreakpoint[i].address, breakpoint_instr, opcode_len);
		}
	}
	return 0;
}

int UpdateBreakpointList(char type, int addr, int desc, int fd)
{
	int i, l, original;

	if (type == REMOVE)
	{
		for (i = 0; i < m_nMonitorBreakpointNumber; i++)
		{
			if (m_vMonitorBreakpoint[i].address == addr && m_vMonitorBreakpoint[i].desc == desc)
			{
				for (l = i; l < m_nMonitorBreakpointNumber-1; l++)
				{
					m_vMonitorBreakpoint[l] = m_vMonitorBreakpoint[l+1];
				}
				m_nMonitorBreakpointNumber--;
			}
		}
		return 0;
	}
	else if (type == CHECK)  //check breakpoint and add it to list.
	{
		for (i = 0; i < m_nMonitorBreakpointNumber; i++)
		{
			if (m_vMonitorBreakpoint[i].desc == desc && m_vMonitorBreakpoint[i].address == addr)
			{
				debugout("breakpoint already in list\n");
				return 0;
			}
		}
		if (CheckBreakpoint(addr, fd, &original) < 0)
			return -1;

		m_vMonitorBreakpoint[m_nMonitorBreakpointNumber].address  = addr;
		m_vMonitorBreakpoint[m_nMonitorBreakpointNumber].original = original;
		m_vMonitorBreakpoint[m_nMonitorBreakpointNumber].desc     = desc;
		m_nMonitorBreakpointNumber++;
		return 0;
	}
	return -1;
}

int GetBreakSignal(int current, int pc)
{
	int i;
	for (i = 0; i < m_nMonitorBreakpointNumber; i++)
	{
		if (m_vMonitorBreakpoint[i].desc == current && m_vMonitorBreakpoint[i].address == pc)
			return 5;
	}
	return 0;
}
