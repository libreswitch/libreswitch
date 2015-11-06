SUMMARY = "Quagga Routing Daemon"
LICENSE = "GPL-2.0 & LGPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=81bcece21748c91ba9992349a91ec11d\
                    file://COPYING.LIB;md5=01ef24401ded36cd8e5d18bfe947240c"

DEPENDS = "ops-utils ops-ovsdb ncurses perl-native openssl"

# the "ip" command from busybox is not sufficient (flush by protocol flushes all routes)
RDEPENDS_${PN} += "iproute2"

SRC_URI = "git://git.openswitch.net/openswitch/ops-quagga;branch=feature/bgpclidev;protocol=http \
    file://ops-zebra.service file://ops-bgpd.service \
"

SRCREV = "${AUTOREV}"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

EXTRA_OECONF = "--disable-doc --disable-ripd \
 --disable-ripngd --disable-ospfd --disable-ospf6d --disable-babeld \
 --disable-watchquagga --disable-opaque-lsa --disable-ospfapi \
 --disable-ospfclient --disable-ospf-te --disable-rtadv --disable-rusage \
 --enable-user=root --enable-group=root --enable-multipath=32 \
 --enable-ovsdb \
"

do_install_append() {
     install -d ${D}${systemd_unitdir}/system
     install -m 0644 ${WORKDIR}/ops-zebra.service ${D}${systemd_unitdir}/system/
     install -m 0644 ${WORKDIR}/ops-bgpd.service ${D}${systemd_unitdir}/system/
     # Remove non-ovs configuration files
     rm -Rf ${D}${sysconfdir}*
     rm -Rf ${D}/usr/include/quagga/*
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "ops-zebra.service ops-bgpd.service"

inherit openswitch autotools pkgconfig systemd
