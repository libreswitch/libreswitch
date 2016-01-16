SUMMARY = "OpenSwitch OVS P4 switch plugin"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-ovsdb"

PROVIDES += "virtual/ops-switchd-switch-api-plugin"
RPROVIDES_${PN} += "virtual/ops-switchd-switch-api-plugin"

# This is the repository we want to use
# SRC_URI = "git://git.openswitch.net/openswitch/ops-switchd-p4switch-plugin;protocol=http"

# This is the repository we'll be using until we get the code into the
# above repository
SRC_URI = "git://github.com/ops-p4/ops-switchd-p4switch-plugin;protocol=https"

FILES_${PN} = "${libdir}/openvswitch/plugins"

SRCREV = "${AUTOREV}"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

DEPENDS = "\
        judy \
        libedit \
        nanomsg \
        p4-hlir \
        python-native \
        python-pyyaml-native \
        python-tenjin \
        thrift \
        thrift-native \
        ops-p4c \
"

RDEPENDS_${PN} = "\
        judy \
        libedit \
        libpcap \
        nanomsg \
        thrift \
        libcrypto \
        gmp \
        libssl \
        ops-ovsdb \
"

FILES_${PN} += "/usr/share/ovs_p4_plugin/switch_bmv2.json"

inherit openswitch autotools-brokensep

EXTRA_OECONF = "--enable-bmv2 --disable-static CPPFLAGS='${CPPFLAGS} -DHOST_BYTE_ORDER_CALLER'"
EXTRA_OEMAKE = "PFX=${PKG_CONFIG_SYSROOT_DIR}"
