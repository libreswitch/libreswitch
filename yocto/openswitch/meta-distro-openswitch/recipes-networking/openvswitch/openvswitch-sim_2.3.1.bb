SUMMARY = "OpenvSwitch"
DESCRIPTION = "Open vSwitch is a production quality, multilayer virtual switch licensed under the open source Apache 2.0 license. It is designed to enable massive network automation through programmatic extension, while still supporting standard management interfaces and protocols (e.g. NetFlow, sFlow, SPAN, RSPAN, CLI, LACP, 802.1ag)"
HOMEPAGE = "http://openvswitch.org/"
SECTION = "networking"
LICENSE = "Apache-2"

DEPENDS += "bridge-utils openssl python perl"

RDEPENDS_${PN} += "util-linux-uuidgen util-linux-libuuid coreutils initscripts \
                   python perl perl-module-strict ${PN}-switch ${PN}-controller ovsdb"
RDEPENDS_${PN}-controller = "${PN} lsb ${PN}-pki ovsdb"
RDEPENDS_${PN}-switch = "${PN} openssl procps util-linux-uuidgen ovsdb"
RDEPENDS_${PN}-pki = "${PN}"
RDEPENDS_${PN}-brcompat = "${PN} ${PN}-switch"
RRECOMMENDS_${PN} += "kernel-module-openvswitch"

RDEPENDS_${PN}-ptest += "python-logging python-syslog python-argparse python-io \
                         python-fcntl python-shell python-lang python-xml python-math \
                         python-datetime python-netclient python sed"

# Some installers will fail because of an install order based on
# rdeps.  E.g. ovs-pki calls sed in the postinstall.  sed may be
# queued for install later.
RDEPENDS_${PN} += "sed gawk grep"

SRC_URI = "http://openvswitch.org/releases/openvswitch-2.3.1.tar.gz \
           file://openvswitch-switch \
           file://openvswitch-switch-setup \
           file://openvswitch-controller \
           file://openvswitch-controller-setup \
           file://openvswitch-add-target-python-handling.patch \
           file://openvswitch-add-target-perl-handling.patch \
           file://openvswitch-add-more-target-python-substitutions.patch \
           file://openvswitch-add-ptest.patch \
           file://run-ptest \
           file://0001-Disable-git-hooks-that-cause-trouble-when-working-in.patch \
           file://custom-prefix-takes-precedence.patch \
           file://openvswitch-sim.service \
           file://ovsdb-server-sim.service \
           file://openvswitch-mod.conf \
           "

SRC_URI[md5sum] = "c008c1de0a8b6363b37afa599105d6d6"
SRC_URI[sha256sum] = "21174901c311d54703b4a7f498be0c42e5d2cc68bdecb0090dbb555e2af4bcd2"

S = "${WORKDIR}/openvswitch-${PV}"

LIC_FILES_CHKSUM = "file://COPYING;md5=5973c953e3c8a767cf0808ff8a0bac1b"

# Don't compile kernel modules by default since it heavily depends on
# kernel version. Use the in-kernel module for now.
# distro layers can enable with EXTRA_OECONF_pn_openvswitch += ""
# EXTRA_OECONF = "--with-linux=${STAGING_KERNEL_DIR} KARCH=${TARGET_ARCH}"

# Changing the default location to /opt to avoid conflict
# with the OVS infrastructure used by OpenSwitch.
PKG_CONFIG_DIR := "${STAGING_DIR_HOST}${libdir}/pkgconfig"
PKG_CONFIG_DIR[vardepvalue] = ""
OVS_PREFIX="/opt/openvswitch"
sys_bindir := "${bindir}"
orig_prefix := "${prefix}"
prefix="${OVS_PREFIX}"
exec_prefix="${OVS_PREFIX}"

EXTRA_OECONF += "TARGET_PYTHON=${sys_bindir}/python \
                 TARGET_PERL=${sys_bindir}/perl \
                 --with-rundir=${localstatedir}/run/openvswitch-sim \
                "

ALLOW_EMPTY_${PN}-pki = "1"
PACKAGES =+ "${PN}-controller ${PN}-switch ${PN}-brcompat ${PN}-pki ovsdb"

PROVIDES = "${PACKAGES}"

FILES_${PN} += "${datadir}/openvswitch"
FILES_${PN}-controller = "${sysconfdir}/init.d/openvswitch-controller \
    ${sysconfdir}/default/openvswitch-controller \
    ${sysconfdir}/openvswitch-controller \
    ${bindir}/ovs-controller"

FILES_${PN}-brcompat = "${sbindir}/ovs-brcompatd"

FILES_${PN}-switch = "${sysconfdir}/init.d/openvswitch-switch \
           ${sysconfdir}/default/openvswitch-switch \
           ${sysconfdir}/modules-load.d \
           "

FILES_ovsdb = "/run /var/run /var/log /var/volatile ${bindir}/ovsdb* \
               ${sbindir}/ovsdb-server ${datadir}/ovsdbmonitor \
               ${sysconfdir}/openvswitch/"

inherit autotools ptest systemd

EXTRA_OEMAKE += "TEST_DEST=${D}${PTEST_PATH} TEST_ROOT=${PTEST_PATH}"

do_install_ptest() {
    oe_runmake test-install
}

do_install_append() {
    install -d ${D}/${sysconfdir}/default/
    install -d ${D}/var/run/openvswitch-sim
    install -m 660 ${WORKDIR}/openvswitch-switch-setup ${D}/${sysconfdir}/default/openvswitch-switch
    install -d ${D}/${sysconfdir}/openvswitch-controller
    install -m 660 ${WORKDIR}/openvswitch-controller-setup ${D}/${sysconfdir}/default/openvswitch-controller
    # Renaming the open source switchd to ovs-vswitchd-sim to differentiate
    # from OpenSwitch switchd process
    /bin/mv  ${D}${OVS_PREFIX}/sbin/ovs-vswitchd ${D}${OVS_PREFIX}/sbin/ovs-vswitchd-sim
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/ovsdb-server-sim.service ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/openvswitch-sim.service ${D}${systemd_unitdir}/system
    install -d ${D}${sysconfdir}/modules-load.d/
    install -m 0644 ${WORKDIR}/openvswitch-mod.conf ${D}${sysconfdir}/modules-load.d/openvswitch.conf

    install -d ${D}${sysconfdir}/tmpfiles.d
    echo "d /run/openvswitch-sim/ - - - -" > ${D}${sysconfdir}/tmpfiles.d/openswitch-sim.conf
}

pkg_postinst_${PN}-pki () {
    # can't do this offline
    if [ "x$D" != "x" ]; then
        exit 1
    fi
    if test ! -d $D/${datadir}/openvswitch/pki; then
        ovs-pki init --dir=$D/${datadir}/openvswitch/pki
    fi
}

pkg_postinst_${PN}-controller () {
    # can't do this offline
    if [ "x$D" != "x" ]; then
        exit 1
    fi

    if test ! -d $D/${datadir}/openvswitch/pki; then
        ovs-pki init --dir=$D/${datadir}/openvswitch/pki
    fi

    cd $D/${sysconfdir}/openvswitch-controller
    if ! test -e cacert.pem; then
        ln -s $D/${datadir}/openvswitch/pki/switchca/cacert.pem cacert.pem
    fi
    if ! test -e privkey.pem || ! test -e cert.pem; then
        oldumask=$(umask)
        umask 077
        ovs-pki req+sign --dir=$D/${datadir}/openvswitch/pki tmp controller >/dev/null
        mv tmp-privkey.pem privkey.pem
        mv tmp-cert.pem cert.pem
        mv tmp-req.pem req.pem
        chmod go+r cert.pem req.pem
        umask $oldumask
    fi
}

SYSTEMD_PACKAGES = "${PN}"

SYSTEMD_SERVICE_${PN} += "openvswitch-sim.service ovsdb-server-sim.service"
