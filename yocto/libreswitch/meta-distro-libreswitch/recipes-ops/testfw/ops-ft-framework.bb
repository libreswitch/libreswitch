SUMMARY = "Python Library for component, feature and system level tests."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

BRANCH ?= "${LBS_REPO_BRANCH}"

SRC_URI = "${LBS_REPO_BASE_URL}/ft-framework;protocol=${LBS_REPO_PROTOCOL};branch=${BRANCH}"

SRCREV = "${AUTOREV}"

# When using AUTOREV, we need to force the package version
# to the revision of git in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

inherit setuptools

RDEPENDS_${PN} = "ops-vsi python-pexpect python-paramiko python-ecdsa"
DEPENDS_class-native = "ops-vsi-native python-pexpect-native python-paramiko-native python-ecdsa-native"

do_install_append () {
    cp ${S}/opstestfw/restEnv/server.crt ${D}/${PYTHON_SITEPACKAGES_DIR}/opstestfw/restEnv
    cp ${S}/opstestfw/restEnv/server-private.key ${D}/${PYTHON_SITEPACKAGES_DIR}/opstestfw/restEnv
}

BBCLASSEXTEND = "native"
