SUMMARY = "Password Server Daemon"
LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-ovsdb"

SRC_URI = "git://git.openswitch.net/openswitch/ops-passwd-srv;protocol=http;branch=rel/dill \
           file://ops-passwd-srv.service \
         "

SRCREV="d882cd77a045d76c49abc561c436a6c338a32a1d"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

do_install_prepend() {
     install -d ${D}${systemd_unitdir}/system
     install -m 0644 ${WORKDIR}/ops-passwd-srv.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "ops-passwd-srv.service"

inherit openswitch systemd cmake
