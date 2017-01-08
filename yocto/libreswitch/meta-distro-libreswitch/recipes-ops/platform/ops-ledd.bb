SUMMARY = "LibreSwitch LED Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-hw-config ops-ovsdb ops-cli ops-supportability"

BRANCH ?= "${LBS_REPO_BRANCH}"

SRC_URI = "${LBS_REPO_BASE_URL}/ledd;protocol=${LBS_REPO_PROTOCOL};branch=${BRANCH} \
           file://ops-ledd.service \
"

SRCREV = "c956dc07557911114a1ec528539e0b2b1e2276d6"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

do_install_append() {
     install -d ${D}${systemd_unitdir}/system
     install -m 0644 ${WORKDIR}/ops-ledd.service ${D}${systemd_unitdir}/system/
}

FILES_${PN} += "/usr/lib/cli/plugins/"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "ops-ledd.service"

inherit libreswitch cmake systemd
