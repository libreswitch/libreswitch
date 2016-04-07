SUMMARY = "SNMP Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "net-snmp ops-openvswitch ops-ovsdb ops-cli"

RDEPENDS_${PN} = "net-snmp-client net-snmp-server net-snmp-mibs net-snmp-libs perl"

SRC_URI = "git://git.openswitch.net/openswitch/ops-snmpd;protocol=http\
           file://ops-snmpd.service\
           file://snmpd.conf"

SRCREV="7c054c44dc3df70e02e0a9a942578435c3f53915"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
#PV = "git${SRCPV}"

S = "${WORKDIR}/git"

EXTRA_OECONF = "--enable-ovsdb"

do_install_append() {
    install -d ${D}${sysconfdir}/snmp
    install -d ${D}${systemd_unitdir}/system
    install -m 644 ${WORKDIR}/snmpd.conf ${D}${sysconfdir}/snmp/
    install -m 0644 ${WORKDIR}/ops-snmpd.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "ops-snmpd.service"

FILES_${PN} += "/usr/lib/cli/plugins/"

inherit openswitch cmake systemd
