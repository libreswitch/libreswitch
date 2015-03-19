#ifndef UTILS_H_
#define UTILS_H_

extern unsigned int ArmRegs;
#define STOPPKT   44

#define IS_CONT(str) ( (str[1]=='c') || (str[1]=='C') || !strncmp(str, "$vCont;c", 8) )
#define IS_STEP(str) ( (str[1]=='s') || !strncmp(str, "$vCont;s", 8) )

int fromhex (int a);
int tohex (int nib);
int getPC(char *buf, char pkt);
int getthread(char *buf);
inline int compute_len(char *buf, int n, int k);
void debugout(const char *format, ...);

#endif /*UTILS_H_*/
