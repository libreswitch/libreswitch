# Copyright (C) 2016, Cavium, Inc. All Rights Reserved.

PR_append = "_as7512"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
  file://00-serial-setup.cfg \
  file://20-diag-menu.cfg \
"
