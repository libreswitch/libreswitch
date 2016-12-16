# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as5712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as5712"
OPENNSL_PLATFORM_BUILD = "6217657"
GPL_MODULES_DIR = "sdk-6.4.11-gpl-modules"

SRC_URI[md5sum] = "622cb7c0e1b91a25d0c06f49053b2b18"
SRC_URI[sha256sum] = "3e70ee5eb9e80a9de00ffa3568408ec0e680b1e03f73d721915e6aaee17ca3ca"
