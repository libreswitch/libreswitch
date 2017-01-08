SUMMARY = "SNMP Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "net-snmp ops-openvswitch ops-ovsdb ops-cli"

RDEPENDS_${PN} = "net-snmp-client net-snmp-server net-snmp-mibs net-snmp-libs perl"

BRANCH ?= "${LBS_REPO_BRANCH}"

SRC_URI = "${LBS_REPO_BASE_URL}/snmpd;protocol=${LBS_REPO_PROTOCOL};branch=${BRANCH} \
           file://ops-snmpd.service\
           file://snmpd.conf\
           "

SRCREV="98ae5991e5d1f48af1d6a9ad9fc0d3f1d6a55209"

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

FILES_${PN} += "${libdir}/libops_snmptrap.so.1* /usr/lib/cli/plugins"

inherit libreswitch cmake systemd
