# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as5712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as5712"
OPENNSL_PLATFORM_BUILD = "b97666c"
GPL_MODULES_DIR = "sdk-6.4.6-gpl-modules"

SRC_URI[md5sum] = "0d95c5697ced51618e2e9a1fa91edb0f"
SRC_URI[sha256sum] = "0c9408824fd1ccfd08d27368798fad7bbcd3bb8e627f5b387a0ab42390484ab8"
