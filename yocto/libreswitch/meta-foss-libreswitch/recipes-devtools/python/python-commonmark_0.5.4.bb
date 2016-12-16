DESCRIPTION = "Python parser for the CommonMark Markdown spec"
HOMEPAGE = "https://github.com/rtfd/CommonMark-py"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=37e127eb75a030780aefcfc584e78523"

SRC_URI = "https://pypi.python.org/packages/4d/93/3808cbcebe94d205f55a9a32857df733a603339d32c46cd32669d808d964/CommonMark-0.5.4.tar.gz"
SRC_URI[md5sum] = "8eaa7602f8086344ae9027d9a7b1d4b9"
SRC_URI[sha256sum] = "34d73ec8085923c023930dfc0bcd1c4286e28a2a82de094bb72fabcc0281cbe5"
S = "${WORKDIR}/CommonMark-0.5.4"

PYPI_PACKAGE_HASH = "e976be0d4ff0b85b44e7e669af8224e0f80c11042015154667a2d9d61a33"

inherit setuptools

BBCLASSEXTEND = "native"
