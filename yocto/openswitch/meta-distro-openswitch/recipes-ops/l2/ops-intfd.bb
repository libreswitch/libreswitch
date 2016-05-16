SUMMARY = "OpenSwitch Interface Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-utils ops-hw-config ops-ovsdb ops-cli ops-supportability ops-snmpd"

SRC_URI = "git://git.openswitch.net/openswitch/ops-intfd;protocol=http;branch=rel/dill \
           file://ops-intfd.service \
           "

SRCREV = "5995e7d011be96d5cbe4f0c0ec9d2edffbb12d76"

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
