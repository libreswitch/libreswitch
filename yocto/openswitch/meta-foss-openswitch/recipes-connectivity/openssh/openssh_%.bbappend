# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_openswitch"

FILESEXTRAPATHS_prepend := "${THISDIR}/openssh:"

RDEPENDS_${PN} += "${PN}-sftp ${PN}-sftp-server"
