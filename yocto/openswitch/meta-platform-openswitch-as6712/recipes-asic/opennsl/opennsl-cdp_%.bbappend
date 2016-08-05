# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as6712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as6712"
OPENNSL_PLATFORM_BUILD = "ac0f770"
GPL_MODULES_DIR = "sdk-6.4.10-gpl-modules"

SRC_URI[md5sum] = "d4a89627140dda1094b87d08f109362f"
SRC_URI[sha256sum] = "2b1d0e4a3b383c52cec1c4ca2795b32de08fd011d8643ecb2da63c205bb722b7"
