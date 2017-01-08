SUMMARY = "LibreSwitch System Daemon (sysd)"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-utils ops-hw-config ops-ovsdb ops-cli ops-supportability"
RDEPENDS_${PN} = "dmidecode"

BRANCH ?= "${LBS_REPO_BRANCH}"

SRC_URI = "${LBS_REPO_BASE_URL}/sysd;protocol=${LBS_REPO_PROTOCOL};branch=${BRANCH} \
           file://ops-sysd.service \
"

SRCREV = "4e4cb61a9f21c40fc1bf977e4a445fbf2b1bced9"

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

EXTRA_OECMAKE+="${@bb.utils.contains('MACHINE_FEATURES', 'ops-container', '-DUSE_SW_FRU=ON', '',d)}"

inherit libreswitch cmake systemd pkgconfig
