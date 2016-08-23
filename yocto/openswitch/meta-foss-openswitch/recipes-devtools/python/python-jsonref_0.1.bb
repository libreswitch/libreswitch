SUMMARY = "An implementation of JSON Reference for Python"
HOMEPAGE = "https://pypi.python.org/pypi/jsonref"

SECTION = "devel/python"

LICENSE = "MIT"
LIC_FILES_CHKSUM="file://LICENSE;md5=a34264f25338d41744dca1abfe4eb18f"

SRCNAME = "jsonref"
SRC_URI[md5sum] = "a7f511bd8d4b7b696b230aee7fa3c62b"
SRC_URI[sha256sum] = "56e0ce228798bdecadff865c142741b1ffdd57a5e9c40c40ec2b641d7c430ad3"

S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit pypi

CLEANBROKEN = "1"
BBCLASSEXTEND = "native"
