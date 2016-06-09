SUMMARY = "Cavium Open APIs to program XPliant Switch"
LICENSE = "Proprietary & Apache-2.0 & GPLv2"
LIC_FILES_CHKSUM = "file://bin/serdes/OPS%20license%20file%20(2016-04-08)%20(Avago%20Rom).md;md5=ceb5984773b3b86852933a7745152e77 \
                    file://LICENSE.md;md5=e3fc50a88d0a364313df4b21ef20c29e \
                    file://xpnet/LICENSE-GPLv2.md;md5=9038119dce34e83b70c78c88bb0a3f23 \
                   "

DEPENDS = "libxml2 libpcap lmsensors"

RDEPENDS_${PN} = "libpcap python-doctest"

DEPENDS_${PN} += "systemd"

SRC_URI = "git://github.com/xpliant/OpenXPS;protocol=http"

SRCREV = "827ef00e79d2505c53fa7daabbb59a9ea3ef4998"

# When using AUTOREV, we need to force the package version
# to the revision of git in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"
EXTERNALSRC_BUILD??="${S}/output"

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
