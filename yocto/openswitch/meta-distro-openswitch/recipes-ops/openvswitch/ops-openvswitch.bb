SUMMARY = "OpenVSwitch for OpenSwitch"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "openssl python perl systemd libtool libyaml jemalloc ops"

SRC_URI = "git://git.openswitch.net/openswitch/ops-openvswitch;protocol=http;branch=rel/dill \
   file://ovsdb-server.service \
   file://partial-map-updates.patch \
   file://on-demand-fetching.patch \
   file://compound-indexes.patch \
   file://idl_tracking_python.patch \
   file://smap-shash-add-numeric-and-flexible-sort.patch \
   file://json.py.patch \
"

SRCREV = "168822522aab9f13e68e8e5247ce2d7a928832f8"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

PACKAGES =+ "ops-ovsdb python-ops-ovsdb"
PROVIDES = "${PACKAGES}"

RDEPENDS_${PN} = "openssl procps util-linux-uuidgen util-linux-libuuid coreutils \
  python perl perl-module-strict sed gawk grep ops-ovsdb \
  ${@bb.utils.contains('MACHINE_FEATURES', 'ops-container', 'openvswitch-sim-switch', '',d)} \
"

RDEPENDS_${PN}_remove := "${@bb.utils.contains("IMAGE_FEATURES", "ops-p4", "openvswitch-sim-switch", "",d)}"

RDEPENDS_ops-ovsdb = "ops"

RDEPENDS_python-ops-ovsdb = "python-io python-netclient python-datetime \
  python-logging python-threading python-math python-fcntl python-resource"

EXTRA_OECONF += "TARGET_PYTHON=${bindir}/python \
                 TARGET_PERL=${bindir}/perl \
                 --disable-static --enable-shared LIBS=-ljemalloc \
                 ${@bb.utils.contains('MACHINE_FEATURES', 'ops-container', '--enable-simulator-provider', '',d)} \
                 "
FILES_ops-ovsdb = "/run /var/run /var/log /var/volatile ${bindir}/ovsdb* \
  ${sbindir}/ovsdb-server ${datadir}/ovsdbmonitor ${sysconfdir}/openvswitch/ \
  ${libdir}/libovscommon.so.1* ${libdir}/libovsdb.so.1* \
  ${sysconfdir}/tmpfiles.d/openswitch.conf"

inherit python-dir useradd

FILES_python-ops-ovsdb = "${PYTHON_SITEPACKAGES_DIR}/ovs"

FILES_${PN} = "${bindir}/ovs-appctl ${bindir}/ovs-pki ${bindir}/ovs-vsctl \
 /var/local/openvswitch \
 ${libdir}/libofproto.so.1* \
 ${libdir}/libopenvswitch.so.1* \
 ${libdir}/libsflow.so.1* \
 ${libdir}/libplugins.so.1* \
 ${libdir}/libvtep.so.1* \
"

USERADD_PACKAGES = "${PN}"

GROUPADD_PARAM_${PN} ="-g 1020 ovsdb-client;ops_netop;ops_admin"

do_configure_prepend() {
    export OPEN_HALON_BUILD=1
    export OPS_BUILD=1
    export BUILD_OVS_VSWITCHD=0
    export BUILD_PLUGINS_LIB=1
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
    cp ${STAGING_DIR_TARGET}/usr/share/openvswitch/vswitch.extschema ${S}/vswitchd/vswitch.extschema
    cp ${STAGING_DIR_TARGET}/usr/share/openvswitch/vswitch.ovsschema ${S}/vswitchd/vswitch.ovsschema
    cp ${STAGING_DIR_TARGET}/usr/share/openvswitch/vswitch.xml ${S}/vswitchd/vswitch.xml
    cp ${STAGING_DIR_TARGET}/usr/share/openvswitch/dhcp_leases.extschema ${S}/vswitchd/dhcp_leases.extschema
    cp ${STAGING_DIR_TARGET}/usr/share/openvswitch/dhcp_leases.ovsschema ${S}/vswitchd/dhcp_leases.ovsschema
    cp ${STAGING_DIR_TARGET}/usr/share/openvswitch/dhcp_leases.xml ${S}/vswitchd/dhcp_leases.xml
    cp ${STAGING_DIR_TARGET}/usr/share/openvswitch/configdb.ovsschema ${S}/vswitchd/configdb.ovsschema
    cp ${STAGING_DIR_TARGET}/usr/share/openvswitch/vtep.ovsschema ${S}/vswitchd/vtep.ovsschema
    cp ${STAGING_DIR_TARGET}/usr/share/openvswitch/vtep.xml ${S}/vswitchd/vtep.xml

    touch ${S}/vswitchd/vswitch.xml
}

do_install_append() {
    # Need to remove files to prevent double-install by autotools - these are already installed from ops.
    rm -f ${D}/${prefix}/share/openvswitch/*.ovsschema
    install -m 0644 lib/libovscommon.pc ${D}/${libdir}/pkgconfig/
    install -m 0644 lib/libsflow.pc ${D}/${libdir}/pkgconfig/
    install -m 0644 lib/libopenvswitch.pc ${D}/${libdir}/pkgconfig/
    install -m 0644 ofproto/libofproto.pc ${D}/${libdir}/pkgconfig/
    install -m 0644 ovsdb/libovsdb.pc ${D}/${libdir}/pkgconfig/
    install -d ${D}${systemd_unitdir}/system
    install -d ${D}/var/local/openvswitch
    install -m 0644 ${WORKDIR}/ovsdb-server.service ${D}${systemd_unitdir}/system/
    install -d ${D}${sysconfdir}/tmpfiles.d
    echo "d /run/openvswitch/ 0770 - ovsdb-client -" > ${D}${sysconfdir}/tmpfiles.d/openswitch.conf
    echo "a+ /run/log/journal/%m - - - - d:group:ops_netop:r-x" >> ${D}${sysconfdir}/tmpfiles.d/openswitch.conf
    echo "A+ /run/log/journal/%m - - - - group:ops_netop:r-x" >> ${D}${sysconfdir}/tmpfiles.d/openswitch.conf
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}
    mv ${D}/${prefix}/share/openvswitch/python/ovs ${D}${PYTHON_SITEPACKAGES_DIR}
}

pkg_postinst_ops-ovsdb () {
        # Trigger creation of the /run files
	if [ -z "$D" ]; then
		systemd-tmpfiles --create
	fi
}

INSANE_SKIP_${PN} = "installed-vs-shipped"

SYSTEMD_PACKAGES = "${PN} ops-ovsdb"

SYSTEMD_SERVICE_ops-ovsdb = "ovsdb-server.service"

inherit openswitch autotools pkgconfig systemd
