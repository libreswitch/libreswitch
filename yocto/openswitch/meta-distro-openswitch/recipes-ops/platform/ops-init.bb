SUMMARY = "OpenSwitch system initilization script."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

RDEPENDS_${PN} = "bash"

SRC_URI = "file://ops-init.service \
           file://ops-first-boot.service \
           file://ops-init.sh \
           file://ops-profile.sh \
           file://ops-vrf-sysctl-set.conf \
           file://ops-vrf-sysctl-unset.conf \
           file://ops-arp-sysctl-set.conf\
           file://ops-ping-sysctl-set.conf\
"

S = "${WORKDIR}"

do_install () {
    install -d ${D}${sbindir}
    install -d ${D}${systemd_unitdir}/system
    install -d ${D}${sysconfdir}/profile.d
    install -d ${D}${sysconfdir}/ops/sysctl.d
    install -d ${D}${sysconfdir}/sysctl.d

    install -m 0644 ${WORKDIR}/ops-first-boot.service ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/ops-init.service ${D}${systemd_unitdir}/system
    install -m 0755 ${WORKDIR}/ops-init.sh ${D}${sbindir}/ops-init
    install -m 0755 ${WORKDIR}/ops-profile.sh ${D}${sysconfdir}/profile.d/ops-profile
    install -m 0755 ${WORKDIR}/ops-vrf-sysctl-set.conf ${D}${sysconfdir}/ops/sysctl.d/ops-vrf-sysctl-set.conf
    install -m 0755 ${WORKDIR}/ops-vrf-sysctl-unset.conf ${D}${sysconfdir}/ops/sysctl.d/ops-vrf-sysctl-unset.conf
    install -m 0644 ${WORKDIR}/ops-arp-sysctl-set.conf ${D}${sysconfdir}/sysctl.d/
    install -m 0755 ${WORKDIR}/ops-ping-sysctl-set.conf ${D}${sysconfdir}/sysctl.d/
}

FILES_${PN} += "${sysconfdir}/ops/sysctl.d"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "ops-init.service ops-first-boot.service"

inherit systemd
