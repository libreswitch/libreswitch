SUMMARY = "Platform Configuration files for OpenSwitch"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "git://git.openswitch.net/openswitch/ops-hw-config;protocol=http \
"

SRCREV="${AUTOREV}"

PLATFORM_PATH?="${MACHINE}"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

do_install () {
    install -d ${D}${sysconfdir}/openswitch/platform/${PLATFORM_PATH}
    cp -p ${S}/${PLATFORM_PATH}/*.yaml ${D}${sysconfdir}/openswitch/platform/${PLATFORM_PATH}
}

FILES_${PN} = "${sysconfdir}"

inherit openswitch
