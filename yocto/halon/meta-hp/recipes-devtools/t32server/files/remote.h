#ifndef REMOTE_H_
#define REMOTE_H_

int putpkt (char *buf, int cnt, int n);
int getpkt (char *buf, int sizeof_buf, int timeout, int fd);
long read_frame (char *buf, long sizeof_buf, int fd);
int ConnectToTrace32(int port);
int ReceiveBlock(char *buffer, int len, int timeout, int fd);
int GdbNetOpen(int port);
int GdbGetError(int fd);
int GdbGetReply(char cmd, char *reply, int k, int recvlen, int fd);
int GdbWriteMemory(int fd, int addr, int value, int len);
int TransmitBlock(char *buf, int len, int timeout);
int GetT32Cmd(char *buf, char **newpkt, int current_task);


#endif /*REMOTE_H_*/
