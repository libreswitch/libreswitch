DESCRIPTION = "V8 JavaScript Engine"
HOMEPAGE = "https://code.google.com/p/v8/"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4fbc731ec49a1773a0db52c6b0eabb87"
PR = "r0"

COMPATIBLE_HOST = "powerpc"

SRC_URI_powerpc = "\
   git://github.com/andrewlow/v8ppc.git;protocol=https;name=v8 \
   svn://gyp.googlecode.com/svn/;protocol=https;module=trunk;name=gyp \
   svn://src.chromium.org/chrome/trunk/deps/third_party/;protocol=https;module=icu52;name=icu \
   svn://googletest.googlecode.com/svn/;protocol=https;module=trunk;name=gtest \
   svn://googlemock.googlecode.com/svn/;protocol=https;module=trunk;name=gmock \
"

S = "${WORKDIR}/git"

SRCREV_v8 = "0b59854"
SRCREV_gyp = "1831"
SRCREV_icu = "277999"
SRCREV_gtest = "692"
SRCREV_gmock = "485"

DEPENDS = "python-native"

V8_TARGET_powerpc = "ppc"

do_compile() {
	ln -sf ../../trunk build/gyp
	mv ../icu52 third_party/icu
	ln -sf ../../trunk testing/gmock
	ln -sf ../../trunk testing/gtest
	export LINK="$CXX"
	oe_runmake ${V8_TARGET}.release snapshot=off component=shared_library
}

do_install() {
	mkdir -p ${D}/usr/lib ${D}/usr/bin
	cp out/${V8_TARGET}.release/lib.target/*.so ${D}/usr/lib
	cp out/${V8_TARGET}.release/d8 ${D}/usr/bin
}

FILES_${PN} = "/usr/bin/d8 /usr/lib/"

