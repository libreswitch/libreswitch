# Copyright Mellanox Technologies, Ltd. 2001-2016.

PR_append = "_mlnx_sn2xxx"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
  file://00-serial-setup.cfg \
"
