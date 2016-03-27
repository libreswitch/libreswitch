KBRANCH_genericx86-64  = "standard/common-pc-64/base"
KMACHINE_genericx86-64 ?= "common-pc-64"

SRCREV_machine_genericx86-64 ?= "dbe5b52e93ff114b2c0f5da6f6af91f52c18f2b8"

COMPATIBLE_MACHINE_genericx86-64 = "genericx86-64"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://ops-fragment.cfg \
	file://openvswitch.cfg \
        file://strongswan-fragment.cfg \
        file://ops-audit.cfg \
"
