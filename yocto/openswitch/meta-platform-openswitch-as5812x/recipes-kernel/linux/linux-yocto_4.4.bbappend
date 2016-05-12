# Copyright (C) 2016 Hewlett Packard Enterprise Development LP

KBRANCH_as5812x  = "standard/base"
KMACHINE_as5812x ?= "common-pc-64"

SRCREV_machine_as5812x ?= "3d2455f9da30f923c6bd69014fad4cc4ea738be6"

COMPATIBLE_MACHINE_as5812x = "as5812x"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://accton-hw-peripherals.cfg \
"
