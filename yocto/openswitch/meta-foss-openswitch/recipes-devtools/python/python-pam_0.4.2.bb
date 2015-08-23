SUMMARY = "Python-pam makes the PAM (Pluggable Authentication Modules) functions available in Python.       \
           With this module you can write Python applications that implement authentication services using PAM"

SECTION = "devel/python"

DEPENDS = "libpam"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRCNAME = "python-pam-0.4.2"
SRC_URI = "http://http.debian.net/debian/pool/main/p/python-pam/python-pam_0.4.2.orig.tar.gz"

SRC_URI[md5sum] = "e6e78959da5482b9a1aea1171589dd16"
SRC_URI[sha256sum] = "9ccce2e494c5869d99b20034fd40e368c35add4ef60ce3a33f5573c49a1e2edf"

S = "${WORKDIR}/${SRCNAME}"

inherit setuptools
