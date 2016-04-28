# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as5812t"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "\
	file://onie.config \
"

IMAGE_NAME = "openswitch-disk-image"
ONIE_PREFIX = "x86_64-as5812_54t"
