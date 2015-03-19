#ifndef BREAKPOINT_H_
#define BREAKPOINT_H_

static const int opcode_len = 4;

/* Under ARM GNU/Linux the traditional way of performing a breakpoint
   is to execute a particular software interrupt, rather than use a
   particular undefined instruction to provoke a trap.  Upon exection
   of the software interrupt the kernel stops the inferior with a
   SIGTRAP, and wakes the debugger.  */

/* However, the EABI syscall interface (new in Nov. 2005) does not look at
   the operand of the swi if old-ABI compatibility is disabled.  Therefore,
   use an undefined instruction instead.  This is supported as of kernel
   version 2.5.70 (May 2003), so should be a safe assumption for EABI
   binaries.  */

#ifdef __ARM_EABI__
static const int breakpoint_instr = 0xf001f0e7; /* undef */
#else
static const int breakpoint_instr = 0x01009fef; /* swi 0x9f0001*/
#endif

#define REMOVE 0x32 /* '2' */
#define CHECK  0x31 /* '1' */

typedef struct {
	int address;
	int desc;
	int original;
} CArmMonitorBreakpoints;

int RemoveBreakpoints(int process, int fd);
int UpdateBreakpointList(char type, int addr, int desc, int fd);
int SetBreakpoints(int process, int fd);
int GetBreakSignal(int current, int pc);


#endif /*BREAKPOINT_H_*/
