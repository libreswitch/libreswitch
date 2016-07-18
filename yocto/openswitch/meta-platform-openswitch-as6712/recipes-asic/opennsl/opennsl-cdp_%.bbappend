# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as6712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as6712"
OPENNSL_PLATFORM_BUILD = "6217657"
GPL_MODULES_DIR = "sdk-6.4.11-gpl-modules"

SRC_URI[md5sum] = "22dca54907c9608e1a50f279e610c325"
SRC_URI[sha256sum] = "11802b33cce44645a88a8f815e82d28a7619b08bfa466c648a7ec5b038caf4c6"
