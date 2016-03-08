SUMMARY = "OpenSwitch System Daemon (sysd)"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-utils ops-config-yaml ops-ovsdb ops-cli"
RDEPENDS_${PN} = "dmidecode"

SRC_URI = "git://git.openswitch.net/openswitch/ops-sysd;protocol=https \
           file://ops-sysd.service \
"

SRCREV = "f1927e0b7349f163ba0e0a89a527f51ba3c3cee3"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/ops-sysd.service ${D}${systemd_unitdir}/system
}

FILES_${PN} += "/usr/lib/cli/plugins/"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "ops-sysd.service"

inherit openswitch cmake systemd pkgconfig
