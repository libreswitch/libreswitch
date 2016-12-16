# Copyright (C) 2016 Hewlett Packard Enterprise Development LP

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SYSTEMD_AUTO_ENABLE = "disable"

PR_append = "_libreswitch"
