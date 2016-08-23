SUMMARY = "OpenSwitch"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

BRANCH ?= "feature/tacacs_plus"

SRC_URI = "${OPS_REPO_BASE_URL}/ops;protocol=${OPS_REPO_PROTOCOL};branch=${BRANCH}"

SRCREV = "${AUTOREV}"

DEPENDS = " \
    python-jsonref-native \
    python-jsonschema-native \
    python-plantuml-native \
    python-pycksum-native \
    python-sphinx-native \
    python-sphinx-rtd-theme-native \
"
>>>>>>> origin/master

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
"

OPS_SCHEMA_PATH="${S}/schema"

inherit openswitch cmake pythonnative
