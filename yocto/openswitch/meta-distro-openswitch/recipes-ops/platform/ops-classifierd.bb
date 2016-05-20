SUMMARY = "OpenSwitch Classifier Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-hw-config ops-ovsdb ops-cli ops-switchd audit"
RDEPENDS_${PN} = "audit"

SRC_URI = "git://git.openswitch.net/openswitch/ops-classifierd;protocol=http;branch=rel/dill \
           file://ops-classifierd.service \
"

SRCREV = "5b9771f6ae683790c78e737f7613bdcb2edef38d"

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
SYSTEMD_AUTO_ENABLE = "disable"

inherit openswitch cmake pkgconfig systemd
