SUMMARY = "Platform Configuration files for OpenSwitch"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "yaml-cpp gtest i2c-tools"

SRC_URI = "git://git.openswitch.net/openswitch/ops-hw-config;protocol=http;branch=rel/dill \
"

SRCREV = "c356da59abe2311a5d03be0c6420b18f015e0762"

PLATFORM_PATH?="${MACHINE}"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

do_install_append () {
    install -d ${D}${sysconfdir}/openswitch/platform/${PLATFORM_PATH}
    for f in ${S}/${PLATFORM_PATH}/*.yaml ; do
        d=`dirname "$f"`
        n=`basename "$f"`
        # If there's a flavor override, use that
        if test -n "${PLATFORM_FLAVOR}" -a -e "${d}/${PLATFORM_FLAVOR}/${n}" ; then
            cp -p "${d}/${PLATFORM_FLAVOR}/${n}" "${D}${sysconfdir}/openswitch/platform/${PLATFORM_PATH}"
        else
            cp -p "${d}/${n}" "${D}${sysconfdir}/openswitch/platform/${PLATFORM_PATH}"
        fi
    done
}

FILES_${PN} += "${sysconfdir}"

inherit openswitch cmake
