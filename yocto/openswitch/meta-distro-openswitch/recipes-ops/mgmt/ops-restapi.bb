SUMMARY = "OpenSwitch REST API rendering"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "git://git.openswitch.net/openswitch/ops-restapi;protocol=http \
"

SRCREV="${AUTOREV}"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"
B = "${S}"

DEPENDS = "ops-ovsdb ops-openvswitch ops-restd"

FILES_${PN} += "/srv/www/api"

do_install_append() {
    # Install Swagger-UI files and the generated REST API file
    # Since Python files from ops-restd is not yet staged at this time,
    # we rely on ops-restd to generate the REST API file and just use
    # it here.
    install -d ${D}/srv/www/api
    cp -R ${S}/src/* ${D}/srv/www/api
    cp ${STAGING_DIR_TARGET}/srv/www/api/ops-restapi.json ${D}/srv/www/api
}
