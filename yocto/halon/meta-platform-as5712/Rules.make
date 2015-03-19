# Copyright (C) 2014 Hewlett-Packard Development Company, L.P.
# All Rights Reserved.
#
# The contents of this software are proprietary and confidential to the
# Hewlett-Packard Development Company, L. P.  No part of this program may be.
# photocopied, reproduced, or translated into another programming language.
# without prior written consent of the Hewlett-Packard Development Co., L. P.

HALON_KERNEL_FILE = $(HALON_BZIMAGE_FILE)
HALON_FS_FILE = $(HALON_CPIO_FS_FILE)
HALON_PRODUCT_FS_TARGET = halon-disk-image
ONIE_INSTALLER_RECIPE = halon-onie-installer
ONIE_INSTALLER_FILE = onie-installer-x86_64-as5712_54x

# For this platform we create a itb image that includes a kernel, fs and dtb
all:: kernel fs onie-installer

