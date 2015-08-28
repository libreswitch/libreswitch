# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as5712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as5712"
OPENNSL_PLATFORM_BUILD = "7301d02673"
GPL_MODULES_DIR = "sdk-6.4.6-gpl-modules"

SRC_URI[md5sum] = "7c85459d7c84ebe21d5f79e06be89944"
SRC_URI[sha256sum] = "42b41f9afccc96439cfb722fbd854fdb22ecac3cf4c6b85eba713014fe7f4735"
