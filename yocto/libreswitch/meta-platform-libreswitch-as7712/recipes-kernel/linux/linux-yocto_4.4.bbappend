# Copyright (C) 2016 Hewlett Packard Enterprise Development LP

KBRANCH_as7712  = "standard/base"
KMACHINE_as7712 ?= "common-pc-64"

SRCREV_machine_as7712 ?= "3d2455f9da30f923c6bd69014fad4cc4ea738be6"

COMPATIBLE_MACHINE_as7712 = "as7712"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://accton-hw-peripherals.cfg \
    file://bcm-knet-requirements.cfg \
"
