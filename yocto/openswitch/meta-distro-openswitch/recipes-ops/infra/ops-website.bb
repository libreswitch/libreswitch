SUMMARY = "OpenSwitch WebSite"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "git://git.openswitch.net/infra/website;protocol=http \
"

SRCREV="${AUTOREV}"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"
B = "${S}"

inherit npm

# This code nevers gets deployed to a target, so we can skip our runtime deps
RDEPENDS_${PN}_remove = "node"

do_compile() {
    cd ${S}
    oe_runnpm_native install     # Installs dependencies defined in package.json
    oe_runnpm_native install -g grommet
    oe_runnpm_native install -g gulp
}
