copy_machine_configuration()
{
    path=$1
    rootfs=$2
    name=$3
    link=$4

    declare -A virtual_to_physical_port_map
    virtual_to_physical_port_map=(["eth1"]="$link" ["eth2"]="$link" ["eth3"]="$link")

    # Load custom map if present
    if [ -f $BUILD_ROOT/build/lxc_port_map.sh ]; then
        echo "Loading a custom port map from $BUILD_ROOT/build/lxc_port_map.sh..."
        source $BUILD_ROOT/build/lxc_port_map.sh
    fi

    cat <<EOF >> $path/config

# Dataplane Interfaces

lxc.network.type = macvlan
lxc.network.macvlan.mode = private
lxc.network.flags = up
lxc.network.link = ${virtual_to_physical_port_map["eth1"]}
lxc.network.name = eth1
lxc.network.mtu = 1500

lxc.network.type = macvlan
lxc.network.macvlan.mode = private
lxc.network.flags = up
lxc.network.link = ${virtual_to_physical_port_map["eth2"]}
lxc.network.name = eth2
lxc.network.mtu = 1500

lxc.network.type = macvlan
lxc.network.macvlan.mode = private
lxc.network.flags = up
lxc.network.link = ${virtual_to_physical_port_map["eth3"]}
lxc.network.name = eth3
lxc.network.mtu = 1500
EOF
}
