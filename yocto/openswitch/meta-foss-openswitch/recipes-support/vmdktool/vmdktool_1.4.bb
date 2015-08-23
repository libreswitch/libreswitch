SUMMARY = "VMDK file converter"
HOMEPAGE = "http://www.freshports.org/sysutils/vmdktool/"
LICENSE = "BSD-2-Clause"

LIC_FILES_CHKSUM = "file://vmdktool.c;beginline=2;endline=23;md5=d8d7552ef272d5fa59096ce436a537ef"

SRC_URI = "http://www.awfulhak.org/vmdktool/vmdktool-1.4.tar.gz"

SRC_URI[md5sum] = "b12a43f7605f6eae4a3ebe3a6c89ce88"
SRC_URI[sha256sum] = "981eb43d3db172144f2344886040424ef525e15c85f84023a7502b238aa7b89c"

RDEPENDS_${PN} = "zlib"

inherit base

do_compile() {
	oe_runmake PREFIX=${prefix}
}

do_install() {
	install -d ${D}${prefix}/bin
	install -d ${D}${prefix}/man/man8/
	oe_runmake install DESTDIR=${D} PREFIX=${prefix}
}

INSANE_SKIP_${PN} += "already-stripped installed-vs-shipped"

BBCLASSEXTEND = "native nativesdk"
