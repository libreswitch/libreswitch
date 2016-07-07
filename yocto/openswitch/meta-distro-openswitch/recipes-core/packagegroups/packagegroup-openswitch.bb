DESCRIPTION = "Package groups for OpenSwitch applications"
LICENSE = "Apache-2.0"
PR = "r1"

#
# packages which content depend on MACHINE_FEATURES need to be MACHINE_ARCH
#
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PROVIDES = "${PACKAGES}"
PACKAGES = ' \
            packagegroup-ops \
            packagegroup-ops-base \
            packagegroup-ops-min \
            packagegroup-ops-min-debug \
            packagegroup-ops-core \
            '

PACKAGES += "${@bb.utils.contains("IMAGE_FEATURES", "ops-p4", "packagegroup-ops-p4", "", d)}"
PACKAGES += "${@bb.utils.contains("MACHINE_FEATURES", "ops-container", "packagegroup-ops-container", "", d)}"

RDEPENDS_packagegroup-ops-base = "\
    os-release \
    i2c-tools \
    mtd-utils \
    gptfdisk \
    packagegroup-base-serial \
    lttng-tools lttng-modules lttng-ust babeltrace \
    kexec kdump \
    rsyslog \
    iproute2 dhcp-client\
    vim \
    tzdata-posix \
    valgrind \
    valgrind-memcheck \
    valgrind-helgrind \
    sudo \
    pwauth \
    shadow \
    cronie \
    systemd-analyze \
    auditd audispd-plugins audit-python \
    inetutils-hostname inetutils-ifconfig \
    inetutils-tftp inetutils-traceroute inetutils-ftp inetutils-telnet \
    iputils-traceroute6 iputils-ping iputils-ping6 \
    wget curl \
    libcap-bin \
    ops-init \
    virtual/switchd \
    virtual/ops-switchd-switch-api-plugin \
    ops-ovsdb \
    ops-hw-config \
    ops-cfgd ops-fand ops-ledd ops-pmd ops-powerd ops-sysd ops-tempd \
    ops-dhcp-tftp \
    ops-intfd ops-lacpd ops-lldpd ops-vland ops-arpmgrd ops-passwd-srv \
    ops-script-utils \
    ops-cli ops-restd ops-webui \
    ops-classifierd \
    ops-portd ops-quagga \
    ops-aaa-utils \
    ${@bb.utils.contains('MACHINE_FEATURES', 'ops-container', '', 'ops-bufmond',d)} \
    ops-broadview \
    ops-mgmt-intf \
    dnsmasq \
    ops-checkmk-agent \
    ops-ansible \
    ops-ntpd \
    ops-supportability \
    strongswan \
    firejail \
    ops-ipapps \
    ops-stpd \
    ops-sysmond \
    nicstat \
    sysstat \
    ${@bb.utils.contains("MACHINE_FEATURES", "ops-kdump", "ops-kdump", "", d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "ops-container-coredump", "ops-container-coredump", "", d)} \
    ops-snmpd \
    ops-l2macd \
"

RDEPENDS_packagegroup-ops-base_append_arm = "\
    u-boot-fw-utils \
    t32server\
    "

RDEPENDS_packagegroup-ops-min = "\
    python \
    python-pyroute2 \
    python-paramiko \
    python-netclient \
    python-netserver \
    yaml-cpp \
    libevent \
    util-linux \
    iptables \
    "

RDEPENDS_packagegroup-ops-min-debug = "\
    file strace ldd tcpdump gdb gdbserver eglibc-thread-db \
    iperf ethtool tcf-agent nfs-utils-client \
    "

RDEPENDS_packagegroup-ops-core = "\
    "

RDEPENDS_packagegroup-ops-p4 = "\
    ops-switchd-p4switch-plugin \
    ops-p4dp \
    ops-p4c \
    "

RDEPENDS_packagegroup-ops-container = "\
    host-sflow \
    "
