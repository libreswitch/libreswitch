DESCRIPTION = "Target packages for Halon SDK"
LICENSE = "MIT"

PR = "r0"

inherit packagegroup

PACKAGEGROUP_DISABLE_COMPLEMENTARY = "1"

RDEPENDS_${PN} += " \
        packagegroup-core-standalone-sdk-target \
        cmake \
        yaml-cpp-dev \
        python-email \
        python-shell \
        redis-py \
        lttng-ust-dev \
        systemd-dev \
        libevent-dev \
	${@base_contains("MACHINE_FEATURES", "bcmsdk", "bcmsdk-dev ", "",d)} \
"
