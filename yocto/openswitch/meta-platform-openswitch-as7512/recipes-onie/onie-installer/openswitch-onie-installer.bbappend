# Copyright (C) 2016, Cavium, Inc. All Rights Reserved.

PR_append = "_as7512"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "\
	file://onie.config \
"

IMAGE_NAME = "openswitch-disk-image"
ONIE_PREFIX = "x86_64-as7512_32x"
