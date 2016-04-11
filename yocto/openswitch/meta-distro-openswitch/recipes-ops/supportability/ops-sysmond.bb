SUMMARY = "OpenSwitch System Monitoring Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = ""

SRC_URI = "git://git.openswitch.net/openswitch/ops-sysmond;protocol=http "

SRCREV="eea44ff282c207bbac030db895e17b396a34fa95"

DEPENDS = ""

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

# This directory is temporary and is just used to validate
# if the sysmond recipe was picked up. It would change to a model of installing
# a service once we have a NTP Daemon service working
DIR_${PN} = "/usr/share/sysmond"

FILES_${PN} = "${DIR_${PN}}"

do_install() {
      install -d ${D}"${DIR_${PN}}"
}

inherit openswitch
