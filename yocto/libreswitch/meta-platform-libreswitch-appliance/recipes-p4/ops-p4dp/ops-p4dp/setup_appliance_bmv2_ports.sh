#!/bin/bash
set -x

for iface in `seq 1 7` ; do
  /sbin/ip link set eth$iface netns emulns
  /sbin/ip netns exec emulns /sbin/ip link set dev eth$iface up
  echo port_add eth$iface `expr $iface - 1` | /sbin/ip netns exec emulns /usr/bin/bm_tools/runtime_CLI.py --json /usr/share/ovs_p4_plugin/switch_bmv2.json --thrift-port 10001
done
