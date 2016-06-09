SUMMARY = "BroadView for OpenSwitch"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-openvswitch ops-ovsdb ops-cli"

SRC_URI = "git://git.openswitch.net/openswitch/ops-broadview;protocol=https;branch=rel/dill \
    file://ops-broadview.service"


SRCREV="c98097852221dca1e98c1bcd390f4675c048e1d9"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
#PV = "git${SRCPV}"

S = "${WORKDIR}/git"

export  BV_OVS_INCLUDE="${STAGING_DIR_TARGET}/usr/include/ovs"
export  BV_OUTPUT="${S}/output/deliverables"
export  BV_TARGET_SYSROOT="${STAGING_DIR_TARGET}"
CFLAGS += "--sysroot=${STAGING_DIR_TARGET}"
do_compile () {
    export CROSS_COMPILE="${TARGET_PREFIX}"
    make
}

do_install() {
    # Installing executable
    install -d ${D}/usr/sbin
    install -m 0755 ${S}/output/deliverables/BroadViewAgent ${D}/usr/sbin/ops-broadview
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/ops-broadview.service ${D}${systemd_unitdir}/system/
    install -d ${D}/usr/lib/cli/plugins
    install -m 0755 ${S}/output/deliverables/libbroadview_cli.so.1 ${D}/usr/lib/cli/plugins/libbroadview_cli.so
}


SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "ops-broadview.service"
FILES_${PN} += "/usr/lib/cli/plugins/"

inherit openswitch systemd
