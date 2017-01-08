SUMMARY = "LibreSwitch REST API rendering"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

BRANCH ?= "${LBS_REPO_BRANCH}"

SRC_URI = "${LBS_REPO_BASE_URL}/restapi;protocol=${LBS_REPO_PROTOCOL};branch=${BRANCH} \
    file://ops-restapi.nginx \
"

SRCREV = "e1763eced3c53f7855de8c2ae9770a091c3d9e1c"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"
B = "${S}"

FILES_${PN} += "/srv/www/api /etc/nginx/conf.d"

do_install_append () {
    # Install Swagger-UI files with modification to point to LibreSwitch
    # REST API file
    install -d ${D}/srv/www/api
    cp -R ${S}/src/* ${D}/srv/www/api

    install -d ${D}/etc/nginx/conf.d
    install -m 0644 ${WORKDIR}/ops-restapi.nginx ${D}/etc/nginx/conf.d/backend-restapi.conf
}
