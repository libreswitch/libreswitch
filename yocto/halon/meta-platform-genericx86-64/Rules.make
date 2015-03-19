# Copyright (C) 2014 Hewlett-Packard Development Company, L.P.
# All Rights Reserved.
#
# The contents of this software are proprietary and confidential to the
# Hewlett-Packard Development Company, L. P.  No part of this program may be.
# photocopied, reproduced, or translated into another programming language.
# without prior written consent of the Hewlett-Packard Development Co., L. P.

MAGMA_KERNEL_FILE = $(MAGMA_BZIMAGE_FILE)
MAGMA_FS_FILE = $(MAGMA_OVA_FILE)
MAGMA_EXTRA_FS_FILES = $(MAGMA_BOX_FILE)
MAGMA_PRODUCT_FS_TARGET = halon-appliance-image

# For this platform we create a itb image that includes a kernel, fs and dtb
all:: kernel fs

