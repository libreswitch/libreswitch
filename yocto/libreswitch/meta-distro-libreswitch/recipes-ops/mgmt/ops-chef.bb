SUMMARY = "LibreSwitch Chef client/agent recipe"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

RDEPENDS_${PN} = "ruby"

BRANCH ?= "${LBS_REPO_BRANCH}"

SRC_URI = "${LBS_REPO_BASE_URL}/chef;protocol=${LBS_REPO_PROTOCOL};branch=${BRANCH}"
SRCREV = "96053a098716220fb4d484bc321b84dc2087cbd8"
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

DIR_${PN} = "/opt/chef"
FILES_${PN} = "${DIR_${PN}}"

do_install() {
    install -d ${D}"${DIR_${PN}}"
}

inherit libreswitch
