# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as5712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as5712"
OPENNSL_PLATFORM_BUILD = "6a1da97"
GPL_MODULES_DIR = "sdk-6.4.6-gpl-modules"

SRC_URI[md5sum] = "796d8a2c13d355da2114abc99f48aaab"
SRC_URI[sha256sum] = "25833d461db61787bd7723b17c51e2f446edf9d4439e7a2e2915f2e6664c7c99"

do_install_append() {
    # Temporal fix until new release cames out
    sed -i -e 's/MB/M/' ${D}${sysconfdir}/modprobe.d/bcm-options.conf
}
