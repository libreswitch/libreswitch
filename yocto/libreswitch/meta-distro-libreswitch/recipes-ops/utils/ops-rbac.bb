SUMMARY = "LibreSwitch RBAC Utilities"
LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGES += "ops-librbac ops-librbac-dev"
FILES_ops-librbac = "/usr/lib/librbac.so.*.*.*"
FILES_ops-librbac-dev = "/usr/lib/pkgconfig/rbac.pc /usr/lib/librbac.so*"

DEPENDS = "ops-ovsdb"

BRANCH ?= "${LBS_REPO_BRANCH}"

SRC_URI = "${LBS_REPO_BASE_URL}/rbac;protocol=${LBS_REPO_PROTOCOL};branch=${BRANCH}"

SRCREV = "0b4c64bd959fabcf4e9437e03d0074a5e1ed93a3"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

# Mixing of two classes, the build happens on the source directory.
inherit libreswitch cmake setuptools

# Doing some magic here. We are inheriting cmake and setuptools classes, so we
# need to override the exported functions and call both by ourselves.
do_compile() {
     cd ${S}
     distutils_do_compile
     # Cmake compile changes to the B directory
     cmake_do_compile
}

do_install() {
     cd ${S}
     distutils_do_install
     # Cmake compile changes to the B directory
     cmake_do_install
}
