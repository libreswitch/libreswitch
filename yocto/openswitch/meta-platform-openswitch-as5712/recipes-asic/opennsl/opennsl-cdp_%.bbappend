# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as5712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as5712"
OPENNSL_PLATFORM_BUILD = "399ff15"
GPL_MODULES_DIR = "sdk-6.4.10-gpl-modules"

SRC_URI[md5sum] = "255cff2e33bb40be99ea8fbb2d182341"
SRC_URI[sha256sum] = "f6fb82cdf819ec73f94274fc2160963623fa6ce38f6d9e92b45615237ab57708"
