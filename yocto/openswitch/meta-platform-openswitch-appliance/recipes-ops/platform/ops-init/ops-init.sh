#!/bin/bash
set -x

/sbin/ip netns add swns
/sbin/ip netns exec swns /sbin/ifconfig lo up
/sbin/ip netns add nonet
/sbin/ip netns exec nonet /sbin/ifconfig lo up

# On Appliance machine we need to move the interfaces into the swns
for iface in eth1 eth2 eth3 ; do
  /sbin/ip link set $iface netns swns
done
