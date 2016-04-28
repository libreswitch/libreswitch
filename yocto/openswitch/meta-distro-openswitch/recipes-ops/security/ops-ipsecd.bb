SUMMARY = "IPsec Configuration Daemon"
LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-ovsdb doxygen-native gtest gmock strongswan libmnl boost"

BRANCH ?= "${OPS_REPO_BRANCH}"

SRC_URI = "${OPS_REPO_BASE_URL}/ops-ipsecd;protocol=${OPS_REPO_PROTOCOL};branch=${BRANCH} \
           file://ops-ipsecd.service \
           "

SRCREV="f7b5235faf5f1f7bdec036229aab47b6e3fb616a"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "${BPN}.service"

do_install_append() {
     install -d ${D}${systemd_unitdir}/system
     install -m 0644 ${WORKDIR}/ops-ipsecd.service ${D}${systemd_unitdir}/system/
}

do_unit_test_append() {
     do_gtest_harness tests/ops-ipsecd-ut
}

inherit openswitch systemd cmake
