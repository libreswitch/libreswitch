# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

SRC_URI += "file://dhclient@.service file://dhclient-exit-hooks"

do_install_append () {
	install -m 0644 ${WORKDIR}/dhclient@.service ${D}${systemd_unitdir}/system/
	sed -i -e 's,@SBINDIR@,${sbindir},g' ${D}${systemd_unitdir}/system/dhclient@.service
	sed -i -e 's,@SYSCONFDIR@,${sysconfdir},g' ${D}${systemd_unitdir}/system/dhclient@.service
        install -m 0755 ${WORKDIR}/dhclient-exit-hooks ${D}${sysconfdir}/dhclient-exit-hooks
}

FILES_dhcp-client += "${systemd_unitdir}/system/dhclient@.service ${sysconfdir}/dhclient-exit-hooks"

SYSTEMD_PACKAGES += "${PN}-client"
SYSTEMD_SERVICE_${PN}-client = "dhclient@.service"
