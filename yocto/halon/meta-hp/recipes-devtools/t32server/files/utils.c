/*********************************************************************************/
/* 12.06.2008                                                                    */
/* Khaled Jmal - Lauterbach Datentechnik GmbH                                    */
/*                                                                               */
/* This file contains helper functions                                           */
/*********************************************************************************/

#include "utils.h"
#include <stdio.h>
#include <string.h>
#include <stdarg.h>

extern int remote_debug;

/*
 * REGISTERS: length of the g-reply packet.
 * REGISTERS = 1(+) + 1($) + (REG BYTES)*2 + 1(#) + 2(chksum)
 * the REG BYTES are defined in gdb/regformats
 */
#define EXPEDITE_PC 29
#define MAXIMUM 4095

void debugout(const char *format, ...)
{
	va_list arg;
	char emsg[4095];

	if (!remote_debug)
		return;

	va_start (arg, format);
	vsprintf(emsg, format, arg);
	va_end (arg);

	printf("%s", emsg);
}

int fromhex (int a)
{
	if (a >= '0' && a <= '9')
		return a - '0';
	else if (a >= 'a' && a <= 'f')
		return a - 'a' + 10;
	else if (a >= 'A' && a <= 'F')
		return a - 'A' + 10;
	return -1;
}

/* gdb function */
int tohex (int nib)
{
	if (nib < 10)
		return '0' + nib;
	else
		return 'a' + nib - 10;
}

/* get the PC from the register packet */
int getPC(char *buf, char pkt)
{
	int i, pc_value = 0;
	char *p;

	if (pkt == 'g')
		p = &buf[1] + 15*8;
	else {
		p = &buf[1] + EXPEDITE_PC;
	}
	if (buf[0] == '+' )
		p++;
	for(i = 0; i < 8; i+=2)
	{
		pc_value |= fromhex(p[i]) << 4*(i+1) | fromhex(p[i+1]) << (4*i) ;
	}
	return pc_value;
}

/* get the current thread from the stop reply packet */
int getthread(char *buf)
{
	int tid, i;
	char *ptr, *ptr2;
	char buf2[100];

	sprintf(buf2,"%s", buf);
	tid = 0;
	ptr = strstr(buf2, "thread:");
	if (ptr == NULL)
		return 0;
	else {
		ptr += 7;
		ptr2 = strchr(ptr, ';');
		if (ptr2 == NULL)
			return 0;
		*ptr2 = 0;
		for (i = 0; i < strlen(ptr); i++)
			tid |= fromhex(ptr[i]) << 4*(strlen(ptr)-i-1);
		return tid;
	}
}

/* compute the expected length of the awaited reply. this is problematic when the */
/* run length encoding is used (implemented in gdbserver 6.6 and newer)	         */
inline int compute_len(char *buf, int n, int pthread_offset)
{
	int recvlen = 0;
	int reglen;
	reglen = ArmRegs + 5;
	if (IS_STEP(buf)) {
		return STOPPKT;
	}

	switch (buf[1])
	{
		case 'g': /* read registers */
			return reglen;
		case '?': /* get status */
			return STOPPKT;
		case 'G': case 'M': case 'Z': case 'z': case 'P': /* write all registers, write memory, BKPTS, P */
			return 7;
		case 'm':          /* read memory, recvlen depends on the number of bits that we want to read */
			if (buf[n-6] == ',')
				recvlen = (fromhex(buf[n-4]) | (fromhex(buf[n-5]) << 4))*2 + 5;
			else if (buf[n-5] == ',')
				recvlen = fromhex(buf[n-4])*2 + 5;
			else if (buf[n-7] == ',')
				recvlen = (fromhex(buf[n-4]) | (fromhex(buf[n-5]) << 4) | (fromhex(buf[n-6]) << 8))*2 + 5;
			else printf("ERROR, recvlen %s\n", buf);
				return recvlen;
		default:
			return MAXIMUM;
	}
}
/* end of utils.c */
