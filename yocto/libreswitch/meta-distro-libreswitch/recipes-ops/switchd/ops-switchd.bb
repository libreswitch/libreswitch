SUMMARY = "LibreSwitch Switch Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops ops-openvswitch ops-ovsdb ops-utils libyaml jemalloc ops-cli"

BRANCH ?= "${LBS_REPO_BRANCH}"

SRC_URI = "${LBS_REPO_BASE_URL}/switchd;protocol=${LBS_REPO_PROTOCOL};branch=${BRANCH} \
   file://switchd_bcm.service \
   file://switchd_sim.service \
   file://switchd_p4sim.service \
   file://switchd_xpliant.service \
   file://switchd_sai.service \
"

SRCREV = "d345f87cd17ced3b97a73ace23c387de5db62842"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

RPROVIDES_${PN} = "virtual/switchd"

RDEPENDS_${PN} = "openssl procps util-linux-uuidgen util-linux-libuuid coreutils \
  python perl perl-module-strict sed gawk grep \
  ops-openvswitch ops-ovsdb virtual/ops-switchd-switch-api-plugin ops-reboot\
"

FILES_${PN} = "${sbindir}/ops-switchd ${libdir}/libswitchd_plugins.so.1* ${libdir}/openvswitch/plugins/"
FILES_${PN} += "/usr/lib/cli/plugins/"

do_install_append() {
   install -d ${D}${systemd_unitdir}/system
   if ${@bb.utils.contains('MACHINE_FEATURES','broadcom','true','false',d)}; then
      install -m 0644 ${WORKDIR}/switchd_bcm.service ${D}${systemd_unitdir}/system/switchd.service
   elif ${@bb.utils.contains('MACHINE_FEATURES','xpliant','true','false',d)}; then
      install -m 0644 ${WORKDIR}/switchd_xpliant.service ${D}${systemd_unitdir}/system/switchd.service
   elif ${@bb.utils.contains('IMAGE_FEATURES','ops-p4','true','false',d)}; then
      install -m 0644 ${WORKDIR}/switchd_p4sim.service ${D}${systemd_unitdir}/system/switchd.service
   elif ${@bb.utils.contains('MACHINE_FEATURES','ops-container','true','false',d)}; then
      install -m 0644 ${WORKDIR}/switchd_sim.service ${D}${systemd_unitdir}/system/switchd.service
   elif ${@bb.utils.contains('MACHINE_FEATURES','ops-sai','true','false',d)}; then
      install -m 0644 ${WORKDIR}/switchd_sai.service ${D}${systemd_unitdir}/system/switchd.service
   fi

   install -d ${D}/usr/share/opsplugins
   for plugin in $(find ${S}/opsplugins -name "*.py"); do \
      install -m 0644 ${plugin} ${D}/usr/share/opsplugins
   done
}
FILES_${PN} += "/usr/share/opsplugins"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "switchd.service"

inherit libreswitch cmake systemd
export OVS_INCLUDE="${STAGING_DIR_TARGET}/usr/include/ovs"
