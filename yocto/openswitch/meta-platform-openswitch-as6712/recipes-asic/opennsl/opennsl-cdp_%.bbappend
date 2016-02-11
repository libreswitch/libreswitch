# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as6712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as6712"
OPENNSL_PLATFORM_BUILD = "b4c5954"
GPL_MODULES_DIR = "sdk-6.4.8-gpl-modules"

SRC_URI[md5sum] = "0e515e8bedf7b853a59ece09f618f86f"
SRC_URI[sha256sum] = "48e70a672f4e3ff78772fb8bb60a5b73279fbd2aff50f0daa06e883384c8a3b0"
