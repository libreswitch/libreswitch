SUMMARY = "Python module for native access to the systemd facilities"

SECTION = "devel/python"

DEPENDS = "systemd"

LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=4fbd65380cdd255951079008b364516c"

SRC_URI = "git://github.com/systemd/python-systemd.git;protocol=git"
SRCREV = "8ccd64789ab030b76a99b578b5b1e9812b7a8cd8"

S = "${WORKDIR}/git"

inherit setuptools

do_compile_prepend() {
  # setup.py has '/usr/include/systemd/' hardcoded, so we symlink this instead
  sed -i 's/"\/usr\//"/' setup.py
  ln -sf "${STAGING_INCDIR}" include
}
