# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

PR = "1"
S = "${WORKDIR}"


SRC_URI = " \
    file://00-eth0.link \
    file://oobm.network \
"
mgmtdev = "eth0"

do_install () {
   install -d ${D}${sysconfdir}/systemd/network/
   install -m 0644 00-eth0.link  ${D}${sysconfdir}/systemd/network/
   install -m 0644 oobm.network  ${D}${sysconfdir}/systemd/network/
   install -d ${D}${sysconfdir}/systemd/system/multi-user.target.wants/
   ln -sf ${systemd_unitdir}/system/dhclient@.service  \
                    ${D}${sysconfdir}/systemd/system/multi-user.target.wants/dhclient@${mgmtdev}.service
}

RDEPENDS_${PN} = "systemd dhcp-client"

FILES_${PN} = "${sysconfdir}"
