SUMMARY = "py.test plugin to abort hanging tests"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d8048cd156eda3df2e7f111b0ae9ceff"

SRC_URI[md5sum] = "0c44e5e03b15131498a86169000cb050"
SRC_URI[sha256sum] = "c42b4106158b43500ea6a433dfee26d1068943ff6673a41e85ea367e38810673"

RDEPENDS_${PN} = "python-py"
DEPENDS_class-native = "python-py-native"

inherit pypi

BBCLASSEXTEND = "native nativesdk"
