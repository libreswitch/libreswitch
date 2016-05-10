SUMMARY = "OpenSwitch Interface Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-utils ops-hw-config ops-ovsdb ops-cli ops-supportability ops-snmpd"

BRANCH ?= "${OPS_REPO_BRANCH}"

SRC_URI = "${OPS_REPO_BASE_URL}/ops-intfd;protocol=${OPS_REPO_PROTOCOL};branch=${BRANCH} \
           file://ops-intfd.service \
           "

SRCREV = "3dc57e70f948f5d31a9a5310bf7eb0fb541a9dd0"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

do_install_append() {
     install -d ${D}${systemd_unitdir}/system
     install -m 0644 ${WORKDIR}/ops-intfd.service ${D}${systemd_unitdir}/system/

     install -d ${D}/usr/share/opsplugins
     for plugin in $(find ${S}/opsplugins -name "*.py"); do \
       install -m 0644 ${plugin} ${D}/usr/share/opsplugins
     done
}

FILES_${PN} += "/usr/lib/cli/plugins/ /usr/lib/snmp/plugins/ \
               /usr/share/opsplugins"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "ops-intfd.service"

inherit openswitch cmake systemd
