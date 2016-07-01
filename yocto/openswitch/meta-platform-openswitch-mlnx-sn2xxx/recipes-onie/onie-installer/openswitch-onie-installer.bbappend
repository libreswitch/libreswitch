# Copyright Mellanox Technologies, Ltd. 2001-2016.

PR_append = "_mlnx_sn2xxx"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "\
	file://onie.config \
"

IMAGE_NAME = "openswitch-disk-image"
ONIE_PREFIX = "x86_64-mlnx-sn2xxx"
