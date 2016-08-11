#
# This include points at uninative tarballs generated and maintained
# by the OpenSwitch Project. These file can be included by distros if they
# want to use/maintain a unified sstate feed for native/cross artefacts
# rather than the feeds having native/cross artefacts which are specific
# to the distro running on the build machine.
#

# This version of the uninative tools is generated with the same recipes as
# the yocto project, plus patches requires by OpenSwitch:
#
# - Update patchelf tools to newer version to avoid issues with go-generated binaries

UNINATIVE_URL = "http://archive.openswitch.net/uninative/0.95/"
UNINATIVE_CHECKSUM[x86_64] ?= "0e1496dc728e43803b9589c1d76eff735bfba12253414234d3459d4f0f46c665"
