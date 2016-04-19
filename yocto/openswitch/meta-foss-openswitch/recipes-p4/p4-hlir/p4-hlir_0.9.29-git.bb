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

SRC_COMMIT = "b825fd54931868f3748de91b6491780b82847cb5"
SRC_URI = "https://github.com/p4lang/p4-hlir/archive/${SRC_COMMIT}.tar.gz"
SRC_URI[md5sum] = "4ef990ab0888d0ef8a8454b9dae951d7"
SRC_URI[sha256sum] = "68bfe5c6f222481ab82a880283cb755e055f56e08ddd0975e578cda7d347a0ab"

DEPENDS = "python-ply"
DEPENDS_class-native = "python-ply-native"

inherit setuptools

S = "${WORKDIR}/p4-hlir-${SRC_COMMIT}"

BBCLASSEXTEND = "native"
