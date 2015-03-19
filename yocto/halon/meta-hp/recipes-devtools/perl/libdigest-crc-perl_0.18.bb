SUMMARY = "Perl module for Generic CRC functions"
DESCRIPTION = "The Digest::CRC module calculates CRC sums of all sorts. It \
contains wrapper functions with the correct parameters for CRC-CCITT, CRC-16, \
CRC-32 and CRC-64, as well as the CRC used in OpenPGP's ASCII-armored checksum."

SECTION = "libs"
LICENSE = "PD"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/PD;md5=b3597d12946881e13cb3b548d1173851"

DEPENDS += "perl"

SRC_URI = "http://www.cpan.org/authors/id/O/OL/OLIMAUL/Digest-CRC-${PV}.tar.gz"

SRC_URI[md5sum] = "baf8cb6c50782416b10f470b2e29bf7a"
SRC_URI[sha256sum] = "5c5329f37c46eb79835169508583da8767d9839350b69bb2b48ac6f594f70374"

S = "${WORKDIR}/Digest-CRC-${PV}"

EXTRA_CPANFLAGS = "EXPATLIBPATH=${STAGING_LIBDIR} EXPATINCPATH=${STAGING_INCDIR}"

inherit cpan

do_compile() {
	export LIBC="$(find ${STAGING_DIR_TARGET}/${base_libdir}/ -name 'libc-*.so')"
	cpan_do_compile
}

BBCLASSEXTEND = "native"
