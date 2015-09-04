# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as5712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as5712"
OPENNSL_PLATFORM_BUILD = "88a82bb"
GPL_MODULES_DIR = "sdk-6.4.6-gpl-modules"

SRC_URI[md5sum] = "de4ccf1a82a6090a129f35f5884f860b"
SRC_URI[sha256sum] = "5ddcef619597117fc175a0da9b8d77bd7eb973b167456527b83beae657d72663"
