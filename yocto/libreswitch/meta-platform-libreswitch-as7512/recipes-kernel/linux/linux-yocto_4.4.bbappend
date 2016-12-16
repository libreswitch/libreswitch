# Copyright (C) 2016, Cavium, Inc. All Rights Reserved.

KBRANCH_as7512  = "standard/base"
KMACHINE_as7512 ?= "common-pc-64"

SRCREV_machine_as7512 ?= "3d2455f9da30f923c6bd69014fad4cc4ea738be6"

COMPATIBLE_MACHINE_as7512 = "as7512"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://accton-hw-peripherals.cfg \
"
