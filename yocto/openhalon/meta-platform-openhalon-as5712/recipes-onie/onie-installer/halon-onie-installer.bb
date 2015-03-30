DESCRIPTION = "Halon ONIE Installer for AS5712"
SECTION = "BSP"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Proprietary;md5=0557f9d92cf58f2ccdd50f62f8ac0b28"

PR = "r0"
PV = "0.2"

IMAGE_NAME = "openhalon-disk-image"

SRC_URI = " \
    file://onie-installer-x86_64-as5712_54x.in \
"

S = "${WORKDIR}"

do_deploy () {
    install -d ${DEPLOYDIR}
    cat onie-installer-x86_64-as5712_54x.in ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}-${MACHINE}.cpio.gz > \
       ${DEPLOYDIR}/onie-installer-x86_64-as5712_54x
    chmod +x ${DEPLOYDIR}/onie-installer-x86_64-as5712_54x
}

addtask deploy before do_build after do_compile

do_deploy[depends] += "${IMAGE_NAME}:do_rootfs"

inherit deploy

