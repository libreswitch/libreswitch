SUMMARY = "Platform Configuration files for LibreSwitch"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "yaml-cpp gtest i2c-tools"

BRANCH ?= "${LBS_REPO_BRANCH}"

SRC_URI = "${LBS_REPO_BASE_URL}/hw-config;protocol=${LBS_REPO_PROTOCOL};branch=${BRANCH} \
           file://0001-Correct-i2c-bus-address.patch \
           "

SRCREV = "4bc063c34fd78b662ee805ac48152abd0953a405"

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
            cp "${d}/${PLATFORM_FLAVOR}/${n}" "${D}${sysconfdir}/openswitch/platform/${PLATFORM_PATH}"
        else
            cp "${d}/${n}" "${D}${sysconfdir}/openswitch/platform/${PLATFORM_PATH}"
        fi
    done
}

FILES_${PN} += "${sysconfdir}"

inherit libreswitch cmake
