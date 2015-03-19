DESCRIPTION = "An implementation of JSON-Schema validation for Python"
HOMEPAGE = "http://pypi.python.org/pypi/jsonschema"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=7a60a81c146ec25599a3e1dabb8610a8"

PR = "r0"
SRCNAME = "jsonschema"

SRC_URI = "http://pypi.python.org/packages/source/j/${SRCNAME}/${SRCNAME}-${PV}.zip"

SRC_URI[md5sum] = "0275f70c5f7c65657555ff478a4fc89c"
SRC_URI[sha256sum] = "c085fca29eeb7e42500aa2769f933b003becd95a95819b4565a734170de84497"

S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit distutils

RDEPENDS_${PN} = "\
    python-core \
    python-io \
    python-json \
    python-math \
    python-textutils \
    python-misc \
    python-tests \
    python-shell \
    python-numbers \
    python-lang \
    python-pkgutil \
    python-re \
    python-subprocess \
"

BBCLASSEXTEND = "native nativesdk"
