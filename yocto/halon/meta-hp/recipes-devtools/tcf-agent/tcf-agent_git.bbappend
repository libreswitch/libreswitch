# Copyright 2013  Hewlett-Packard Development Company, L.P. 

PR_append = "_magma"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# Default dummy interfaces
SRC_URI += " \
    file://tcf-agent.service \
"

#RDEPENDS_${PN} = ""

do_install_append () {
    if ${@base_contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -d ${D}${systemd_unitdir}/system
        install -m 0644 ${WORKDIR}/tcf-agent.service ${D}${systemd_unitdir}/system/tcf-agent.service
    fi
}

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "tcf-agent.service"
SYSTEMD_AUTO_ENABLE = "disable"
