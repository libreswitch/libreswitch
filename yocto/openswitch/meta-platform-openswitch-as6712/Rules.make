# Copyright (C) 2015 Hewlett Packard Enterprise Development LP
# All Rights Reserved.
#
# The contents of this software are proprietary and confidential to the
# Hewlett-Packard Development Company, L. P.  No part of this program may be.
# photocopied, reproduced, or translated into another programming language.
# without prior written consent of the Hewlett-Packard Development Co., L. P.

DISTRO_KERNEL_FILE = $(BASE_BZIMAGE_FILE)
DISTRO_FS_FILE = $(BASE_CPIO_FS_FILE)
DISTRO_FS_TARGET = openswitch-disk-image
ONIE_INSTALLER_RECIPE = openswitch-onie-installer
ONIE_INSTALLER_FILE = onie-installer-x86_64-as6712_32x

# For this platform we create an onie-installer
all:: onie-installer

