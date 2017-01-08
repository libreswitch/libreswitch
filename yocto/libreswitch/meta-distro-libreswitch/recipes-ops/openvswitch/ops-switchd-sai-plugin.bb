# Copyright Mellanox Technologies, Ltd. 2001-2016.
# This software product is licensed under Apache version 2, as detailed in
# the COPYING file.

SUMMARY = "LibreSwitch SAI plugin"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit libreswitch cmake

DEPENDS = "ops-ovsdb virtual/sai ops-hw-config"
RDEPENDS_${PN} = "virtual/sai"

PROVIDES += "virtual/ops-switchd-switch-api-plugin"
RPROVIDES_${PN} += "virtual/ops-switchd-switch-api-plugin"

SAI_PLUGIN_BASE_URL ??= "${LBS_REPO_BASE_URL}"
SAI_PLUGIN_PROTOCOL ??= "${LBS_REPO_PROTOCOL}"
SAI_PLUGIN_BRANCH ??= "${LBS_REPO_BRANCH}"

SRC_URI = " \
   ${SAI_PLUGIN_BASE_URL}/switchd-sai-plugin;protocol=${SAI_PLUGIN_PROTOCOL};branch=${SAI_PLUGIN_BRANCH} \
"
FILES_${PN} = "${libdir}/openvswitch/plugins"

SRCREV = "${AUTOREV}"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"
