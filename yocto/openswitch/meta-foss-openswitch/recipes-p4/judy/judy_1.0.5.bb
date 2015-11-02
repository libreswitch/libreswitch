DESCRIPTION = "General purpose dynamic array"
HOMEPAGE = "http://judy.sourceforge.net/"
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=a2f59868b389d66faed0cf18e0caa486"

SECTION = "devel"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/${BPN}/${BPN}/Judy-${PV}/Judy-${PV}.tar.gz \
	file://out_of_tree_build.patch \
"
SRC_URI[md5sum] = "115a0d26302676e962ae2f70ec484a54"
SRC_URI[sha256sum] = "d2704089f85fdb6f2cd7e77be21170ced4b4375c03ef1ad4cf1075bd414a63eb"

inherit autotools
