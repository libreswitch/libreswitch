# Copyright (C) 2016 Hewlett Packard Enterprise Development LP

PR_append = "_appliance"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

RDEPENDS_${PN}_append = " bash"

SRC_URI += "\
  file://simple_switch.service \
  file://setup_appliance_bmv2_ports.sh \
"

do_install_append() {
  install -d ${D}/usr/bin
  install -m 0755 ${WORKDIR}/setup_appliance_bmv2_ports.sh ${D}/usr/bin
}
