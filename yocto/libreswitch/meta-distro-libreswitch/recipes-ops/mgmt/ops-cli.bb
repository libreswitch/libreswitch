SUMMARY = "LibreSwitch CLI"
LICENSE = "GPL-2.0 & LGPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=81bcece21748c91ba9992349a91ec11d\
                    file://COPYING.LIB;md5=01ef24401ded36cd8e5d18bfe947240c"

DEPENDS = "ops-utils ops-ovsdb ops-rbac"

BRANCH ?= "${LBS_REPO_BRANCH}"

SRC_URI = "${LBS_REPO_BASE_URL}/cli;protocol=${LBS_REPO_PROTOCOL};branch=${BRANCH} \
"

SRCREV = "ce5d2be6a31fca8734e69755a68075ed4992ece9"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

FILES_${PN} += "/usr/share/opsplugins"
do_install_append() {
    # Code to copy ECMP custom validator to /usr/share/opsplugins.
    install -d ${D}/usr/share/opsplugins
    for plugin in $(find ${S}/opsplugins -name "*.py"); do \
        install -m 0644 ${plugin} ${D}/usr/share/opsplugins
    done
}

inherit libreswitch pkgconfig cmake
