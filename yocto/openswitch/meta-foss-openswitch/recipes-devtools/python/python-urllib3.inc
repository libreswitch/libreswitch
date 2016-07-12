DESCRIPTION = "Python HTTP library with thread-safe connection pooling, file post support, sanity friendly, and more"
HOMEPAGE = "https://github.com/shazow/urllib3"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=ea114851ad9a8c311aac8728a681a067"

PR = "r0"
SRCNAME = "UrlLib3"

SRC_URI = "git://github.com/shazow/urllib3.git;protocol=https;branch=release"
SRCREV = "2762e84dae1858866a915f427ff1b92e825ea1c6"

S = "${WORKDIR}/git"

#RDEPENDS_${PN}_class-native = ""

BBCLASSEXTEND = "native nativesdk"
