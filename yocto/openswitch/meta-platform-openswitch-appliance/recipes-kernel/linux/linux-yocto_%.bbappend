KBRANCH_appliance  = "standard/common-pc-64/base"
KMACHINE_appliance ?= "common-pc-64"

SRCREV_machine_appliance ?= "dbe5b52e93ff114b2c0f5da6f6af91f52c18f2b8"

COMPATIBLE_MACHINE_appliance = "appliance"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://ops-fragment.cfg \
	file://openvswitch.cfg \
        file://vmware-guest.cfg \
"
