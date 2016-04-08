DESCRIPTION = "OpenSwitch ONIE Installer"
SECTION = "BSP"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r0"
PV = "1.0"

IMAGE_NAME ??= "undefined"
ONIE_PREFIX ??= "${BUILD_ARCH}-${MACHINE}"

SRC_URI = " \
    file://onie-installer.in \
"

S = "${WORKDIR}"

do_deploy () {
    install -d ${DEPLOYDIR}
    echo "DISTRO_NAME=${DISTRO_NAME}" | cat - onie.config onie-installer.in ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}-${MACHINE}.cpio.gz > \
       ${DEPLOYDIR}/onie-installer-${ONIE_PREFIX}
    chmod +x ${DEPLOYDIR}/onie-installer-${ONIE_PREFIX}
}

addtask deploy before do_build after do_compile

do_deploy[depends] += "${IMAGE_NAME}:do_rootfs"

inherit deploy
