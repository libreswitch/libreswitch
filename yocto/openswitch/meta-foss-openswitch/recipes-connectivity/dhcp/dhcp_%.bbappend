# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://dhclient@.service file://dhclient-exit-hooks file://dhclient6@.service"

do_install_append () {
	install -m 0644 ${WORKDIR}/dhclient@.service ${D}${systemd_unitdir}/system/
	install -m 0644 ${WORKDIR}/dhclient6@.service ${D}${systemd_unitdir}/system/
	sed -i -e 's,@SBINDIR@,${sbindir},g' ${D}${systemd_unitdir}/system/dhclient@.service ${D}${systemd_unitdir}/system/dhclient6@.service
	sed -i -e 's,@SYSCONFDIR@,${sysconfdir},g' ${D}${systemd_unitdir}/system/dhclient@.service ${D}${systemd_unitdir}/system/dhclient6@.service
        install -m 0755 ${WORKDIR}/dhclient-exit-hooks ${D}${sysconfdir}/dhclient-exit-hooks
}

FILES_dhcp-client += "${systemd_unitdir}/system/dhclient@.service ${sysconfdir}/dhclient-exit-hooks \
                      ${systemd_unitdir}/system/dhclient6@.service"

SYSTEMD_PACKAGES += "${PN}-client"
SYSTEMD_SERVICE_${PN}-client = "dhclient@.service dhclient6@.service"
