KBRANCH_appliance  = "standard/base"
KMACHINE_appliance ?= "common-pc-64"

SRCREV_machine_appliance ?= "3d2455f9da30f923c6bd69014fad4cc4ea738be6"

COMPATIBLE_MACHINE_appliance = "appliance"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://ops-fragment.cfg \
	file://openvswitch.cfg \
        file://ops-audit.cfg \
        file://vmware-guest.cfg \
"
