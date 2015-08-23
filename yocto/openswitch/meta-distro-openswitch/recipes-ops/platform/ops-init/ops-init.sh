#!/bin/bash
set -x

/sbin/ip netns add swns
/sbin/ip netns exec swns /sbin/ifconfig lo up
/sbin/ip netns add nonet
/sbin/ip netns exec nonet /sbin/ifconfig lo up
