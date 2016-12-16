#!/bin/sh

mknod /dev/linux-bcm-knet c `awk '/bcm-knet/ { print $1 }' /proc/devices` 0
mknod /dev/linux-user-bde c `awk '/user-bde/ { print $1 }' /proc/devices` 0
mknod /dev/linux-kernel-bde c `awk '/kernel-bde/ { print $1 }' /proc/devices` 0
