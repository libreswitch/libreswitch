DESCRIPTION = "Pcapy is a Python extension module that interfaces with the libpcap packet capture library."
SECTION = "devel/python"
HOMEPAGE = "https://github.com/CoreSecurity/pcapy"
LICENSE = "Apache-1.1"
LIC_FILES_CHKSUM="file://${COMMON_LICENSE_DIR}/Apache-1.1;md5=61cc638ff95ff4f38f243855bcec4317"

RDEPENDS_${PN} = "python libpcap"

PR = "r1"

SRC_URI = "git://github.com/CoreSecurity/pcapy.git;protocol=git"

SRCREV = "0.10.8"

S = "${WORKDIR}/git/"

inherit distutils

BBCLASSEXTEND = "native"
