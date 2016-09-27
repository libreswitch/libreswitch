SUMMARY = "OpenSwitch"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

BRANCH ?= "${OPS_REPO_BRANCH}"

SRC_URI = "${OPS_REPO_BASE_URL}/ops;protocol=${OPS_REPO_PROTOCOL};branch=${BRANCH} \
	file://ops-schemadoc.nginx \
"

SRCREV = "252dfa00f6d66566279a3538a880ffaf1ae67e08"

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

OPS_SCHEMA_PATH="${S}/schema"

do_install_prepend() {
    make doc
}

do_install_append() {
    install -d ${D}/etc/nginx/conf.d
    install -m 0644 ${WORKDIR}/ops-schemadoc.nginx ${D}/etc/nginx/conf.d/backend-schemadoc.conf
}

inherit openswitch cmake pythonnative
