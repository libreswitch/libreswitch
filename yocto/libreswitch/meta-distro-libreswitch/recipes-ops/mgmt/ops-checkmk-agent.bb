SUMMARY = "Check_MK Agent for LibreSwitch"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM="file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

RDEPENDS_${PN} = "bash"

BRANCH ?= "${LBS_REPO_BRANCH}"

SRC_URI = "${LBS_REPO_BASE_URL}/checkmk-agent;protocol=${LBS_REPO_PROTOCOL};branch=${BRANCH} \
           file://checkmk-agent.sh \
           file://checkmk-agent@.service \
           file://checkmk-agent.socket \
"

SRCREV = "fa07cae60254093ff8ee166cd2cd251fe33808a5"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

do_install_prepend () {
    install -d ${D}${systemd_unitdir}/system
    install -m 644 ${WORKDIR}/checkmk-agent\@.service ${D}${systemd_unitdir}/system
    install -m 644 ${WORKDIR}/checkmk-agent.socket ${D}${systemd_unitdir}/system
    install -d ${D}${sysconfdir}/checkmk
    install -d ${D}${bindir}
    install -m 755 ${WORKDIR}/checkmk-agent.sh ${D}${bindir}/check_mk_agent
}

pkg_postinst_${PN} () {
    ln -s /lib/systemd/system/checkmk-agent\@.service /etc/systemd/system/sockets.target.wants/checkmk-agent\@.service
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "checkmk-agent@.service checkmk-agent.socket"

inherit systemd
