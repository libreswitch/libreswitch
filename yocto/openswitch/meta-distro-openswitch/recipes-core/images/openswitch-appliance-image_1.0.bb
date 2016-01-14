SUMMARY = "A VirtualBox image of OpenSwitch"

LICENSE = "Apache-2.0"

IMAGE_ROOTFS_SIZE = "524288"

# Do a quiet boot with limited console messages
APPEND += "quiet"
SYSLINUX_PROMPT ?= "0"
SYSLINUX_TIMEOUT ?= "0"

DEPENDS = "tar-native"
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

    DISK_BOOT_VMDK_SIZE=`du -b ${DISTRO_NAME}.vmdk | awk '{ print $1 }'`

	sed -e "s|@@DISK_BOOT_NAME@@|${DISTRO_NAME}.vmdk|g" \
        -e "s|@@DISK_BOOT_VMDK_SIZE@@|${DISK_BOOT_VMDK_SIZE}|g" \
        -e "s|@@DISK_BOOT_CAPACITY@@|${IMAGE_ROOTFS_SIZE}|g" \
        -e "s|@@DISTRO_NAME@@|${DISTRO_NAME}-${DISTRO_VERSION}|g" \
        -e "s|@@CORE_NUMBER@@|${CORE_NUMBER}|g" \
        -e "s|@@RAM_SIZE@@|${RAM_SIZE}|g" \
	    -e "s|@@OVA_PRODUCT@@|${OVA_PRODUCT}|g" \
	    -e "s|@@OVA_VENDOR@@|${OVA_VENDOR}|g" \
	    -e "s|@@OVA_VENDOR_URL@@|${OVA_VENDOR_URL}|g" \
	    -e "s|@@OVA_VERSION@@|${OVA_VERSION}|g" \
            appliance.ovf.in  > ${DISTRO_NAME}.ovf

	printf "SHA1 (${DISTRO_NAME}.ovf)= %s\n" `sha1sum ${DISTRO_NAME}.ovf | cut -f1 -d' '` > ${DISTRO_NAME}.mf
	printf "SHA1 (${DISTRO_NAME}.vmdk)= %s\n" `sha1sum ${DISTRO_NAME}.vmdk | cut -f1 -d' '` >> ${DISTRO_NAME}.mf
	tar cvf ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.ova ${DISTRO_NAME}.ovf ${DISTRO_NAME}.mf ${DISTRO_NAME}.vmdk
	ln -sf ${IMAGE_NAME}.ova ${DEPLOY_DIR_IMAGE}/${BPN}-${MACHINE}.ova
	cp ${DISTRO_NAME}.ovf box.ovf
	tar cvzf ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.box box.ovf ${DISTRO_NAME}.vmdk Vagrantfile metadata.json
	ln -sf ${IMAGE_NAME}.box ${DEPLOY_DIR_IMAGE}/${BPN}-${MACHINE}.box
}

python do_bundle_files() {
    bb.build.exec_func('create_bundle_files', d)
}

addtask bundle_files after do_vmdkimg before do_build
#do_bundle_files[nostamp] = "1"

inherit openswitch-image
