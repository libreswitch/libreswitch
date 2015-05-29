#!/bin/bash
set -x

/sbin/ip netns add swns
/sbin/ip netns exec swns /sbin/ifconfig lo up
