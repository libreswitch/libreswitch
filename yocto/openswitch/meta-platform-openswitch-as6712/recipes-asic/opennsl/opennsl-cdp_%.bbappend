# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as6712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as6712"
OPENNSL_PLATFORM_BUILD = "399ff15"
GPL_MODULES_DIR = "sdk-6.4.10-gpl-modules"

SRC_URI[md5sum] = "e2c75b595b36cbc2a34fe061c351d6af"
SRC_URI[sha256sum] = "c4397b679a78ec981b7fb8279b37f0d36bea42498e340ba120bf3443760eceb0"
