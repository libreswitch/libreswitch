SUMMARY = "Pyroute2 is a pure Python netlink and Linux network configuration library.   \
                          It requires only Python stdlib, no 3rd party libraries. "
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI[md5sum] = "3427f498bbf52179239109a8cc483735"
SRC_URI[sha256sum] = "ceaff55dc0361c1757fd27444ec23a7ada35c853c89ca226f4dd36d187f36be3"

RDEPENDS_${PN} = "python-py python-multiprocessing"
DEPENDS_class-native = "python-py-native"

inherit pypi

BBCLASSEXTEND = "native nativesdk"
