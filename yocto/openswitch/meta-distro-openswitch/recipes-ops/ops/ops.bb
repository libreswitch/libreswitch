SUMMARY = "OpenSwitch"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

BRANCH ?= "${OPS_REPO_BRANCH}"

SRC_URI = "${OPS_REPO_BASE_URL}/ops;protocol=${OPS_REPO_PROTOCOL};branch=${BRANCH}"

SRCREV = "53a168fd72a7e691cd8b3b3428574f294551ef59"

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
"

OPS_SCHEMA_PATH="${S}/schema"

inherit openswitch cmake pythonnative
