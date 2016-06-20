SUMMARY = "OpenSwitch Network Time Protocol Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-utils ops-ovsdb ops-cli ops-supportability"

RDEPENDS_${PN} = "ntp"
SRC_URI = "git://git.openswitch.net/openswitch/ops-ntpd;protocol=http;branch=rel/dill \
           file://ops-ntpd.service \
"

SRCREV = "c87a40bb098a72fcea6a12a69b30f09f3317e4fc"

# Mixing of two classes, the build happens on the source directory.
inherit openswitch cmake setuptools systemd

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"
do_compile() {
     cd ${S}
     distutils_do_compile
     # Cmake compile changes to the B directory
     cmake_do_compile
}

do_install() {
     cd ${S}
     distutils_do_install
     # Cmake compile changes to the B directory
     cmake_do_install
     install -d ${D}${systemd_unitdir}/system
     install -m 0644 ${WORKDIR}/ops-ntpd.service ${D}${systemd_unitdir}/system/

     # Code to copy NTP custom validators to /usr/share/opsplugins.
     install -d ${D}/usr/share/opsplugins
     for plugin in $(find ${S}/opsplugins -name "*.py"); do \
         install -m 0644 ${plugin} ${D}/usr/share/opsplugins
     done
}

FILES_${PN} += "/usr/lib/cli/plugins/ /usr/share/opsplugins"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "ops-ntpd.service"
