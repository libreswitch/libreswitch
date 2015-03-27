SUMMARY = "A Unix Authentication Tool"
HOMEPAGE = "https://code.google.com/p/pwauth/"
LICENSE = "BSD-3-Clause"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "https://pwauth.googlecode.com/files/${PN}-${PV}.tar.gz"
SRC_URI[md5sum] = "2b7812724653bb0d238c1e0dfd5affdf"
SRC_URI[sha256sum] = "3c6ab789d6ec92984954b4217418781508df393639adc32514d8491d8825fa22"

S = "${WORKDIR}/${PN}-${PV}"

do_install() {
	install -d ${D}/usr/bin
	install -m 0755 pwauth ${D}/usr/bin
	# Avoid the perl dependency
	#install -m 0755 unixgroup ${D}/usr/bin
}
