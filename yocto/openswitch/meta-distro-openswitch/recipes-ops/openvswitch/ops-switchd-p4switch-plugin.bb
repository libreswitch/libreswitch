SUMMARY = "OpenSwitch OVS P4 switch plugin"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PROVIDES += "virtual/ops-switchd-switch-api-plugin"
RPROVIDES_${PN} += "virtual/ops-switchd-switch-api-plugin"

PV = "git${SRCPV}"

SRC_URI = "gitsm://git.openswitch.net/openswitch/ops-switchd-p4switch-plugin;protocol=http;branch=rel/dill"

SRCREV = "299a07a69a28708229e5d615a727714af602c46e"

S = "${WORKDIR}/git"

DEPENDS = "\
        judy \
        libedit \
        nanomsg \
        p4-hlir \
        thrift \
        thrift-native \
        ops-p4c-native \
        ops-p4c-libpd \
        ops-openvswitch \
"


inherit openswitch autotools-brokensep pythonnative pkgconfig

# temporary workaround while the correct code enters the repository
do_install() {
    if test -e "${S}/Makefile" ; then
        autotools_do_install
    else
        # install something until real files are built
        mkdir -p ${D}/usr/share/ovs_p4_plugin
        cp ${S}/test.txt ${D}/usr/share/ovs_p4_plugin/switch_bmv2.json

    fi
}

FILES_${PN} += "${libdir}/openvswitch/plugins"
FILES_${PN} += "/usr/share/ovs_p4_plugin/switch_bmv2.json"


EXTRA_OECONF = "--enable-bmv2 --disable-static CPPFLAGS='${CPPFLAGS} -DHOST_BYTE_ORDER_CALLER'"
