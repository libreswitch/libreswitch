SUMMARY = "The Swiss Army knife of Python web development"
DESCRIPTION = "\
Werkzeug started as simple collection of various utilities for WSGI \
applications and has become one of the most advanced WSGI utility modules. \
It includes a powerful debugger, full featured request and response objects, \
HTTP utilities to handle entity tags, cache control headers, HTTP dates, \
cookie handling, file uploads, a powerful URL routing system and a bunch \
of community contributed addon modules."
HOMEPAGE = "http://werkzeug.pocoo.org/"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a68f5361a2b2ca9fdf26b38aaecb6faa"

SRC_URI = "https://pypi.python.org/packages/4c/01/7c64cf317ace1ff2c9b44cbf2c967dfcb170088b3676a871b7f08c059dfd/Werkzeug-0.10.1.tar.gz"
SRC_URI[md5sum] = "231d10996b7c3c1efe1776f22d570f83"
SRC_URI[sha256sum] = "9cf783990b1a99173e707a5768610800aa87775e9d86e211d17180d5b6c245ab"

S = "${WORKDIR}/Werkzeug-0.10.1"

PYPI_PACKAGE = "Werkzeug"

RDEPENDS_${PN} += " \
    ${PYTHON_PN}-difflib \
    ${PYTHON_PN}-email \
    ${PYTHON_PN}-html \
    ${PYTHON_PN}-json \
    ${PYTHON_PN}-netserver \
    ${PYTHON_PN}-numbers \
    ${PYTHON_PN}-pkgutil \
    ${PYTHON_PN}-subprocess \
    ${PYTHON_PN}-threading \
    ${PYTHON_PN}-unixadmin \
"
