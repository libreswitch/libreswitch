# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as6712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as6712"
OPENNSL_PLATFORM_BUILD = "04b2579"
GPL_MODULES_DIR = "sdk-6.4.10-gpl-modules"

SRC_URI[md5sum] = "f8bad0770807e490db7802449bcf7886"
SRC_URI[sha256sum] = "07e7c73a89948faeb7b56ac16a7e4342cec9df4c083bdc2db756d281d439e7fc"
