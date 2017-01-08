SUMMARY = "LibreSwitch Classifier Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-hw-config ops-ovsdb ops-cli ops-switchd audit"
RDEPENDS_${PN} = "audit"

BRANCH ?= "${LBS_REPO_BRANCH}"

SRC_URI = "${LBS_REPO_BASE_URL}/classifierd;protocol=${LBS_REPO_PROTOCOL};branch=${BRANCH} \
           file://ops-classifierd.service \
"

SRCREV = "8f473be41cacf21b09b9b7d49cd0f730c1ca5fc0"

FILES_${PN} = "${libdir}/openvswitch/plugins ${includedir}/plugins/* ${bindir} ${bindir}/ops-classifierd"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/ops-classifierd.service ${D}${systemd_unitdir}/system/

    # Added for rest custom validators
    install -d ${D}/usr/share/opsplugins
    for plugin in $(find ${S}/ops/opsplugins -name "*.py"); do \
        install -m 0644 ${plugin} ${D}/usr/share/opsplugins
    done
}

FILES_${PN} += "/usr/share/opsplugins"
FILES_${PN} += "/usr/lib/cli/plugins/"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "ops-classifierd.service"

inherit libreswitch cmake pkgconfig systemd
