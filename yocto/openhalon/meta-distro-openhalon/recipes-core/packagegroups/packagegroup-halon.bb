DESCRIPTION = "Package groups for Halon applications"
LICENSE = "HP"
PR = "r1"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PROVIDES = "${PACKAGES}"
PACKAGES = ' \
            packagegroup-halon \
            packagegroup-halon-base \
            packagegroup-halon-min \
            packagegroup-halon-min-debug \
            packagegroup-halon-core \
            '

#
# packages which content depend on MACHINE_FEATURES need to be MACHINE_ARCH
#
PACKAGE_ARCH = "${MACHINE_ARCH}"

RDEPENDS_packagegroup-halon-base = "\
    i2c-tools \
    mtd-utils \
    gptfdisk \
    packagegroup-base-serial \
    lttng-tools lttng-modules lttng-ust babeltrace \
    kexec kdump \
    rsyslog \
    iproute2 \
    vim \
    systemd-dev \
    valgrind \
    valgrind-memcheck \
    valgrind-helgrind \
    sudo \
    pwauth \
"

RDEPENDS_packagegroup-halon-base_append_arm = "\
    u-boot-fw-utils \
    t32server\
    "

RDEPENDS_packagegroup-halon-min = "\
    python \
    python-datetime python-netclient python-threading python-misc python-json \
    yaml-cpp \
    redis-py \
    python-compile \
    python-email \
    python-distutils \
    python-shell \
    python-pexpect \
    python-unittest \
    python-subprocess \
    python-jsonschema \
    python-flask python-werkzeug python-itsdangerous python-jinja2 python-markupsafe \
    python-unixadmin \
    libevent \
    util-linux-uuidgen \
    iptables \
    "

RDEPENDS_packagegroup-halon-min-debug = "\
    file strace ldd tcpdump gdb gdbserver eglibc-thread-db \
    iperf ethtool tcf-agent nfs-utils-client \
    "

RDEPENDS_packagegroup-halon-core = "\
    "
