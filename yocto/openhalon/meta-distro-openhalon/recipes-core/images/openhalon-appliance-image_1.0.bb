SUMMARY = "A VirtualBox image of Halon"

LICENSE = "Apache-2.0"

IMAGE_FEATURES += "ssh-server-openssh"

IMAGE_ROOTFS_SIZE = "524288"

# Do a quiet boot with limited console messages
APPEND += "quiet"
SYSLINUX_PROMPT ?= "0"
SYSLINUX_TIMEOUT ?= "0"

DEPENDS = "tar-native"
IMAGE_FSTYPES = "vmdk tar.gz"

inherit core-image

SRC_URI = " \
	file://halon.ovf \
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
	cp ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.vmdk halon.vmdk
	printf "SHA1 (halon.ovf)= %s\n" `sha1sum halon.ovf | cut -f1 -d' '` > halon.mf
	printf "SHA1 (halon.vmdk)= %s\n" `sha1sum halon.vmdk | cut -f1 -d' '` >> halon.mf
	tar cvf ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.ova halon.ovf halon.vmdk halon.mf
	ln -sf ${IMAGE_NAME}.ova ${DEPLOY_DIR_IMAGE}/${BPN}-${MACHINE}.ova
	cp halon.ovf box.ovf
	tar cvzf ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.box box.ovf halon.vmdk Vagrantfile metadata.json
	ln -sf ${IMAGE_NAME}.box ${DEPLOY_DIR_IMAGE}/${BPN}-${MACHINE}.box
}

python do_bundle_files() {
    bb.build.exec_func('create_bundle_files', d)
}

addtask bundle_files after do_vmdkimg before do_build
#do_bundle_files[nostamp] = "1"
