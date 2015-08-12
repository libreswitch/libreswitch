SUMMARY = "FreeRADIUS Client is a framework and library for writing RADIUS Clients"
HOMEPAGE = "http://wiki.freeradius.org/project/Radiusclient"
LICENSE = "BSD"

LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=880db8dd9d6adcc3f06a24194f55a40d"

SRC_URI = "ftp://ftp.freeradius.org/pub/freeradius/${PN}-${PV}.tar.gz \
"

SRC_URI[md5sum] = "43b4d21715b613dc4fe8ef128467fe78"
SRC_URI[sha256sum] = "eada2861b8f4928e3ac6b5bbfe11e92cd6cdcacfce40cae1085e77c1b6add0e9"

CACHED_CONFIGUREVARS = "ac_cv_func_uname=no"
EXTRA_OECONF += "--disable-static"

inherit autotools pkgconfig
