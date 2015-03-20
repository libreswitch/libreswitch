# Copyright 2014  Hewlett-Packard Development Company, L.P. 
PR_append = "_hp"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "\
    file://support-yocto-patched-gcc-4.8.patch \
"
