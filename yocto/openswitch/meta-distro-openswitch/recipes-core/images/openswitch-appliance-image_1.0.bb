SUMMARY = "A virtual machine image of OpenSwitch"

LICENSE = "Apache-2.0"

# Image alignment is required for the resulting VMDK to be loadable by
# VirtualBox. To do this we need the partition size (IMAGE_ROOTFS_SIZE)
# to be less than: FINAL_ROOTFS_SIZE - BOOTDD_EXTRA_SPACE - Bootloader
# This is to align up neatly when we align the image.
#
# Technical details: VirtualBox requires that the grains of a streamOptimized
# VMDK are all filled. qemu-img sets 128 sectors per grain, so that means
# that we need to align on a 64KiB boundry.
IMAGE_ROOTFS_SIZE = "1015808"
FINAL_ROOTFS_SIZE = "1048576"

# Do a quiet boot with limited console messages
APPEND += "quiet"
SYSLINUX_PROMPT ?= "0"
SYSLINUX_TIMEOUT ?= "0"

DEPENDS = "tar-native qemu-native"
IMAGE_FSTYPES = "vmdk tar.gz"

CORE_NUMBER ??= "2"
RAM_SIZE ??= "512"
OVA_PRODUCT ??= "${DISTRO_NAME}"
OVA_VENDOR ??= "${DISTRO_NAME}"
OVA_VENDOR_URL ??= "http://www.openswitch.net"
OVA_VERSION ??= "${DISTRO_VERSION}"

SRC_URI = " \
	file://appliance.ovf.in \
	file://Vagrantfile \
	file://metadata.json \
"

IMAGE_CMD_ext4_append () {
	# We don't need to reserve much space for root, 0.5% is more than enough
	tune2fs -m 0.5 ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.ext4
}

addtask rootfs after do_unpack

python () {
	# Ensure we run these usually noexec tasks
	d.delVarFlag("do_fetch", "noexec")
	d.delVarFlag("do_unpack", "noexec")
}

create_bundle_files () {
	cd ${WORKDIR}

	cp ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.vmdk ${DISTRO_NAME}.vmdk

	DISK_BOOT_VMDK_SIZE=$(du -b ${DISTRO_NAME}.vmdk | awk '{ print $1 }')
	DISK_BOOT_CAPACITY=$(qemu-img info ${DISTRO_NAME}.vmdk \
		| awk -F '[\\( ]' '/^virtual size/ {print $5}')

	sed -e "s|@@DISK_BOOT_NAME@@|${DISTRO_NAME}.vmdk|g" \
        -e "s|@@DISK_BOOT_VMDK_SIZE@@|${DISK_BOOT_VMDK_SIZE}|g" \
        -e "s|@@DISK_BOOT_CAPACITY@@|${DISK_BOOT_CAPACITY}|g" \
        -e "s|@@DISTRO_NAME@@|${DISTRO_NAME}-${DISTRO_VERSION}|g" \
        -e "s|@@CORE_NUMBER@@|${CORE_NUMBER}|g" \
        -e "s|@@RAM_SIZE@@|${RAM_SIZE}|g" \
	    -e "s|@@OVA_PRODUCT@@|${OVA_PRODUCT}|g" \
	    -e "s|@@OVA_VENDOR@@|${OVA_VENDOR}|g" \
	    -e "s|@@OVA_VENDOR_URL@@|${OVA_VENDOR_URL}|g" \
	    -e "s|@@OVA_VERSION@@|${OVA_VERSION}|g" \
            appliance.ovf.in  > ${DISTRO_NAME}.ovf

	tar cvf ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.ova ${DISTRO_NAME}.ovf ${DISTRO_NAME}.vmdk
	ln -sf ${IMAGE_NAME}.ova ${DEPLOY_DIR_IMAGE}/${BPN}-${MACHINE}.ova
	cp ${DISTRO_NAME}.ovf box.ovf
	tar cvzf ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.box box.ovf ${DISTRO_NAME}.vmdk Vagrantfile metadata.json
	ln -sf ${IMAGE_NAME}.box ${DEPLOY_DIR_IMAGE}/${BPN}-${MACHINE}.box
}

align_directdisk() {
  truncate -s ${FINAL_ROOTFS_SIZE}K ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.hdddirect
}

python do_align_directdisk() {
    bb.build.exec_func('align_directdisk', d)
}

python do_bundle_files() {
    bb.build.exec_func('create_bundle_files', d)
}

addtask align_directdisk after do_bootdirectdisk before do_vmdkimg
addtask bundle_files after do_vmdkimg before do_build
#do_bundle_files[nostamp] = "1"

inherit openswitch-image
