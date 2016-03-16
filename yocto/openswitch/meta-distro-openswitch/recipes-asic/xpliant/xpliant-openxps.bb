SUMMARY = "Cavium Open APIs to program XPliant Switch"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=e3fc50a88d0a364313df4b21ef20c29e"

DEPENDS = "libxml2 libpcap lmsensors"

DEPENDS_${PN} += "systemd"

SRC_URI = "git://github.com/xpliant/OpenXPS;protocol=http"

SRCREV = "a33a307f667ad0e81893d41d023b68a6fd2240c0"

# When using AUTOREV, we need to force the package version
# to the revision of git in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

# Avoid running make clean during configuration stage
CLEANBROKEN = "1"

do_install() {
    # Installing headers
    install -d ${D}${includedir}/openXps
    cp -Rp ${S}/include/* ${D}${includedir}/openXps

    # Installing library
    install -d ${D}${libdir}/pkgconfig
    install -m 0755 ${S}/lib/libOpenXps.so.0 ${D}${libdir}
    ln -s libOpenXps.so.0 ${D}${libdir}/libOpenXps.so
    install -m 0655 ${S}/lib/openXps.pc ${D}${libdir}/pkgconfig
}

INSANE_SKIP_${PN} += "already-stripped"

inherit openswitch
