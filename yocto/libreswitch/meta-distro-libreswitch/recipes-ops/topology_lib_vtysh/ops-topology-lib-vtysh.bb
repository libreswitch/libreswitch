SUMMARY = "Python Library for component, feature and system level tests."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

BRANCH ?= "${LBS_REPO_BRANCH}"

SRC_URI = "${LBS_REPO_BASE_URL}/topology-lib-vtysh;protocol=${LBS_REPO_PROTOCOL};branch=${BRANCH}"

SRCREV = "${AUTOREV}"

# When using AUTOREV, we need to force the package version
# to the revision of git in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

RDEPENDS_${PN} = "python-pytest"
DEPENDS_class-native = "python-pytest-native"

BBCLASSEXTEND = "native"
