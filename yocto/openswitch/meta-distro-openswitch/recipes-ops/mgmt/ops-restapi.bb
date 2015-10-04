SUMMARY = "OpenSwitch REST API rendering"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "git://git.openswitch.net/openswitch/ops-restapi;protocol=http \
"

SRCREV = "87da644cefe798fe29ceb428e9153d377a4d1836"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"
B = "${S}"

FILES_${PN} += "/srv/www/api"

do_install_append () {
    # Install Swagger-UI files with modification to point to OpenSwitch
    # REST API file
    install -d ${D}/srv/www/api
    cp -R ${S}/src/* ${D}/srv/www/api
}
