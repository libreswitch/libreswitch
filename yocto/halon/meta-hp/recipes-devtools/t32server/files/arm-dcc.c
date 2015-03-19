/*********************************************************************************/
/* 11.06.2008                                                                    */
/* Khaled Jmal - Lauterbach Datentechnik GmbH                                    */
/*                                                                               */
/* DCC functions                                                                 */
/*********************************************************************************/
#include <stdio.h>
#include <unistd.h>
#include <time.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <string.h>

#include "arm-dcc.h"

#define MIN(A,B) (( (A) < (B)) ? (A) : (B))

char serial_dcc = 0;
static int ttyJ = 0;
static int tty_count_out;
static char tty_buf_out[TTY_BUF_SIZE];
static int tty_sent = 0;
int armfamily; 	/* = 9 for arm7,9 and =11 for arm10,11, CORTEX */

int get_dcc_status(int arm)
{
	int status = 0;

	if (arm == 9)
		asm volatile("MRC p14,0,%0,c0,c0": "=r" (status));
	else if (arm == 11)
	{
		asm volatile("MRC p14,0,%0,c0,c1": "=r" (status));
		status = ((status & 0x40000000) >> 30) + ((status & 0x20000000) >> 28);
	}

	return status;
}

int read_dcc_reg(int arm)
{
	int data = -1;

	if (arm == 9)
		asm volatile("MRC p14,0,%0,c1,c0": "=r" (data));
	else if (arm == 11)
		asm volatile("MRC p14,0,%0,c0,c5": "=r" (data));

	return data;
}

void write_dcc_reg(int arm, int data, int terminal)
{
 	if (!terminal)
		data |= 0x0c000000;
	if (arm == 9)
		asm volatile("MCR p14,0,%0,c1,c0"::"r" (data));
	else if (arm == 11)
		asm volatile("MCR p14,0,%0,c0,c5"::"r" (data));
	return;
}


int send_dcc(char *buf, int cnt, int timeout, int terminal)
{
	int n, r, status;
	int i=0;
	int data = 0;
	time_t startingTime;

	r = cnt%3;
	n = cnt/3;
	while (i<n*3)
	{
		data = buf[i] | buf[i+1] << 8 | buf[i+2] << 16 | 0x02000000;
		startingTime = time(NULL);
		do {
			status = get_dcc_status(armfamily);
			if (((time(NULL)-startingTime)*2000) >= timeout) {
			    return i;
			}

		} while (status & 0x2);
		write_dcc_reg(armfamily, data, terminal);
		i+=3;
	}
	data = 0;
	if (r!=0)
	{
		if (r==1)
		{
 			data = buf[i];
		}
		else if(r==2)
		{
			data = buf[i] | buf[i+1] << 8 | 0x01000000;
		}
		startingTime = time(NULL);
		do {
			status = get_dcc_status(armfamily);
			if (((time(NULL)-startingTime)*2000) >= timeout) {
			    return i;
			}

		} while (status & 0x2);
		write_dcc_reg(armfamily, data, terminal);
	}

	return cnt;
}

/* this function is called when the terminal and run mode are multiplexed over DCC. */
void console_read()
{
	int n;
	int cnt;

	if (tty_count_out-tty_sent > 0) {
		n = MIN(TERMINAL_BLOCK_SIZE,tty_count_out-tty_sent);
		cnt = send_dcc(tty_buf_out+tty_sent, n, 1000, 1);
		tty_sent+=cnt;
		return;
	}
	tty_sent = 0;
	tty_count_out = read(ttyJ, tty_buf_out, 2000);
	lseek(ttyJ, 0, SEEK_SET);
	if (tty_count_out) {
		n = MIN(TERMINAL_BLOCK_SIZE,tty_count_out-tty_sent);
		cnt = send_dcc(tty_buf_out+tty_sent, n, 1000, 1);
		tty_sent+=cnt;
	}
}

int read_dcc(char *buf, int timeout)
{
	int status, channel;
	int data, len, i;
	time_t startingTime;

	startingTime = time(NULL);
	do {
		status = get_dcc_status(armfamily);
		if (((time(NULL)-startingTime)*2000) >= timeout)
		{
			return -1;
		}
		if (!(status & 0x1) && serial_dcc) /* if terminal packet, store in buffer */
			console_read();
	} while (!(status & 0x1));

	data = read_dcc_reg(armfamily);
	len = ((data >> 24) & 0x3) + 1;
	if (len > 3) {
		return -1;
	}
	for(i=0; i<len; i++)
		buf[i] = (data >> (i*8));

	channel = (data >> 24) & 0xfc;
	if (channel == 0) {
		write(ttyJ, buf, len);
		return -1;
	}
	return data;
}

int init_dcc_console()
{
	char tmp[5];
	int id;

	ttyJ = open("/proc/ttyJf", O_RDWR, 0);
	if (ttyJ < 0) {
		printf("error redirecting serial console\n");
		return -1;
	}
	printf("redirecting serial dcc console..\n");
	sprintf(tmp, "T32");
	id = getpid();
	tmp[3] = (id >> 8) & 0xff;
	tmp[4] = id & 0xff;
	write(ttyJ, tmp, 5);
	return 0;
}

int send_dcc_fast(int data1, int data2, int data3, int len)
{
	int status;
	time_t startingTime = time(NULL);

	do {
		status = get_dcc_status(armfamily);
		if (((time(NULL)-startingTime)*2000) >= 100)
		    return -1;
	} while (!(status & 0x1) && (status & 0x2));

	if (!(status & 0x2))
	{
		write_dcc_reg(armfamily, data1, 0);
		if (len == 1)
		{
		    return 0;
		}
		startingTime = time(NULL);
		do {
			status = get_dcc_status(armfamily);
			if (((time(NULL)-startingTime)*2000) >= 100)
				return -1;

		} while (status & 0x2);
		write_dcc_reg(armfamily, data2, 0);
		if (len == 2)
			return 0;
		startingTime = time(NULL);
		do {
			status = get_dcc_status(armfamily);
			if (((time(NULL)-startingTime)*2000) >= 100)
				return -1;
		} while (status & 0x2);
		write_dcc_reg(armfamily, data3, 0);
		return 0;
	}
	return -1;
}

void get_arm_version()
{
	int count = 0;;
	FILE * fd;
	char buf[500];
	char *res, *ptr;

	fd = fopen("/proc/cpuinfo", "r");
	if (fd < 0) {
		printf("error identifying arm version, assuming it is arm 7/9\n");
		armfamily = 9;
		return;
	}
	while (1) {
		res = fgets(buf, sizeof buf - 1, fd);
		if (res == NULL || res == EOF)
			break;
		if (count == 0)
			printf("%s", buf);
		count++;
		if (strstr(buf, "Processor") || strstr(buf, "processor") || strstr(buf, "PROCESSOR")) {
			if (strstr(buf, "CORTEX") || strstr(buf, "cortex")) {
				armfamily = 11;
	 			goto done;
			}
			ptr = strstr(buf, "ARM");
			if (ptr) {
				ptr += 3;
				if (*ptr == '9' || *ptr == '7') {
					armfamily = 9;
					goto done;
				} else if (ptr[0] == '1' && ptr[1] == '1') {
					armfamily = 11;
					goto done;
				}
			}
			continue;
		}
		if (strstr(buf, "architecture")) {
			ptr = strchr(buf, ':');
			if (ptr)
				ptr+=2;
			else
				continue;
			if (*ptr == '5') {
				armfamily = 9;
			} else {
				armfamily = 11;
			}
			goto done;
		}
		if (strstr(buf, "CPU part") || strstr(buf, "cpu part")) {
			if (strstr(buf, "0x9") || strstr(buf, "0x7")) {
				armfamily = 9;
			} else {
				armfamily = 11;
			}
		}
	}
	printf("error identifying arm version, assuming it is arm 7/9\n");
	armfamily = 9;
	return;
done:
	fclose(fd);
}
