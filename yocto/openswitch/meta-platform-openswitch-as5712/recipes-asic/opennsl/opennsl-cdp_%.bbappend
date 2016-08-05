# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as5712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as5712"
OPENNSL_PLATFORM_BUILD = "ac0f770"
GPL_MODULES_DIR = "sdk-6.4.10-gpl-modules"

SRC_URI[md5sum] = "f11c8eb2812acb09c3d1bcef9e1e63c6"
SRC_URI[sha256sum] = "52b9c718f3c4c9de4de24db491396318e13116d57ec341db7abe0d5b5f5fb830"
