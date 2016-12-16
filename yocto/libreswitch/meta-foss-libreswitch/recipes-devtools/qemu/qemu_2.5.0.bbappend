# Copyright (C) 2016 Hewlett Packard Enterprise Development LP

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
        file://vmdk-streamOptimized.patch \
"

PR_append = "_libreswitch"
