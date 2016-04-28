SUMMARY = "OpenSwitch RBAC Utilities"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = ""

SRC_URI = "git://git.openswitch.net/openswitch/ops-rbac;protocol=http;branch=rel/dill"

SRCREV = "9f551b08c5fb2c717cba50d79ce695b158f781af"

# When using AUTOREV, we need to force the package version to the revision of
# git in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

# This directory is temporary and is just used to validate
# if the rbac recipe was picked up.
DIR_${PN} = "/usr/share/rbac"

FILES_${PN} = "${DIR_${PN}}"

do_install() {
    install -d ${D}"${DIR_${PN}}"
}

inherit openswitch
