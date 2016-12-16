require opennsl-odp.inc

LIC_FILES_CHKSUM = "file://packages/cdp/docs/LICENSE_CDP_BIN;md5=e4111c2d8b944da9d44a0c458635d87f"

# Point by default to some http server where the file is hosted, even if doesn't exists
# Using the local file by default causes bitbake to warn againts the inability to find the
# file during the parsing stage
SRC_URI += "https://archive.openswitch.net/opennsl/opennsl-${PV}-odp.tar.bz2;name=opennsl"
# Use this SRC_URI if you put the tarball on the local download directory
#SRC_URI += "file://${DL_DIR}/opennsl-${PV}-odp.tar.bz2;name=opennsl"

SRC_URI[opennsl.md5sum] = "ddd11eb3e3d06e5bbe7c255bdd4dd725"
SRC_URI[opennsl.sha256sum] = "d03da8c03e852d50dc8328a452cf94f2517195b2a424bf4cd13db210a39ff790"
