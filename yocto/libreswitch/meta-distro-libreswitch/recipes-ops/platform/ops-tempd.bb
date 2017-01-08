SUMMARY = "LibreSwitch Temperature Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-hw-config ops-ovsdb ops-cli ops-supportability"

BRANCH ?= "${LBS_REPO_BRANCH}"

SRC_URI = "${LBS_REPO_BASE_URL}/tempd;protocol=${LBS_REPO_PROTOCOL};branch=${BRANCH} \
           file://ops-tempd.service \
"

SRCREV = "b63592dfaaca819b0f879f6765563fc96d2e1629"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

do_install_append() {
     install -d ${D}${systemd_unitdir}/system
     install -m 0644 ${WORKDIR}/ops-tempd.service ${D}${systemd_unitdir}/system/
}

FILES_${PN} += "/usr/lib/cli/plugins/"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "ops-tempd.service"

inherit libreswitch cmake systemd
