SUMMARY = "OpenSwitch WebUI"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

BRANCH ?= "${OPS_REPO_BRANCH}"

SRC_URI = "${OPS_REPO_BASE_URL}/ops-webui;protocol=${OPS_REPO_PROTOCOL};branch=${BRANCH} \
"

SRCREV = "da109872542adcee178017ffdccde2f9cd356263"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"
B = "${S}"

inherit npm

# Put it after the inherit NPM to override the dependency on node
RDEPENDS_${PN} = "ops-restd"

do_extract_vendored_dependencies() {
    ./tools/scripts/extract-node-tars
}

do_patch_append() {
    bb.build.exec_func('do_extract_vendored_dependencies', d)
}

do_compile() {
    oe_runnpm run buildprod
}

do_install() {
    install -d ${D}/srv/www/static
    cp -R build/* ${D}/srv/www/static
}

do_unittest() {
    oe_runnpm run testcover
}

addtask unittest after do_patch
do_unittest() {
    oe_runnpm run testcover
}
do_unittest[nostamp] = "1"

FILES_${PN} = "/srv/www/static/*"
