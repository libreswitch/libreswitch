#!/bin/bash
set -x

/sbin/ip netns add swns
/sbin/ip netns exec swns /sbin/ifconfig lo up
/sbin/ip netns add nonet
/sbin/ip netns exec nonet /sbin/ifconfig lo up

# Calculate the max interface available
default_max=7
# Reconfigure the ports.yaml
yaml_file=/etc/openswitch/platform/Generic-x86/X86-64/ports.yaml
new_max=$(awk '/eth.*:/ { print $1 }' /proc/net/dev | sed -e 's/://' -e 's/eth//' | sort -h | tail -n 1)
sed -i -e "s/\(number_ports:.*\)${default_max}/\1${new_max}/" $yaml_file
sed -i '/^ports:/q' $yaml_file

for iface in `seq 1 $new_max` ; do
  cat <<EOF >>$yaml_file
    -  name:             eth${iface}
       switch_device:      0
       switch_device_port: ${iface}
       pluggable:          False
       connector:          RJ45
       max_speed:          1000
       speeds:             [1000]  # supported speeds in Mb/S
       capabilities:       [enet1G]
       subports:           []
       supported_modules:  [TBD]

EOF
done

# On Appliance machine we need to move the interfaces into the swns
for iface in `seq 1 $new_max` ; do
  /sbin/ip link set eth$iface netns swns
done

# If we are in container environment, disable the ttys
if systemd-detect-virt -q -c ; then
  systemctl disable getty@
  systemctl disable serial-getty@
fi
