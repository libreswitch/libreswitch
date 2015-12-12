SUMMARY = "OpenSwitch Network Time Protocol Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = ""

SRC_URI = "git://git.openswitch.net/openswitch/ops-ntpd;protocol=http"

SRCREV = "51cb02a097fb991d443ea05c14dc3c353d471a70"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

# This directory is temporary and is just used to validate
# if the ntpd recipe was picked up. It would change to a model of installing
# a service once we have a NTP Daemon service working
DIR_${PN} = "/usr/share/ntpd"

FILES_${PN} = "${DIR_${PN}}"

do_install() {
    install -d ${D}"${DIR_${PN}}"
}

inherit openswitch
