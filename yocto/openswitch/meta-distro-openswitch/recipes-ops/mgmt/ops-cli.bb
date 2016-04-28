SUMMARY = "OpenSwitch CLI"
LICENSE = "GPL-2.0 & LGPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=81bcece21748c91ba9992349a91ec11d\
                    file://COPYING.LIB;md5=01ef24401ded36cd8e5d18bfe947240c"

DEPENDS = "ops-utils ops-ovsdb"

BRANCH ?= "${OPS_REPO_BRANCH}"

SRC_URI = "${OPS_REPO_BASE_URL}/ops-cli;protocol=${OPS_REPO_PROTOCOL};branch=${BRANCH} \
"

SRCREV = "eaf5cbb56480e398f32669dafe2c267054a268e6"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

inherit openswitch pkgconfig cmake
