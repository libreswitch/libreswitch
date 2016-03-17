DESCRIPTION = "Frontend for the P4 compiler"
HOMEPAGE = "https://github.com/p4lang/p4-hlir"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2f3453ba8e98aaed11a290758a999e65"

SECTION = "devel"

# Upstream is not making releases, filed an issue about it.
#
# Meanwhile, as a workaround, pick a commit and grab the corresponding
# tarball.  Why not just point it to the Git repository? Because we
# really want to be using releases, not chasing some random commit
# upstream. Why not point it to git and set SRV_REV? As a reminder that
# we really don't want to be using Git.

SRC_COMMIT = "a0c233be8258ca3fbccd01313400df2b6400adeb"
SRC_URI = "https://github.com/p4lang/p4-hlir/archive/${SRC_COMMIT}.tar.gz"
SRC_URI[md5sum] = "f64460688df9eb6837ece6d31b238658"
SRC_URI[sha256sum] = "c9f2bb9df98cdf5df9da2c06e0f4861b96d709501628fa98e9d9b693d441b88b"

DEPENDS = "python-ply"
DEPENDS_class-native = "python-ply-native"

inherit setuptools

S = "${WORKDIR}/p4-hlir-${SRC_COMMIT}"

BBCLASSEXTEND = "native"
