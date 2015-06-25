SUMMARY = "Switch CLI"
LICENSE = "GPL-2.0 & LGPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=81bcece21748c91ba9992349a91ec11d\
                    file://COPYING.LIB;md5=01ef24401ded36cd8e5d18bfe947240c"

DEPENDS = "halonutils halon-ovsdb"

SRC_URI = "git://git.openhalon.io/openhalon/cli;protocol=http \
"

SRCREV="${AUTOREV}"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

inherit halon autotools pkgconfig

EXTRA_OECONF = "--enable-user=root --enable-group=root \
 --enable-ovsdb --enable-vtysh\
"
