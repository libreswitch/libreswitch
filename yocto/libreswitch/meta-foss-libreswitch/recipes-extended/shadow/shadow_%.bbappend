# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_libreswitch"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

EXTRA_OECONF_remove_class-native = "--without-audit"
