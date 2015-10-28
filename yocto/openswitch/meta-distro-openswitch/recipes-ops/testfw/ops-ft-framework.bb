SUMMARY = "Python Library for component, feature and system level tests."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "git://git.openswitch.net/openswitch/ops-ft-framework;protocol=https"

SRCREV = "${AUTOREV}"

# When using AUTOREV, we need to force the package version
# to the revision of git in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

inherit setuptools

RDEPENDS_${PN} = "ops-vsi python-pexpect python-paramiko python-ecdsa"
DEPENDS_class-native = "ops-vsi-native python-pexpect-native python-paramiko-native python-ecdsa-native"

BBCLASSEXTEND = "native"
