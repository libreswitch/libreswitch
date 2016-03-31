SUMMARY = "OpenSwitch WebUI"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "git://git.openswitch.net/openswitch/ops-webui;protocol=http \
"

SRCREV = "a61398b2d145d301398149ddb4c67eac072cba71"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"
B = "${S}"

inherit npm

# Put it after the inherit NPM to override the dependency on node
RDEPENDS_${PN} = "ops-restd"

do_compile() {
    ./tools/scripts/extract-node-tars
    oe_runnpm run testcover
    oe_runnpm run buildprod
}

do_install() {
    install -d ${D}/srv/www/static
    cp -Rp build/* ${D}/srv/www/static
}

FILES_${PN} = "/srv/www/static/*"
