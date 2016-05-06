KBRANCH_genericx86-64  = "standard/base"
KMACHINE_genericx86-64 ?= "common-pc-64"

SRCREV_machine_genericx86-64 ?= "3d2455f9da30f923c6bd69014fad4cc4ea738be6"

COMPATIBLE_MACHINE_genericx86-64 = "genericx86-64"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://ops-fragment.cfg \
	file://openvswitch.cfg \
        file://strongswan-fragment.cfg \
        file://ops-audit.cfg \
"
