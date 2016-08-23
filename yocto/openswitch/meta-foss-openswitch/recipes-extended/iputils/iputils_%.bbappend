# Copyright (C) 2016 Hewlett Packard Enterprise Development LP
PR_append = "_openswitch"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
	file://iputils-s20121221-fix-traceroute6.patch \
        file://iputils-s20121221-0001-traceroute6.patch \
        file://iputils-s20121221-fix-ping.patch \
"
