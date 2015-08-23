SUMMARY = "OpenVSwitch for OpenSwitch"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "openssl python perl systemd ops-utils libtool"

PR = "r1"

SRC_URI = "git://git.openhalon.io/openhalon/openvswitch;preserve_origin=1;protocol=http \
   file://ovsdb-server.service \
   file://switchd_bcm.service \
   file://switchd_sim.service \
"

SRCREV="${AUTOREV}"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

PACKAGES =+ "ops-ovsdb python-ops-ovsdb"
PROVIDES = "${PACKAGES}"

RPROVIDES_${PN} = "virtual/switchd"

RDEPENDS_${PN} = "openssl procps util-linux-uuidgen util-linux-libuuid coreutils \
  python perl perl-module-strict sed gawk grep ops-ovsdb \
  ${@bb.utils.contains('MACHINE_FEATURES', 'ops-sim', 'openvswitch-sim-switch', '',d)} \
"

RDEPENDS_python-ops-ovsdb = "python-io python-netclient python-datetime \
  python-logging python-threading python-math python-fcntl python-resource"

EXTRA_OECONF += "TARGET_PYTHON=${bindir}/python \
                 TARGET_PERL=${bindir}/perl \
                 --disable-static --enable-shared\
                 ${@bb.utils.contains('MACHINE_FEATURES', 'ops-sim', '--enable-simulator-provider', '',d)} \
                 "
FILES_ops-ovsdb = "/run /var/run /var/log /var/volatile ${bindir}/ovsdb* \
  ${sbindir}/ovsdb-server ${datadir}/ovsdbmonitor ${sysconfdir}/openvswitch/ \
  ${libdir}/libovscommon.so.1* ${libdir}/libovsdb.so.1* \
  ${sysconfdir}/tmpfiles.d/openswitch.conf /usr/share/openvswitch/*.ovsschema /usr/share/openvswitch/vswitch.extschema"

inherit python-dir useradd

FILES_python-ops-ovsdb = "${PYTHON_SITEPACKAGES_DIR}/ovs"

FILES_${PN} = "${bindir}/ovs-appctl ${bindir}/ovs-pki ${bindir}/ovs-vsctl \
 /var/local/openvswitch ${sbindir}/ovs-vswitchd \
 ${libdir}/libofproto.so.1* ${libdir}/libopenvswitch.so.1* ${libdir}/libsflow.so.1*"

USERADD_PACKAGES = "${PN}"

GROUPADD_PARAM_${PN} ="-g 1020 ovsdb_users"

do_configure_prepend() {
    export OPEN_HALON_BUILD=1
    # After building the code with libltdl, we get a subdirectory with autoconf that will
    # inherit the m4 macros configurations from his parent, causing to fail if not finding some
    # of their macros. This hack removes the issue
    if [ -d ${S}/libltdl ] ; then
        if ! [ -L ${S}/libltdl/m4 ] ; then
            ln -s ../m4 ${S}/libltdl/m4
        fi
    fi
}

do_compile_prepend() {
    ${PYTHON} ${S}/vswitchd/sanitize.py ${S}/vswitchd/vswitch.extschema ${S}/vswitchd/vswitch.ovsschema
    touch ${S}/vswitchd/vswitch.xml
}

do_install_append() {
    install -m 0644 lib/libovscommon.pc ${D}/${libdir}/pkgconfig/
    install -m 0644 lib/libsflow.pc ${D}/${libdir}/pkgconfig/
    install -m 0644 lib/libopenvswitch.pc ${D}/${libdir}/pkgconfig/
    install -m 0644 ofproto/libofproto.pc ${D}/${libdir}/pkgconfig/
    install -m 0644 ovsdb/libovsdb.pc ${D}/${libdir}/pkgconfig/
    install -d ${D}${systemd_unitdir}/system
    install -d ${D}/var/local/openvswitch
    install -m 0644 ${WORKDIR}/ovsdb-server.service ${D}${systemd_unitdir}/system/
    if ${@bb.utils.contains('MACHINE_FEATURES','broadcom','true','false',d)}; then
        install -m 0644 ${WORKDIR}/switchd_bcm.service ${D}${systemd_unitdir}/system/switchd.service
    fi
    if ${@bb.utils.contains('MACHINE_FEATURES','ops-sim','true','false',d)}; then
        install -m 0644 ${WORKDIR}/switchd_sim.service ${D}${systemd_unitdir}/system/switchd.service
    fi
    install -d ${D}${sysconfdir}/tmpfiles.d
    echo "d /run/openvswitch/ 0770 - ovsdb_users -" > ${D}${sysconfdir}/tmpfiles.d/openswitch.conf
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}
    mv ${D}/${prefix}/share/openvswitch/python/ovs ${D}${PYTHON_SITEPACKAGES_DIR}
    install -m 0644 ${S}/vswitchd/vswitch.extschema ${D}/${prefix}/share/openvswitch/vswitch.extschema
}

pkg_postinst_ops-ovsdb () {
        # Trigger creation of the /run files
	if [ -z "$D" ]; then
		systemd-tmpfiles --create
	fi
}

INSANE_SKIP_${PN} = "installed-vs-shipped"

SYSTEMD_PACKAGES = "${PN} ops-ovsdb"

SYSTEMD_SERVICE_${PN} += "switchd.service"
SYSTEMD_SERVICE_ops-ovsdb = "ovsdb-server.service"

inherit openswitch autotools pkgconfig systemd
