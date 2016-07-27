# Copyright (C) 2016 Hewlett Packard Enterprise Development LP

PR_append = "_as7712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as7712"
OPENNSL_PLATFORM_BUILD = "399ff15"
GPL_MODULES_DIR = "sdk-6.4.10-gpl-modules"

SRC_URI[md5sum] = "51d4eaa9e6e6759d23d664526550f675"
SRC_URI[sha256sum] = "6d3ffb64b244539ef7af467ab6a46cdf16f31cdc008167c442517db35f93225a"
