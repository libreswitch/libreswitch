# Copyright Mellanox Technologies, Ltd. 2001-2016.

KBRANCH_mlnx-sn2xxx  = "standard/base"
KMACHINE_mlnx-sn2xxx ?= "common-pc-64"

SRCREV_machine_mlnx-sn2xxx ?= "3d2455f9da30f923c6bd69014fad4cc4ea738be6"

COMPATIBLE_MACHINE_mlnx-sn2xxx = "mlnx-sn2xxx"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://pmbus.patch \
    file://ucd9200.patch \
"
