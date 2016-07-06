# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_openswitch"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

ALTERNATIVE_${PN} += "systemd-def-target"

ALTERNATIVE_TARGET[systemd-def-target] = "${systemd_unitdir}/system/multi-user.target"
ALTERNATIVE_LINK_NAME[systemd-def-target] = "${systemd_unitdir}/system/default.target"
ALTERNATIVE_PRIORITY[systemd-def-target] ?= "1"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEPENDS += "python-lxml-native libgcrypt"

inherit pythonnative python-dir

SRC_URI += "file://systemctl-alias.sh \
    file://silent-fsck-on-boot.patch \
    file://revert-ipv6ll-address-setting.patch \
    file://systemd-coredump-groupname.patch \
    file://systemd-coredump-conf.patch \
    file://system.conf \
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
}

# We use systemd core dump
EXTRA_OECONF_remove = "--disable-coredump"
EXTRA_OECONF += "--with-dns-servers=" ""
EXTRA_OECONF_append = " --disable-timesyncd"
FILES_${PN} += "${bindir}/coredumpctl \
                 /var/log/journal"
EXTRA_OECONF_remove = "--without-python"
EXTRA_OECONF += "with-python"

# Enable Audit framework on OpenSwitch
PACKAGECONFIG_append += "audit"

# Make sure to only build against AppArmor if we want to
PACKAGECONFIG[apparmor] = "--enable-apparmor,--disable-apparmor,apparmor"
PACKAGECONFIG_append += "${@bb.utils.contains('DISTRO_FEATURES', 'apparmor', 'apparmor', '', d)}"

PACKAGES =+ "python-${PN}-dbg python-${PN} python-${PN}-dev"

FILES_python-${PN} += "${libdir}/${PYTHON_DIR}/site-packages/systemd/ \
                              ${libdir}/${PYTHON_DIR}/site-packages/systemd/*.py ${libdir}/${PYTHON_DIR}/site-packages/systemd/*.so"

RDEPENDS_python-${PN} = "python-core"

FILES_python-${PN}-dbg += "${libdir}/${PYTHON_DIR}/site-packages/systemd/.debug/"

FILES_python-${PN}-dev += "${libdir}/${PYTHON_DIR}/site-packages/systemd/*.la"

#pkg_postinst_udev-hwdb_prepend() {
#	# Abort script since causes problems for read-only fs
#	if test -n "$D"; then
#		exit 0;
#	fi
#}
