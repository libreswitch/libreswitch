#include "main.h"
#include "utils.h"
#include "remote.h"
#include "arm-dcc.h"

#include <netdb.h>
#include <sys/socket.h>
#include <netinet/tcp.h>
#include <stdlib.h>

/* seconds to wait for connect */
#define TIMEOUT 15
#define POLL_INTERVAL 2

#define SERIAL_TIMEOUT	-2
#define SERIAL_ERROR	-1
#define REMOTE_TIMEOUT 100

#define BUFSIZE  4095

/* NOTE: for gdbserver 6.6 and newer, the run length encoding have to be deactivated
 * so that the communication with the t32server works:
 * (in remote-utils.c, try_rle, return 1 after *(*p)++ = buf[0];)
 * hint: the t32server computes the length of the reply packet (len) so that recv will be called
 * wit exactly the length of the packet waiting in the queue to avoid waiting ca. 1.s for the timeout.
 * if the rle is used, the t32server cannot compute the len
 */
//! TRACE32 -> t32server <- gdbserver's
int ReceiveBlock(char *buffer, int len, int timeout, int fd)
{
	fd_set fds;
	int n, tmp, m;
	struct timeval tv;
	time_t t0;

	if ((t0 = time(0)) == -1)
	{
		return -4;
	}

	tv.tv_sec = 0;
	tv.tv_usec = 500000; // 500 ms
	n=0;
	while (n<len)
	{
		/* Check readability */
		FD_ZERO (&fds); FD_SET (fd, &fds);
		tmp = select (fd+1, &fds, (void *)0, (void *)0, &tv);
		/* Error. Return */
		if (tmp < 0)
		{
			return -1;
		}
		/* Data available. Read */
		else if ((tmp > 0) && FD_ISSET (fd, &fds))
		{
			if ((m = recv (fd, buffer+n, len - n,0)) <= 0)
			{
				return -2;
			}
			else n += m;
			buffer[n] = 0;
		} else
			break;
		/* Update time left. (0s < Error < 1s) */
		if ((tv.tv_sec -= (long) (difftime (time (0), t0) - timeout/1000.0)) < 0)
			tv.tv_sec = 0;
	}
	return n;
}

/* Read a single character from the remote end, masking it down to 7 bits. */
//! gdb function
static int readchar (int timeout, int fd)
{
	int		result;
	char	ch;

	if ((result = ReceiveBlock(&ch, 1, timeout, fd)) != 1)
	{
		if (result == SERIAL_ERROR)
		{
			return SERIAL_TIMEOUT;
		}
		if (result == 0)
			return SERIAL_TIMEOUT;
		return result;
	}
	return (ch & 0x7f);
}


//! gdb function
long read_frame (char *buf, long sizeof_buf, int fd)
{
	long	bc;
	int		c, c1, c2;

	bc = 0;
	while (1)
	{
		c = readchar(REMOTE_TIMEOUT, fd);
		switch (c)
		{
			case SERIAL_TIMEOUT:
				return -1;
			case '#':
			{
				buf[bc++] = c;
				c1 = readchar(REMOTE_TIMEOUT, fd);
				c2 = readchar(REMOTE_TIMEOUT, fd);

				if (c1 == SERIAL_TIMEOUT || c2 == SERIAL_TIMEOUT)
				{
					return -1;
				}
				else
				{
					buf[bc++] = c1;
					buf[bc]   = c2;
					buf[bc+1] = 0;
					return bc;
				}
			}
			default:
				if (bc < sizeof_buf - 1)
				{
					buf[bc++] = c;
					continue;
				}
				return -1;
		}
	}
}

//! gdb function
int putpkt (char *buf, int cnt, int fd)
{
	int             i;
	unsigned char   csum = 0;
	char            buf2[4095];
	char           *p;

	if (fd< 0)
		return -1;

	/* Copy the packet into buffer BUF2, encapsulating it and giving it a checksum. */

	p = buf2;
	*p++ = '$';

	for (i = 0; i < cnt; i++)
	{
		csum += buf[i];
		*p++ = buf[i];
	}
	*p++ = '#';
	*p++ = tohex((csum >> 4) & 0xf);
	*p++ = tohex(csum & 0xf);
	*p = 0;

	if (send(fd, buf2, strlen(buf2), 0) < 0)
		return -1;
	ReceiveBlock(buf, 1, 0, fd);
	return 0;
}

//! open a tcp socket and establish the connection
int GdbNetOpen(int port)
{
	struct sockaddr_in sockaddr;
	struct hostent *hostent;
	int n, fd, tmp, error;
	const char hostname[100] = {'1','2','7','.','0','.','0','.','1'};

	hostent = gethostbyname (hostname);
	if (!hostent)
	{
		fprintf (stderr, "%s: unknown host\n", hostname);
		return -1;
	}

	fd = socket (PF_INET, SOCK_STREAM, 0);

	if (fd < 0)
		return -1;

	sockaddr.sin_family = PF_INET;
	sockaddr.sin_port = htons(port);
	memcpy (&sockaddr.sin_addr.s_addr, hostent->h_addr, sizeof (struct in_addr));

	n = connect (fd, (struct sockaddr *) &sockaddr, sizeof (sockaddr));
	if (n < 0 && errno != EINPROGRESS)
	{
		error = errno;
		close(fd);
		if (error == 101)
		    return -3;
		return -2;
	}

	if (n)
	{
		/* looks like we need to wait for the connect */
		struct timeval t;
		fd_set rset, wset, eset;
		int polls = 0;
		fflush(stderr);
		FD_ZERO (&rset);

		do {
			FD_SET (fd, &rset);
			wset = rset;
			eset = rset;
			t.tv_sec = 0;
			t.tv_usec = 1000000 / POLL_INTERVAL;
			n = select (fd + 1, &rset, &wset, &eset, &t);
			polls++;

		} while (n == 0 && polls <= TIMEOUT * POLL_INTERVAL);

		if (n < 0 || polls > TIMEOUT * POLL_INTERVAL)
		{
			close (fd);
			fd = -1;
			return -2;
		}
	}

	/* Got something.  Is it an error? */
	{
		int res, err;
		socklen_t len;
		len = sizeof (err);
		res = getsockopt (fd, SOL_SOCKET, SO_ERROR, (void *) &err, &len);
		tmp = 1;
		// disable Nagle algorithm needed in some cases
		res = setsockopt(fd, IPPROTO_TCP, TCP_NODELAY, (char *) &tmp, sizeof(tmp));
		if (res < 0 || err)
		{
			close (fd);
			fd = -1;
			return -1;
		}

	}

	tmp = 1;
	// disable Nagle algorithm needed in some cases
	setsockopt(fd, IPPROTO_TCP, TCP_NODELAY, (char *) &tmp, sizeof(tmp));

	return fd;
}


//! gdb function
int getpkt (char *buf, int sizeof_buf, int timeout, int fd)  /* strip!! */
{
	int c;
	int val;
	char *ptr;

	if (fd < 0)
		return -1;
 	do
	{
		c = readchar (timeout, fd);

		if (c == SERIAL_TIMEOUT)
			return -1;
	} while (c != '$');

	/* We've found the start of a packet, now collect the data.  */
	val = read_frame (buf, sizeof_buf, fd);
	if (val >= 0)
	{
		send(fd, "+", 1, 0);
		ptr = strchr(buf, '#');
		if (ptr) *ptr = 0;
			return val;
	}
	send(fd, "+", 1, 0);
	return -1;

}

int GdbGetError(int fd)
{
	char buf[10];
	int cnt;

	do {
		cnt = ReceiveBlock(buf, 1, 0, fd);
	} while (cnt == 1 && buf[0] != '$' && buf[0] != '+');
	if (cnt < 0)
		return -1;
	if (buf[0] == '+')
		cnt = ReceiveBlock(buf, 6, 0, fd);
	else
		cnt = ReceiveBlock(buf+1, 5, 0, fd);
	if (cnt < 0)
		return -1;

	send(fd, "+", 1, 0);
	if (buf[1] == 'E')
	{
		cnt = ReceiveBlock(buf, 1, 0, fd);
		return -1;
	}
	return 0;
}
//! t32server -> TRACE32
int TransmitBlock(char *buf, int len, int timeout)
{
	int cnt = 0;

	if (len > 1)
		debugout("reply to host (%i): (\"%s\")\n", len, buf);
	cnt = send_dcc(buf, len, timeout, 0);

	if (cnt <= 0 && len > 1)
		debugout("TransmitBlock: error\n");
	return cnt;
}

int GdbWriteMemory(int fd, int addr, int value, int len)
{
	char buf[100];
	int error;

	sprintf(buf, "M%x,%x:%08x", addr, len, value);
	if (putpkt(buf, strlen(buf), fd) < 0)
	{
		printf("error writing at address 0x%x\n", addr);
		return -1;
	}
	error = GdbGetError(fd);
	return error;
}

static int CheckPacket(char *pkt, int recvlen, int fd)
{
	char *ptr;
	char buf[4095];
	int newlen, n, cnt;

	ptr = strrchr(pkt, '$');
	if (ptr == NULL)
		return -1;
	if (ptr != pkt && ptr != pkt+1)
		debugout("[unchecked packet]: (\"%s\")\n", pkt);
	sprintf(buf, "%s", ptr);
	ptr = strchr(buf, '#');
	if (ptr == NULL) // we have only received a part of our reply, so try to get the rest
	{
		n = strlen(buf);
		newlen = recvlen - n;
		if ((cnt = ReceiveBlock(buf + n, newlen, 0, fd)) > 0)
			buf[n+cnt] = 0;
	}
	sprintf(pkt, "%s", buf);
	return 0;
}

int GdbGetReply(char cmd, char *reply, int k, int recvlen, int fd)
{
	int cnt, rest, tmp;

	memset(reply, 0, BUFSIZE);

	if (recvlen == BUFSIZE || cmd == 'z' || cmd == 'Z' || cmd == 'P')
	{
		cnt = ReceiveBlock(reply, 1, 0, fd);
		if (reply[0] == '+')
			rest = 4;
		else
			rest = 5;
		cnt = ReceiveBlock(reply+1, rest, 0, fd);
		if (reply[2] == '#' || reply[3] == '#')
			goto done;
		tmp = ReceiveBlock(reply+cnt, recvlen, 0, fd);
		debugout("second part: (%i) %s\n", tmp, reply);
		if (tmp > 0) cnt+=tmp;
	}

	if (cmd == 's' || cmd == '?')
	{
		cnt = GdbGetState(k, 0, reply);
		goto done;
	}
	if (cmd == 'g' || cmd == 'm')
	{
		cnt = ReceiveBlock(reply, recvlen, 0, fd);
		goto done;
	}
	cnt = ReceiveBlock(reply, 7, 0, fd);
	if (reply[2] == 'E')
	{
		sprintf(reply, "$E0#75");
		cnt = 7;
		goto done;
	}
	if (reply[2] == 'O')
		goto done;
	if (recvlen - 7 > 0)
		cnt = ReceiveBlock(&reply[7], recvlen-7, 0, fd) + 7;
done:
	if (cnt != recvlen)
		debugout("!!!! %c [%i != %i]\n", cmd, cnt, recvlen);
	reply[cnt+1] = '\0';
	send(fd, "+", 1, 0);
	CheckPacket(reply, recvlen-1, fd);

	return cnt;
}

inline int GetT32CmdDCC(char *buf, int current_task)
{
	int data, nmax = -1, timeout = 1000, len, n, i;
	char tmp[4096];

start:
	n = 0;

	while (1)
	{
		if (n == 0)
			timeout = 1;
		else
			timeout = 10000; /* we received a part of the packet, so wait more for the rest */

		data = read_dcc (tmp, timeout);
		if (data <= 0)
		{
			if (n > 0)
				return n;
			else
				return -1;
		}
		len = ((data >> 24) & 0x3) + 1;
		if (n == 0)
			debugout("[T32]: (\"");
		for(i=0; i<len; i++)
		{
			buf[n] = (data >> (8*i)) & 0x7f;
			debugout("%c", buf[n]);
			if ((buf[n] == '$' || buf[n] == '&') && n != 0)
			{
				/* new packet in the middle of an old one */
				buf[0] = buf[n];
				n = 0;
			}
			if (buf[n] == '\003') // Break
			{
				GdbBreak(current_task);
				n = 0;
				buf[0] = 'x';
				len = data = 0;
				goto start;

			}
			if (buf[n] == '%') /* end of t32 command */
			{
				debugout("\")\n");
				buf[n+1] = 0;
				return n;
			}
			if (buf[n] == '#') /* end of gdb command = n+2 (checsum) */
			{
				nmax = n + 2;
			}
			n++;
			if (n > nmax && nmax != -1)
			{
				buf[n] = '\0';
				debugout("\")\n");
				return n;
			}
		}
		if (n > 1024)
		{
			debugout("TIMEOUT\n");
			n = 0;
			goto start;
		}
	}
	return n;
}

int GetT32Cmd(char *buf, char **newpkt, int current_task)
{
	buf[0] = 0;
	return GetT32CmdDCC(buf, current_task);

}
