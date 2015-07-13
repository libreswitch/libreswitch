SUMMARY = "FreeRADIUS Client is a framework and library for writing RADIUS Clients"
HOMEPAGE = "http://wiki.freeradius.org/project/Radiusclient"
LICENSE = "BSD"

LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=3e47566c9271b786693d8a08792dbf41"

SRC_URI = "ftp://ftp.freeradius.org/pub/freeradius/${PN}-${PV}.tar.bz2 \
"

SRC_URI[md5sum] = "2e46564e450ae13aedb70dc133b158ac"
SRC_URI[sha256sum] = "a3c9522ed6d9bc795794595a8f3eebada868ea11a0c046637500faf257f9688f"

CACHED_CONFIGUREVARS = "ac_cv_func_uname=no"
EXTRA_OECONF += "--disable-static"

inherit autotools

