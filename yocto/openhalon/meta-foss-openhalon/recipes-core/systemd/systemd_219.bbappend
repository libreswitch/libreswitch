# Copyright 2015  Hewlett-Packard Development Company, L.P. 

PR_append = "_openhalon"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

ALTERNATIVE_${PN} += "systemd-def-target"

ALTERNATIVE_TARGET[systemd-def-target] = "${systemd_unitdir}/system/multi-user.target"
ALTERNATIVE_LINK_NAME[systemd-def-target] = "${systemd_unitdir}/system/default.target"
ALTERNATIVE_PRIORITY[systemd-def-target] ?= "1"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://systemctl-alias.sh"
FILES_${PN} += "${sysconfdir}/profile.d/"

do_install_append() {
    install -d ${D}${sysconfdir}/profile.d
    install -m 755 ${WORKDIR}/systemctl-alias.sh ${D}${sysconfdir}/profile.d/systemctl-alias.sh
}

# We use systemd core dump
EXTRA_OECONF_remove = "--disable-coredump"

FILES_${PN} += "${bindir}/coredumpctl"

#pkg_postinst_udev-hwdb_prepend() {
#	# Abort script since causes problems for read-only fs
#	if test -n "$D"; then
#		exit 0;
#	fi
#}

