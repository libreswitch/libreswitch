SUMMARY = "LibreSwitch"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

BRANCH ?= "${LBS_REPO_BRANCH}"

SRC_URI = "${LBS_REPO_BASE_URL}/ops;protocol=${LBS_REPO_PROTOCOL};branch=${BRANCH} \
	file://ops-schemadoc.nginx \
"

SRCREV = "009afc8850e16f9686b5fe4bf39cce1038774f42"

DEPENDS = " \
    python-jsonref-native \
    python-jsonschema-native \
    python-plantuml-native \
    python-pycksum-native \
    python-sphinx-native \
    python-sphinx-rtd-theme-native \
"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

FILES_${PN} += " \
    /usr/share/openvswitch/ \
    /usr/share/openvswitch/*.extschema \
    /usr/share/openvswitch/*.ovsschema \
    /usr/share/openvswitch/*.xml \
    /usr/include/*_empty_values.h \
    /srv/www/schemadoc \
"

LBS_SCHEMA_PATH="${S}/schema"

do_install_prepend() {
    make doc
}

do_install_append() {
    install -d ${D}/etc/nginx/conf.d
    install -m 0644 ${WORKDIR}/ops-schemadoc.nginx ${D}/etc/nginx/conf.d/backend-schemadoc.conf
}

inherit libreswitch cmake pythonnative
