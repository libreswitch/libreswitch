SUMMARY = "Quagga Routing Daemon"
LICENSE = "GPL-2.0 & LGPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=81bcece21748c91ba9992349a91ec11d\
                    file://COPYING.LIB;md5=01ef24401ded36cd8e5d18bfe947240c"

DEPENDS = "ops-utils ops-ovsdb ncurses perl-native openssl ops-supportability ops-cli"

# the "ip" command from busybox is not sufficient (flush by protocol flushes all routes)
RDEPENDS_${PN} += "iproute2"

BRANCH ?= "${LBS_REPO_BRANCH}"

SRC_URI = "${LBS_REPO_BASE_URL}/quagga;protocol=${LBS_REPO_PROTOCOL};branch=${BRANCH} \
    file://ops-zebra.service file://ops-bgpd.service file://ops-ospfd.service \
"

SRCREV = "2020dbf7fd779485386f04900e60167800af2e81"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

EXTRA_OECONF = "--disable-doc --disable-ripd \
 --disable-ripngd --disable-ospf6d --disable-babeld \
 --disable-watchquagga --disable-opaque-lsa --disable-ospfapi \
 --disable-ospfclient --disable-ospf-te --disable-rtadv --disable-rusage \
 --enable-user=root --enable-group=root --enable-multipath=32 \
 --enable-ovsdb \
"

FILES_${PN} += "/usr/share/opsplugins /usr/lib/cli/plugins/"
do_install_append() {
     install -d ${D}${systemd_unitdir}/system
     install -m 0644 ${WORKDIR}/ops-zebra.service ${D}${systemd_unitdir}/system/
     install -m 0644 ${WORKDIR}/ops-bgpd.service ${D}${systemd_unitdir}/system/
     install -m 0644 ${WORKDIR}/ops-ospfd.service ${D}${systemd_unitdir}/system/
     # Remove non-ovs configuration files
     rm -Rf ${D}${sysconfdir}*
     rm -Rf ${D}/usr/include/quagga/*
    install -d ${D}/usr/share/opsplugins
    for plugin in $(find ${S}/ops/opsplugins -name "*.py"); do \
        install -m 0644 ${plugin} ${D}/usr/share/opsplugins
    done
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "ops-zebra.service ops-bgpd.service ops-ospfd.service"

inherit libreswitch autotools pkgconfig systemd
