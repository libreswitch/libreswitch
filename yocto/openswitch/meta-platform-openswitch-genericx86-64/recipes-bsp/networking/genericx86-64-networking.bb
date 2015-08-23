# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Proprietary;md5=0557f9d92cf58f2ccdd50f62f8ac0b28"

PR = "1"
S = "${WORKDIR}"


SRC_URI = " \
    file://00-eth0.link \
    file://oobm.network \
"

do_install () {
   install -d ${D}${sysconfdir}/systemd/network/
   install -m 0644 00-eth0.link  ${D}${sysconfdir}/systemd/network/
   install -m 0644 oobm.network  ${D}${sysconfdir}/systemd/network/
}

FILES_${PN} = "${sysconfdir}"

