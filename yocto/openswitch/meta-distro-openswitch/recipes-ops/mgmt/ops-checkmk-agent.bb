SUMMARY = "Check_MK Agent for Openswitch"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM="file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

RDEPENDS_${PN} = "bash"

SRC_URI = "file://checkmk-agent.sh \
    file://checkmk-agent@.service \
    file://checkmk-agent.socket \
"

S = "${WORKDIR}"

do_install () {
    install -d ${D}${systemd_unitdir}/system
    install -m 644 ${WORKDIR}/checkmk-agent\@.service ${D}${systemd_unitdir}/system
    install -m 644 ${WORKDIR}/checkmk-agent.socket ${D}${systemd_unitdir}/system
    install -d ${D}${sysconfdir}/checkmk
    install -d ${D}${bindir}
    install -m 755 ${WORKDIR}/checkmk-agent.sh ${D}${bindir}/check_mk_agent
}

FILES_${PN} += "${systemd_unitdir}/system/checkmk-agent*"
