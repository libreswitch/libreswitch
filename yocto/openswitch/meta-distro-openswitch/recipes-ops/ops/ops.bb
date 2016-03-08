SUMMARY = "OpenSwitch"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "git://git.openswitch.net/openswitch/ops;protocol=https"

SRCREV = "d26bbb2b171c48e43287ea946e237c6b3bc12fc9"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

FILES_${PN} = "/usr/share/openvswitch/ /usr/share/openvswitch/*.extschema /usr/share/openvswitch/*.xml /usr/share/openvswitch/*.ovsschema"

OPS_SCHEMA_PATH="${S}/schema"

do_compile() {
  ${PYTHON} ${OPS_SCHEMA_PATH}/sanitize.py ${OPS_SCHEMA_PATH}/vswitch.extschema ${OPS_SCHEMA_PATH}/vswitch.ovsschema
  ${PYTHON} ${OPS_SCHEMA_PATH}/sanitize.py ${OPS_SCHEMA_PATH}/dhcp_leases.extschema ${OPS_SCHEMA_PATH}/dhcp_leases.ovsschema
  touch ${OPS_SCHEMA_PATH}/vswitch.xml
}

do_install() {
  install -d ${D}/${prefix}/share/openvswitch
	install -m 0644 ${OPS_SCHEMA_PATH}/vswitch.extschema ${D}/${prefix}/share/openvswitch/vswitch.extschema
	install -m 0644 ${OPS_SCHEMA_PATH}/vswitch.ovsschema ${D}/${prefix}/share/openvswitch/vswitch.ovsschema
	install -m 0644 ${OPS_SCHEMA_PATH}/vswitch.xml ${D}/${prefix}/share/openvswitch/vswitch.xml
	install -m 0644 ${OPS_SCHEMA_PATH}/dhcp_leases.extschema ${D}/${prefix}/share/openvswitch/dhcp_leases.extschema
	install -m 0644 ${OPS_SCHEMA_PATH}/dhcp_leases.ovsschema ${D}/${prefix}/share/openvswitch/dhcp_leases.ovsschema
	install -m 0644 ${OPS_SCHEMA_PATH}/dhcp_leases.xml ${D}/${prefix}/share/openvswitch/dhcp_leases.xml
	install -m 0644 ${OPS_SCHEMA_PATH}/configdb.ovsschema ${D}/${prefix}/share/openvswitch/configdb.ovsschema
	install -m 0644 ${OPS_SCHEMA_PATH}/vtep.ovsschema ${D}/${prefix}/share/openvswitch/vtep.ovsschema
	install -m 0644 ${OPS_SCHEMA_PATH}/vtep.xml ${D}/${prefix}/share/openvswitch/vtep.xml
}
