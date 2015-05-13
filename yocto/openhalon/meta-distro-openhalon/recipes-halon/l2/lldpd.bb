SUMMARY = "Halon LLDP Daemon"
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://README.md;beginline=249;endline=259;md5=c39f3ef7df23415cc6e3f0d278dd31f4"

DEPENDS = "halonutils config-yaml halon-ovsdb libevent openssl"

SRC_URI = "git://git.openhalon.io/openhalon/lldpd;protocol=http;preserve_origin=1 \ 
	  file://lldpd.service \
"
SRCREV="${AUTOREV}"

S = "${WORKDIR}/git"

inherit halon autotools systemd pkgconfig

# Autoreconf breaks on
# gnu-configize: `configure.ac' or `configure.in' is required
#
# Works good enough without autoreconf
do_configure() {
	cd ${S}
        autoreconf -fi
        cd ${B}
	oe_runconf
}

# Disable readline to skip GPL linking
EXTRA_OECONF = "--enable-ovsdb --disable-privsep --without-readline"

do_install_append() {
     install -d ${D}${systemd_unitdir}/system
     install -m 0644 ${WORKDIR}/lldpd.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "lldpd.service"

FILES_${PN} += "/usr/share/zsh usr/lib/sysusers.d"

