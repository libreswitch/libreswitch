#!/bin/sh

#  Copyright (C) 2015 Hewlett Packard Enterprise Development LP
#  All Rights Reserved.
#
#  Licensed under the Apache License, Version 2.0 (the "License"); you may
#  not use this file except in compliance with the License. You may obtain
#  a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
#  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
#  License for the specific language governing permissions and limitations
#  under the License.

#
# This script will show a listing of all the processes and threads
# running on the system, along with some associated info.
#

# Meanings of the fields reported by the kernel in /proc/<PID>/stat:
#  pid           process id
#  tcomm         filename of the executable
#  state         state (R is running, S is sleeping, D is sleeping in an uninterruptible wait, Z is zombie, T is traced or stopped)
#  ppid          process id of the parent process
#  pgrp          pgrp of the process
#  sid           session id
#  tty_nr        tty the process uses
#  tty_pgrp      pgrp of the tty
#  flags         task flags
#  min_flt       number of minor faults
#  cmin_flt      number of minor faults with child's
#  maj_flt       number of major faults
#  cmaj_flt      number of major faults with child's
#  utime         user mode jiffies
#  stime         kernel mode jiffies
#  cutime        user mode jiffies with child's
#  cstime        kernel mode jiffies with child's
#  priority      priority level
#  nice          nice level
#  num_threads   number of threads
#  it_real_value	(obsolete, always 0)
#  start_time    time the process started after system boot
#  vsize         virtual memory size
#  rss           resident set memory size
#  rsslim        current limit in bytes on the rss
#  start_code    address above which program text can run
#  end_code      address below which program text can run
#  start_stack   address of the start of the stack
#  esp           current value of ESP
#  eip           current value of EIP
#  pending       bitmap of pending signals (obsolete)
#  blocked       bitmap of blocked signals (obsolete)
#  sigign        bitmap of ignored signals (obsolete)
#  sigcatch      bitmap of catched signals (obsolete)
#  wchan         address where process went to sleep
#  0             (place holder)
#  0             (place holder)
#  exit_signal   signal to send to parent thread on exit
#  task_cpu      which CPU the task is scheduled on
#  rt_priority   realtime priority
#  policy        scheduling policy (man sched_setscheduler)
#  blkio_ticks   time spent waiting for block IO

show_process()
{
	wchan_str=""

	if [ $1 -eq $2 ]; then
		STATFILE="/proc/$1"
	else
		STATFILE="/proc/$1/task/$2"
	fi

	if [ ! -f $STATFILE/stat ]; then
		return
	fi

	read pid tcomm state ppid pgrp sid tty_nr tty_pgrp flags \
		min_flt cmin_flt maj_flt cmaj_flt utime stime cutime cstime \
		priority nice num_threads it_real_value start_time vsize rss rsslim \
		start_code end_code start_stack esp eip \
		pending blocked sigign sigcatch \
		wchan dummy0 dummy1 exit_signal task_cpu rt_priority policy \
		blkio_ticks unknown \
		< $STATFILE/stat

	if [ $wchan != "0" ] && [ -f $STATFILE/wchan ]; then
		read wchan_str < $STATFILE/wchan
	fi

	comm=$(echo $tcomm | sed -e s/\(// -e s/\)//)

	vsizekb=$((vsize/1024))
	rsskb=$((rss*4))

	if [ $1 -eq $2 ]; then
		printf "%5d      %-18s %2s %4d %4d %4d %4d %7d %7d %10d %10d %-18s\n" \
			$pid $comm $state $priority $nice $rt_priority $policy $vsizekb $rsskb $utime $stime $wchan_str
	else
		printf     "%10d %-18s %2s %4d %4d %4d %4d %7d %7d %10d %10d %-18s\n" \
			$pid $comm $state $priority $nice $rt_priority $policy $vsizekb $rsskb $utime $stime $wchan_str
	fi
}


usage()
{
	echo "Usage: `basename $0` [<single process name>]"
	echo "  The columns, as reported by the kernel, are:"
	echo "	   PID		process id"
	echo "	   Command	filename of the executable"
	echo "	   State	state (R running, S sleeping, D uninterruptible sleep, Z zombie, T traced/stopped)"
	echo "	   Pri		priority level"
	echo "	   Nice		nice level"
	echo "	   RTPri	realtime priority"
	echo "	   Pol		scheduling policy (man sched_setscheduler)"
	echo "	   VSZ		virtual memory size"
	echo "	   RSS		resident set memory size"
	echo "	   UserJiffs	user mode jiffies"
	echo "	   KernJiffs	kernel mode jiffies"
	echo "	   Blocked-On	kernel function where process went to sleep"
	echo "If you want to monitor a single process continuously, use something like this:"
	echo "  while :; do ps_threads vcmd && sleep 3 ; done"
	echo
}


if [ "$#" -eq 1 ]; then
	if [ "$1" == "--help" -o "$1" == "-h" -o "$1" == "-?" -o "$1" == "?" ]; then
		usage
		exit 1
	fi
fi

MATCHLIST=""
if [ "$#" -gt 0 ]; then
	for ARG in $*; do

		ARGPIDLIST=$(pidof "$ARG")
		if [ -n "$ARGPIDLIST" ]; then
			MATCHLIST="$MATCHLIST $ARGPIDLIST"
		else
			ARGPIDLIST=none
		fi
		printf "PIDs for %-14s\t%s\n" "$ARG" "$ARGPIDLIST"
	done
	if [ -z "$MATCHLIST" ]; then
		exit 1
	fi
fi

if [ -f "/proc/1/wchan" ]; then
	printf "%5s      %-16s%-5s%5s%5s%5s%5s%8s%8s%11s%11s%-19s\n" \
		"PID" "Command" "State" "Pri" "Nice" "RTPri" "Pol" "VSZ-KB" "RSS-KB" "UserJiffs" "KernJiffs" " Blocked-On"
else
	printf "%5s      %-16s%-5s%5s%5s%5s%5s%8s%8s%11s%11s\n" \
		"PID" "Command" "State" "Pri" "Nice" "RTPri" "Pol" "VSZ-KB" "RSS-KB" "UserJiffs" "KernJiffs"
fi

LIST=$(ls -1 /proc/*/statm | sed -e "s|^/proc/||" -e "s|/statm$||" -e "/self/d" | sort -n)

for PID in $LIST ; do

	if [ -n "$MATCHLIST" ]; then
		FOUND_PID=false
		for ONEPID in $MATCHLIST; do
			if [ "$PID" -eq "$ONEPID" ]; then
				FOUND_PID=true
				break
			fi
		done
		if [ "$FOUND_PID" = "false" ]; then
			continue
		fi
	fi

	show_process $PID $PID

	if [ -d /proc/$PID/task ]; then
		for TID in /proc/$PID/task/[0-9]* ; do
			if [ -d $TID ]; then
				if [ $PID -ne $(basename $TID) ]; then
					show_process $PID $(basename $TID)
				fi
			fi
		done | sort -n
	fi
done

echo

exit 0
