SUMMARY = "OpenSwitch Ansible modules/playbooks"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

RDEPENDS_${PN} = "python-syslog python-unixadmin python-compiler"

BRANCH ?= "${OPS_REPO_BRANCH}"

SRC_URI = "${OPS_REPO_BASE_URL}/ops-ansible;protocol=${OPS_REPO_PROTOCOL};branch=${BRANCH}"

SRCREV = "fce72acb974340d0b4a907026ce234e990e1dac9"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

DIR_${PN} = "/usr/share/ansible"
FILES_${PN} = "${DIR_${PN}}"

do_install() {
    install -d ${D}"${DIR_${PN}}"
}

inherit openswitch
