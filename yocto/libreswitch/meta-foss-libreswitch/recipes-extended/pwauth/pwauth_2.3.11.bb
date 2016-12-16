SUMMARY = "A Unix Authentication Tool"
HOMEPAGE = "https://code.google.com/p/pwauth/"
LICENSE = "BSD-3-Clause"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "git://github.com/phokz/pwauth"
SRCREV = "3b4ba71005c4e5436d0f2d1638bddaa86950e81c"

S = "${WORKDIR}/git/pwauth"

do_install() {
	install -d ${D}/usr/bin
	install -m 0755 pwauth ${D}/usr/bin
	# Avoid the perl dependency
	#install -m 0755 unixgroup ${D}/usr/bin
}
