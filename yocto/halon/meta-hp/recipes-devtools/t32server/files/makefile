###############################################
# Khaled Jmal - Lauterbach Datentechnik 2008 #
##############################################

RM := rm -rf

C_SRCS += \
./arm-dcc.c \
./breakpoint.c \
./fileio.c \
./main.c \
./remote.c \
./thread.c \
./utils.c 

OBJS += \
./arm-dcc.o \
./breakpoint.o \
./fileio.o \
./main.o \
./remote.o \
./thread.o \
./utils.o 

DEPS += \
${addprefix ./, \
arm-dcc.d \
breakpoint.d \
fileio.d \
main.d \
remote.d \
thread.d \
utils.d \
}

%.o: %.c
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C Compiler'
	@echo $(CC) -Wall -g -c -o$@ $<
	@$(CC) -Wall -g -c -o$@ $< && \
	echo -n $(@:%.o=%.d) $(dir $@) > $(@:%.o=%.d) && \
	$(CC) -MM -MG -P -w -Wall -c  $< >> $(@:%.o=%.d)
	@echo 'Finished building: $<'
	@echo ' '
	
LIBS := -lpthread

all: t32server

t32server: $(OBJS)
	@echo 'Building target: $@'
	@echo 'Invoking: GCC C Linker'
	@echo $(CC)  -ot32server $(OBJS) $(LIBS)
	@$(CC)  -ot32server $(OBJS) $(LIBS)
	@mkdir bin
	@mv t32server bin
	@rm -f *.o *.d
	@echo 'Finished building target: $@'
	@echo ' '
	
clean:
	-$(RM) $(OBJS)$(DEPS) bin
	-@echo ' '