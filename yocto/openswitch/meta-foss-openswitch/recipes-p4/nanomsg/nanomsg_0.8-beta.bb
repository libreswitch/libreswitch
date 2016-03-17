SUMMARY = "Socket library that provides several common communication patterns"
DESCRIPTION = "\
nanomsg is a socket library that provides several common communication \
patterns. It aims to make the networking layer fast, scalable, and easy \
to use. Implemented in C, it works on a wide range of operating systems \
with no further dependencies. \
"
HOMEPAGE = "http://nanomsg.org/"
SECTION = "libs"
LICENSE = "MIT"

LIC_FILES_CHKSUM = "file://COPYING;md5=587b3fd7fd291e418ff4d2b8f3904755"

SRC_URI = "https://github.com/${BPN}/${BPN}/releases/download/${PV}/${BPN}-${PV}.tar.gz \
"

SRC_URI[md5sum] = "bfccbcd8c5ea8ccad4ef7ade558eebea"
SRC_URI[sha256sum] = "75ce0c68a50cc68070d899035d5bb1e2bd75a5e01cbdd86ba8af62a84df3a947"

inherit autotools

DEPENDS = " \
	libtool-cross \
"

LIBTOOL = "${B}/${HOST_SYS}-libtool"
EXTRA_OEMAKE = "'LIBTOOL=${LIBTOOL}'"

DEPENDS_class-native = "libtool-native"
EXTRA_OEMAKE_class-native = "LIBTOOL=${BUILD_SYS}-libtool"

BBCLASSEXTEND = "native"
