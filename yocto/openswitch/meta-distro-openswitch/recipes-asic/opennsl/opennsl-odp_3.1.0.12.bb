require opennsl-odp.inc

LIC_FILES_CHKSUM = "file://packages/cdp/docs/LICENSE_CDP_BIN;md5=e4111c2d8b944da9d44a0c458635d87f"

SRC_URI += "file://${DL_DIR}/opennsl-${PV}-odp.tar.bz2;name=tarball"

SRC_URI[tarball.md5sum] = "ddd11eb3e3d06e5bbe7c255bdd4dd725"
SRC_URI[tarball.sha256sum] = "d03da8c03e852d50dc8328a452cf94f2517195b2a424bf4cd13db210a39ff790"
