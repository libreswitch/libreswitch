DESCRIPTION = "NetBSD Editline library"
HOMEPAGE = "http://thrysoee.dk/editline/"
SECTION = "libs"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=1e4228d0c5a9093b01aeaaeae6641533"

DEPENDS = "ncurses"

PR = "r1"
S = "${WORKDIR}/libedit-20130712-3.1"

SRC_URI = "http://thrysoee.dk/editline/libedit-20130712-3.1.tar.gz"
SRC_URI[md5sum] = "0891336c697362727a1fa7e60c5cb96c"
SRC_URI[sha256sum] = "5d9b1a9dd66f1fe28bbd98e4d8ed1a22d8da0d08d902407dcc4a0702c8d88a37"

inherit autotools

BBCLASSEXTEND = "native nativesdk"
