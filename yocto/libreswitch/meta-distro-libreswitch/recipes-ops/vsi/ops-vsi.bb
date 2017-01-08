SUMMARY = "Mininet based Python Library for component & feature tests."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

BRANCH ?= "${LBS_REPO_BRANCH}"

SRC_URI = "${LBS_REPO_BASE_URL}/vsi;protocol=${LBS_REPO_PROTOCOL};branch=${BRANCH}"

SRCREV = "${AUTOREV}"

# When using AUTOREV, we need to force the package version
# to the revision of git in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

inherit setuptools

RDEPENDS_${PN} = "mininet python-pytest bash"
DEPENDS_class-native = "mininet-native python-pytest-native python-pytest-timeout-native \
                        python-pyyaml-native util-linux-native python-smartpm-native"

do_install_append () {
    cp ${S}/opsvsiutils/restutils/server.crt ${D}/${PYTHON_SITEPACKAGES_DIR}/opsvsiutils/restutils/
}

BBCLASSEXTEND = "native"
