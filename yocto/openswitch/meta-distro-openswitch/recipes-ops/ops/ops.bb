SUMMARY = "OpenSwitch"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "git://git.openswitch.net/openswitch/ops;protocol=https"

SRCREV = "94ef70c7cae761181159a44dfbc8de13ca8a1100"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

FILES_${PN} = "/usr/share/openvswitch/ /usr/share/openvswitch/*.extschema /usr/share/openvswitch/*.xml /usr/share/openvswitch/*.ovsschema"

do_compile() {
  ${PYTHON} ${S}/schema/sanitize.py ${S}/schema/vswitch.extschema ${S}/schema/vswitch.ovsschema
  ${PYTHON} ${S}/schema/sanitize.py ${S}/schema/dhcp_leases.extschema ${S}/schema/dhcp_leases.ovsschema
  touch ${S}/schema/vswitch.xml
}

do_install() {
  install -d ${D}/${prefix}/share/openvswitch
	install -m 0644 ${S}/schema/vswitch.extschema ${D}/${prefix}/share/openvswitch/vswitch.extschema
	install -m 0644 ${S}/schema/vswitch.ovsschema ${D}/${prefix}/share/openvswitch/vswitch.ovsschema
	install -m 0644 ${S}/schema/vswitch.xml ${D}/${prefix}/share/openvswitch/vswitch.xml
	install -m 0644 ${S}/schema/dhcp_leases.extschema ${D}/${prefix}/share/openvswitch/dhcp_leases.extschema
	install -m 0644 ${S}/schema/dhcp_leases.ovsschema ${D}/${prefix}/share/openvswitch/dhcp_leases.ovsschema
	install -m 0644 ${S}/schema/dhcp_leases.xml ${D}/${prefix}/share/openvswitch/dhcp_leases.xml
	install -m 0644 ${S}/schema/configdb.ovsschema ${D}/${prefix}/share/openvswitch/configdb.ovsschema
}
