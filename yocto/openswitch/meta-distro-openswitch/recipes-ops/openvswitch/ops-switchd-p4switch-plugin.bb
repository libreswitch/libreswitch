SUMMARY = "OpenSwitch OVS P4 switch plugin"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "\
        gmp \
        judy \
        libedit \
        libpcap \
        nanomsg \
        ops-ovsdb \
        ops-switchd \
        ops-p4c \
        p4-hlir \
        python-native \
        python-pyyaml-native \
        python-tenjin \
        thrift \
        thrift-native \
        "

PROVIDES += "virtual/ops-switchd-switch-api-plugin"
RPROVIDES_${PN} += "virtual/ops-switchd-switch-api-plugin"

PV = "git${SRCPV}"

SRC_URI = "gitsm://git.openswitch.net/openswitch/ops-switchd-p4switch-plugin;protocol=http"

SRCREV = "ee94e9d0f362b6e834abf9a0692f60413309f875"

S = "${WORKDIR}/git"

inherit openswitch autotools-brokensep

# temporary workaround while the correct code enters the repository
do_install() {
    if test -e "${S}/Makefile" ; then
        autotools_do_install
    fi
}

FILES_${PN} += "${libdir}/openvswitch/plugins"
FILES_${PN} += "/usr/share/ovs_p4_plugin/switch_bmv2.json"

RDEPENDS_${PN} = "\
        gmp \
        judy \
        libcrypto \
        libedit \
        libpcap \
        libssl \
        nanomsg \
        ops-ovsdb \
        thrift \
        "

EXTRA_OECONF = "--enable-bmv2 --disable-static CPPFLAGS='${CPPFLAGS} -DHOST_BYTE_ORDER_CALLER'"
EXTRA_OEMAKE = "PFX=${PKG_CONFIG_SYSROOT_DIR}"
