SUMMARY = "Configuration files for genericx86-64"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

RPROVIDES_${PN} = "virtual/ops-hw-config"

SRC_URI = "git://git.openhalon.io/openhalon/simulator-config;protocol=http \
"

SRCREV="${AUTOREV}"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

do_install () {
    install -d ${D}${sysconfdir}/openswitch/platform/Generic-x86/X86-64
    cp -p ${S}/*.yaml ${D}${sysconfdir}/openswitch/platform/Generic-x86/X86-64
}

FILES_${PN} = "${sysconfdir}"

inherit openswitch
