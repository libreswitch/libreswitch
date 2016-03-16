# Copyright (C) 2016, Cavium, Inc. All Rights Reserved.

PR_append = "_as7512"

do_install_append() {
    install -d ${D}/lib/modules/${KERNEL_VERSION}/extra
    install -m 0644 ${S}/lib/modules/as7512/xp80-Pcie-Endpoint.ko ${D}/lib/modules/${KERNEL_VERSION}/extra
}

inherit module-base
inherit kernel-module-split
