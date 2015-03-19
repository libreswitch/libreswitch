/*********************************************************/
/* 11.06.08                                              */
/* Khaled Jmal - Lanterbach Datentechnik GmbH            */
/* this file contains the routines for file transfer     */
/* between TRACE32 and the t32server                     */
/*********************************************************/

#include "main.h"
#include "remote.h"
#include "arm-dcc.h"
#include "utils.h"

//! TASK.COPYDOWN
int GetFile(char *buf)
{
	char *content, tmpbuf[4095];
	int  fd, n, count;

	debugout("GetFile: %s\n", buf);
	content = strchr(buf, ';');
	if (!content)
		return -1;
		*content = 0;
		fd = open(buf, O_WRONLY | O_CREAT | O_TRUNC, S_IRWXU | S_IRWXG | S_IRWXO);
		if (fd < 0)
		{
			printf("cannot create file\n");
			TransmitBlock("-", 1, 1000);
			return -1;
		}
		TransmitBlock("+", 1, 1000);
		printf("receiving file..\n");
		while (1)
		{
			memset(tmpbuf, 0, sizeof(tmpbuf));
			{
			int i;
			i = n = 0;
			while (1)
			{
				n = read_dcc (tmpbuf + i, 5000);
				if (n == -1)
					break;
				i += ((n >> 24) & 0x3) + 1;
				if (i > 255)
					break;
				tmpbuf[i] = 0;
			}
			count = i;
			if (n < 0  && count == 0)
			{
				debugout("n = %i\n", n);
				break;
			}
		}
		if (count <= 0)
			break;
		n = write(fd, tmpbuf, count);
		debugout("writing %i bytes from %i, errno = %i\n", n, count, errno);
		if (n <= 0)
		{
			if (errno == ENOSPC)
				printf("error: no space left on device, file will be truncated\n");
			else if (errno != 0)
				printf("error %i writing to file\n", errno);
			TransmitBlock("-", 1, 1000);
			break;
		}
		else
			TransmitBlock("+", 1, 1000);
	}
	close(fd);
	TransmitBlock("+", 1, 1000);
	printf("File transfer complete\n");
	return 0;
}

//! TASK.COPYUP
int SendFile(char *buf)
{
	char *ptr;
	int fd, n, i, count, cnt;
	char tmpbuf[4095], c;
	int test = 0;

	count = 1;
	ptr = strchr(buf, ';');
	if (!ptr) return -1;
		*ptr = 0;
	ptr++;
	fd = open(buf, O_RDONLY, 0);
	printf("sending file %s\n", buf);
	if (fd < 0)
	{
		TransmitBlock("-", 1, 1000);
		printf("file %s not found\n", buf);
		return -1;
	}
	TransmitBlock("+", 1, 1000);
	while ((n = read(fd, tmpbuf, 0x100)) > 0)
	{
		if (n < 0) break;
		test++;
		if ((cnt = TransmitBlock(tmpbuf, n, 10000)) < n)
		{
			close(fd);
			return -1;
		}
		c = 0;
		i = 0;
		while (i < count)
		{
			int data;
			data = read_dcc(&c,10000);
			c = data & 0x7f;
			i++;
			if (c == '+')
				break;
		}
		if (c != '+')
		{
			close(fd);
			return -1;
		}
	}
	printf("File transfer complete\n");
	close(fd);
	return 0;
}
/* end of fileio.c */
