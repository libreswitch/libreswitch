# Copyright (C) 2016 Hewlett Packard Enterprise Development LP

KBRANCH_as6712  = "standard/base"
KMACHINE_as6712 ?= "common-pc-64"

SRCREV_machine_as6712 ?= "3d2455f9da30f923c6bd69014fad4cc4ea738be6"

COMPATIBLE_MACHINE_as6712 = "as6712"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://accton-hw-peripherals.cfg \
"
