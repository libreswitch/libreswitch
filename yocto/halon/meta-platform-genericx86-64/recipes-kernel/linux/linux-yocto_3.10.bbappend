KBRANCH_genericx86-64  = "standard/common-pc-64/base"
KMACHINE_genericx86-64 ?= "common-pc-64"

SRCREV_machine_genericx86-64 ?= "2ee37bfe732c73f7d39af55875ce8d30b282471c"

COMPATIBLE_MACHINE_genericx86-64 = "genericx86-64"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://halon-fragment.cfg \
	file://openvswitch.cfg \
"

