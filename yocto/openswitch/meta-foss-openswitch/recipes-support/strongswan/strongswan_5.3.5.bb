SUMMARY = "strongSWAN - IPsec for Linux"
HOMEPAGE = "https://www.strongswan.org/"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

DEPENDS = "gmp libsoup-2.4 openssl"

SRC_URI ="https://download.strongswan.org/strongswan-${PV}.tar.gz"
SRC_URI[md5sum] = "1e5a0ce628e4993208b332729a698f08"
SRC_URI[sha256sum] = "e3a3618446f2bb040881cffc598ac2aa59c4ed2ac9af74b8f58e4003a262c403"

EXTRA_OECONF += " --enable-openssl --without-lib-prefix "
EXTRA_OECONF += "${@base_contains('DISTRO_FEATURES', 'systemd', '--with-systemdsystemunitdir=${systemd_unitdir}/system/', '--without-systemdsystemunitdir', d)}"

FILES_${PN} += "${libdir}/ipsec/lib*${SOLIBS} ${libdir}/ipsec/plugins/*.so"
FILES_${PN}-dbg += "${libdir}/ipsec/.debug ${libdir}/ipsec/plugins/.debug ${libexecdir}/ipsec/.debug"
FILES_${PN}-dev += "${libdir}/ipsec/lib*${SOLIBSDEV} ${libdir}/ipsec/*.la ${libdir}/ipsec/plugins/*.la"
FILES_${PN}-staticdev += "${libdir}/ipsec/*.a ${libdir}/ipsec/plugins/*.a"

RPROVIDES_${PN} += "${PN}-systemd"
RREPLACES_${PN} += "${PN}-systemd"
RCONFLICTS_${PN} += "${PN}-systemd"
SYSTEMD_SERVICE_${PN} = "${BPN}.service"

inherit autotools gettext pkgconfig systemd
