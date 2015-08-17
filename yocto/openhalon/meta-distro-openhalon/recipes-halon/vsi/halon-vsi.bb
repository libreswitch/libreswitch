SUMMARY = "Mininet based Python Library for component & feature tests."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://setup.py;beginline=0;endline=14;md5=7b67a88d8ffaceb13faa66ec76114ed6"

SRC_URI = "git://git.openhalon.io/openhalon/vsi;protocol=https"

SRCREV = "${AUTOREV}"

# When using AUTOREV, we need to force the package version
# to the revision of git in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

inherit setuptools

RDEPENDS_${PN} = "mininet python-pytest"
DEPENDS_class-native = "mininet-native python-pytest-native util-linux-native"

BBCLASSEXTEND = "native"
