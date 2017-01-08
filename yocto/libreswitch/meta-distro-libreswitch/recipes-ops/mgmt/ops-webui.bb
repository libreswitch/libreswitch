SUMMARY = "LibreSwitch WebUI"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

BRANCH ?= "${LBS_REPO_BRANCH}"

SRC_URI = "${LBS_REPO_BASE_URL}/webui;protocol=${LBS_REPO_PROTOCOL};branch=${BRANCH} \
"

SRCREV = "50881232a97a3353ffee0a32c56769f163bf48d9"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"
B = "${S}"

inherit npm

# Override the NPM dependency on node, it is only used compile the resources
RDEPENDS_${PN} = ""

do_extract_vendored_dependencies() {
    ./tools/scripts/extract-node-tars
}

do_patch_append() {
    bb.build.exec_func('do_extract_vendored_dependencies', d)
}

do_compile() {
    # When using devenv_import the patch step may have been skipped
    # but we want to preserve the extraction as part of the fetch process
    # for developers using devenv_add
    if ! [ -d node_modules ] ; then
        ./tools/scripts/extract-node-tars
    fi
    oe_runnpm run buildprod
    oe_runnpm run errorpages
}

do_install() {
    install -d ${D}/srv/www/static/errors
    cp -R build/* ${D}/srv/www/static
    cp errors/build/* ${D}/srv/www/static/errors/
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
