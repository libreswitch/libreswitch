DESCRIPTION = "xmltodict is a Python module that makes working with XML feel like you are working with JSON"
HOMEPAGE = "https://github.com/martinblech/xmltodict"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=01441d50dc74476db58a41ac10cb9fa2"

PR = "r0"
SRCNAME = "XmlToDict"

SRC_URI = "git://github.com/martinblech/xmltodict.git;protocol=https"
SRCREV = "4b634b92a697696684b20292b376d1acdfb71851"

S = "${WORKDIR}/git"

#RDEPENDS_${PN}_class-native = ""

BBCLASSEXTEND = "native nativesdk"
