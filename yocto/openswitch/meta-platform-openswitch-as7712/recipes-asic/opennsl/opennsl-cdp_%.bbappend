# Copyright (C) 2016 Hewlett Packard Enterprise Development LP

PR_append = "_as7712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as7712"
OPENNSL_PLATFORM_BUILD = "ac0f770"
GPL_MODULES_DIR = "sdk-6.4.10-gpl-modules"

SRC_URI[md5sum] = "6ef78bdac293015ca5f2a8d03bc36215"
SRC_URI[sha256sum] = "d196b73baf54f34a1b12f398e7793ead769241f316eee697a449482854a3910f"
