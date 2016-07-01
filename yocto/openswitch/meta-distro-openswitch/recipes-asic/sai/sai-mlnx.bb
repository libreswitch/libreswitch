# Copyright Mellanox Technologies, Ltd. 2001-2016.

SUMMARY = "SAI API"
LICENSE = "Apache-2.0"
DESCRIPTION = "Mellanox SAI"

inherit autotools pkgconfig openswitch

PROVIDES += "virtual/sai"
RPROVIDES_${PN} += "virtual/sai"

DEPENDS += "libxml2 mlnx-applibs mlnx-sxd-libs mlnx-sx-complib mlnx-sx-gen-utils"
RDEPENDS_${PN} += "mlnx-hw-mgmt mlnx-sx-examples"

PREFIX = "${STAGING_DIR_TARGET}/${prefix}"

EXTRA_OECONF='--with-applibs=${PREFIX} --with-sxcomplib=${PREFIX} --with-sxdlibs=${PREFIX} --with-xml2=${PREFIX}'

LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10; \
"

S = "${WORKDIR}/git/mlnx_sai"
B = "${S}"

SRCREV = "0132a7172291af350f35946aeaee6911ff2710fa"

SRC_URI = " \
   git://github.com/Mellanox/SAI-Implementation.git;protocol=https;branch=openswitch \
"

FILES_${PN} += "${prefix}/share"

ERROR_QA_remove = "compile-host-path"
WARN_QA_append = "compile-host-path"
