SUMMARY = "OpenSwitch RBAC Utilities"
LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGES += "ops-librbac ops-librbac-dev"
FILES_ops-librbac = "/usr/lib/librbac.so.*.*.*"
FILES_ops-librbac-dev = "/usr/lib/pkgconfig/rbac.pc /usr/lib/librbac.so*"

SSTATE_DUPWHITELIST = \
     "${STAGING_DIR_TARGET}/usr/include/rbac.h \
      ${STAGING_DIR_TARGET}/usr/lib/librbac.so.0.1.0 \
      ${STAGING_DIR_TARGET}/usr/lib/librbac.so \
      ${STAGING_DIR_TARGET}/usr/lib/python2.7/site-packages/rbac.py \
      ${STAGING_DIR_TARGET}/usr/lib/python2.7/site-packages/rbac.pyc"

DEPENDS = ""

BRANCH ?= "${OPS_REPO_BRANCH}"

SRC_URI = "${OPS_REPO_BASE_URL}/ops-rbac;protocol=${OPS_REPO_PROTOCOL};branch=${BRANCH}"

SRCREV = "e9f109f484895fcaea7ccdda11b07d5133d62625"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

# Mixing of two classes, the build happens on the source directory.
inherit openswitch cmake setuptools

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
