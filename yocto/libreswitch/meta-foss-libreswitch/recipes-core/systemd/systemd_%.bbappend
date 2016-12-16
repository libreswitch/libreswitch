# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_libreswitch"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

ALTERNATIVE_${PN} += "systemd-def-target"

ALTERNATIVE_TARGET[systemd-def-target] = "${systemd_unitdir}/system/multi-user.target"
ALTERNATIVE_LINK_NAME[systemd-def-target] = "${systemd_unitdir}/system/default.target"
ALTERNATIVE_PRIORITY[systemd-def-target] ?= "1"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://systemctl-alias.sh \
    file://system.conf \
    file://systemd-coredump-groupname.patch \
"

# We remove the dependency from os-release to avoid rebuilding constantly
RRECOMMENDS_${PN}_remove = "os-release"

FILES_${PN} += "${sysconfdir}/profile.d/"

do_install_append() {
    install -d ${D}${sysconfdir}/profile.d
    install -m 755 ${WORKDIR}/systemctl-alias.sh ${D}${sysconfdir}/profile.d/systemctl-alias.sh
    ln -s /dev/null ${D}/etc/udev/rules.d/80-net-setup-link.rules
    install -m 644 ${WORKDIR}/system.conf ${D}${sysconfdir}/systemd/
    echo "L+ /var/log/journal 0666 root root - /var/diagnostics/logs" >> ${D}${sysconfdir}/tmpfiles.d/00-create-volatile.conf
    sed -i -e 's/.*Storage.*/Storage=persistent/' ${D}${sysconfdir}/systemd/journald.conf
    sed -i -e 's/.*SyncIntervalSec.*/SyncIntervalSec=1440m/' ${D}${sysconfdir}/systemd/journald.conf
    echo "GroupName=ops_coredump" >> ${D}${sysconfdir}/systemd/coredump.conf

    # Correct attributes in systemd, since we use /var/log/journal as a symlink into our support partition
    sed -i -e 's?/var/log/journal?/var/diagnostics/logs?' ${D}/${prefix}/lib/tmpfiles.d/systemd.conf
    sed -i -e 's?/var/log/journal?/var/diagnostics/logs?' ${D}/${prefix}/lib/tmpfiles.d/journal-nocow.conf
}

# We use systemd core dump, coredump
PACKAGECONFIG_append += "${@bb.utils.contains('DISTRO_FEATURES', 'audit', 'audit', '', d)} coredump resolved"

EXTRA_OECONF += "--with-dns-servers=" ""
EXTRA_OECONF_append = " --disable-timesyncd"
FILES_${PN} += "${bindir}/coredumpctl \
                 /var/log/journal"

# Make sure to only build against AppArmor if we want to
PACKAGECONFIG[apparmor] = "--enable-apparmor,--disable-apparmor,apparmor"
PACKAGECONFIG_append += "${@bb.utils.contains('DISTRO_FEATURES', 'apparmor', 'apparmor', '', d)}"

#pkg_postinst_udev-hwdb_prepend() {
#	# Abort script since causes problems for read-only fs
#	if test -n "$D"; then
#		exit 0;
#	fi
#}
