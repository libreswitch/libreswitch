# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as5712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as5712"
OPENNSL_PLATFORM_BUILD = "4c90aba"
GPL_MODULES_DIR = "sdk-6.4.10-gpl-modules"

SRC_URI[md5sum] = "45d81d395e05ff13f37efedac9c68059"
SRC_URI[sha256sum] = "db1f87f4e71c8ea9c129411f4fe2426203c2ca9fd8fc11cb082e04f49eb61b9b"
