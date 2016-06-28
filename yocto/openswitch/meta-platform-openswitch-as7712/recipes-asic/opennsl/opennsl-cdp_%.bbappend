# Copyright (C) 2016 Hewlett Packard Enterprise Development LP

PR_append = "_as7712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as7712"
OPENNSL_PLATFORM_BUILD = "e8bdb92"
GPL_MODULES_DIR = "sdk-6.4.10-gpl-modules"

SRC_URI[md5sum] = "0982df84dd75ab043df5e26c47b9a7b8"
SRC_URI[sha256sum] = "d0c780660a7006c6a4e24b013711dea13a950c963469699b965d5795d45e0ac1"
