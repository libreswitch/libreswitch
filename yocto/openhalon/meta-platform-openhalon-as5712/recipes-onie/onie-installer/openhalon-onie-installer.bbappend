# Copyright 2015  Hewlett-Packard Development Company, L.P. 

PR_append = "_as5712"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "\
	file://onie.config \
"

IMAGE_NAME = "openhalon-disk-image"
ONIE_PREFIX = "x86_64-as5712_54x"
