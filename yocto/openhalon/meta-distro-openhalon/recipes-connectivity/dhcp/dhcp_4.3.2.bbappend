# Copyright 2015 Hewlett-Packard Development Company, L.P.

SRC_URI += "file://dhclient@.service"

do_install_append () {
	install -m 0644 ${WORKDIR}/dhclient@.service ${D}${systemd_unitdir}/system/
	sed -i -e 's,@SBINDIR@,${sbindir},g' ${D}${systemd_unitdir}/system/dhclient@.service
	sed -i -e 's,@SYSCONFDIR@,${sysconfdir},g' ${D}${systemd_unitdir}/system/dhclient@.service
}

FILES_dhcp-client += "${systemd_unitdir}/system/dhclient@.service"

SYSTEMD_PACKAGES += "${PN}-client"
SYSTEMD_SERVICE_${PN}-client = "dhclient@.service"
