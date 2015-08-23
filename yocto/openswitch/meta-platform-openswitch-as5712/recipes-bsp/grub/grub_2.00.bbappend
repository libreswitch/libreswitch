# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as5712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://grub.cfg.in"

include nfs.conf 

def get_default_ip(d):
    if d.getVar("BB_NO_NETWORK", True) == "1":
        bb.warn('Due lack of network connectivity, unable to automatically IP of this machine for NFS root setup, please set it up manually')
        return "127.0.0.1"

    import socket
    return [(s.connect(('8.8.8.8', 80)), s.getsockname()[0], s.close()) for s in [socket.socket(socket.AF_INET, socket.SOCK_DGRAM)]][0][1]

#NFS_SERVER_IP ??= "${@get_default_ip(d)}"
#NFS_SERVER_PATH ??= "${BUILD_ROOT}/nfsroot-${MACHINE}"
NFS_SERVER_IP ??= "192.168.0.1"
NFS_SERVER_PATH ??= "/srv/nfsroot-${MACHINE}"

do_install_append () {
    install -d ${D}/boot/grub
    sed -e "s?@@NFSROOTIP@@?${NFS_SERVER_IP}?" -e "s?@@NFSROOTPATH@@?${NFS_SERVER_PATH}?" \
      ${WORKDIR}/grub.cfg.in > ${D}/boot/grub/grub.cfg
}

FILES_${PN} += "/boot/grub/grub.cfg"
