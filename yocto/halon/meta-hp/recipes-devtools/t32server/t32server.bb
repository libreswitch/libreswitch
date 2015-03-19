DESCRIPTION = "Lauterbach T32 Server"
SECTION = "Developer Tools"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Proprietary;md5=0557f9d92cf58f2ccdd50f62f8ac0b28"

PR = "r0"
PV = "2012"

RDEPENDS_${PN} = "gdbserver"

SRC_URI = " \
file://makefile \
file://main.c \
file://main.h \
file://breakpoint.c \
file://breakpoint.h \
file://fileio.c \
file://fileio.h \
file://remote.c \
file://remote.h \
file://thread.c \
file://utils.c \
file://utils.h \
file://arm-dcc.c \
file://arm-dcc.h \
file://t32.service \
"

S = "${WORKDIR}"

do_compile() {
	make all
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 bin/t32server ${D}${bindir}
}
