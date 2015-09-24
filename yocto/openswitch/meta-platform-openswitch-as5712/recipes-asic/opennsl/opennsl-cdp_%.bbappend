# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as5712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as5712"
OPENNSL_PLATFORM_BUILD = "f3cca35"
GPL_MODULES_DIR = "sdk-6.4.6-gpl-modules"

SRC_URI[md5sum] = "98d1837def3b3926fbebb15dd9bbb3e9"
SRC_URI[sha256sum] = "68cdf2610fe6e67da22108c66f1d786d007fbe65257eb7c797cfc1e16e971a6d"
