# Copyright 2014  Hewlett-Packard Development Company, L.P. 

PR_append = "_magma"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
            file://remove-run-length-encoding-for-dcc.patch \
"

