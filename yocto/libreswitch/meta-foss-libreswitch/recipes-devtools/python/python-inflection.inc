DESCRIPTION = "Inflection is a string transformation library. It singularizes and pluralizes English words, and transforms strings from CamelCase to underscored string. Inflection is a port of Ruby on Rails' inflector to Python."
HOMEPAGE = "https://github.com/jpvanhal/inflection"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=de7833d9c2ce0052a4073928c76a13d7"

PR = "r0"

SRC_URI = "git://github.com/jpvanhal/inflection.git;protocol=https"

SRCREV = "663982efaa1a99f677e20c342a7a75dd9c041c8d"

S = "${WORKDIR}/git"

CLEANBROKEN = "1"

PR = "r3"

# RDEPENDS_${PN}_class-native = ""

BBCLASSEXTEND = "native nativesdk"
