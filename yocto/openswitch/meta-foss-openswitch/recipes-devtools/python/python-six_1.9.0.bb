SUMMARY = "Six is a Python 2 and 3 compatibility library. It provides utility functions for smoothing over the differences \
           between the Python versions with the goal of writing Python code that is compatible on both Python versions"
HOMEPAGE = "https://pypi.python.org/pypi/six"

SECTION = "devel/python"

LICENSE = "MIT"
LIC_FILES_CHKSUM="file://LICENSE;md5=6f00d4a50713fa859858dd9abaa35b21"

SRCNAME = "six"
SRC_URI[md5sum] = "476881ef4012262dfc8adc645ee786c4"
SRC_URI[sha256sum] = "e24052411fc4fbd1f672635537c3fc2330d9481b18c0317695b46259512c91d5"

S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit pypi

CLEANBROKEN = "1"
