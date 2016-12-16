SUMMARY = "Userspace helper for Linux kernel EDAC drivers"
HOMEPAGE = "https://github.com/grondo/edac-utils"
SECTION = "Applications/System"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

DEPENDS = " sysfsutils"

SRCREV = "556ebce6e1a5a8ad8c07090979a36be7a2276e2e"
PV = "0.19x+git${SRCPV}"

S = "${WORKDIR}/git"

SRC_URI = "git://github.com/grondo/edac-utils \
    file://make-init-script-be-able-to-automatically-load-EDAC-.patch \
    file://add-restart-to-initscript.patch \
"

inherit autotools-brokensep

do_configure_prepend () {
    touch ${S}/ChangeLog
    ${S}/bootstrap
}

RDEPENDS_${PN}_x86 = "dmidecode"
RDEPENDS_${PN}_x86-64 = "dmidecode"
RDEPENDS_${PN}_arm = "dmidecode"
RDEPENDS_${PN}_aarch64 = "dmidecode"
RDEPENDS_${PN}_powerpc = "dmidecode"
RDEPENDS_${PN}_powerpc64 = "dmidecode"
RDEPENDS_${PN}_append = " \
    perl-module-file-basename perl-module-file-find perl-module-getopt-long perl-module-posix perl-module-file-glob \
"
