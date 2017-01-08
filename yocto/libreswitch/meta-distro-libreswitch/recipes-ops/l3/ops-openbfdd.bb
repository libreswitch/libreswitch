SUMMARY = "An open source implementation of the BFD protocol"
LICENSE = "BSD"
HOMEPAGE = "https://github.com/dyninc/OpenBFDD"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-utils ops-ovsdb ops-cli"

BRANCH ?= "${LBS_REPO_BRANCH}"

SRC_URI = "${LBS_REPO_BASE_URL}/openbfdd;protocol=${LBS_REPO_PROTOCOL};branch=${BRANCH} \
	file://bfdd-beacon.service \
"
SRCREV = "8c68a74f197a93fe05e14d264b2c9ca1ad1ea7ec"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

FILES_${PN} += "/usr/share/opsplugins /usr/lib/cli/plugins/"
do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/bfdd-beacon.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "bfdd-beacon.service"

inherit libreswitch autotools pkgconfig systemd
export OVS_INCLUDE="${STAGING_DIR_TARGET}/usr/include/ovs"
