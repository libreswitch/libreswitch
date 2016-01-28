SUMMARY = "Network Time Protocol daemon and utilities"
DESCRIPTION = "The Network Time Protocol (NTP) is used to \
synchronize the time of a computer client or server to \
another server or reference time source, such as a radio \
or satellite receiver or modem."
HOMEPAGE = "http://support.ntp.org"
SECTION = "net"
LICENSE = "NTP"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=f41fedb22dffefcbfafecc85b0f79cfa"

DEPENDS = "libevent libenv-perl libcap"
RDEPENDS_${PN} += "perl"

SRC_URI = "http://www.eecis.udel.edu/~ntp/ntp_spool/ntp4/ntp-4.2/ntp-${PV}.tar.gz"
SRC_URI[md5sum] = "6af96862b09324a8ef965ca76b759c8b"
SRC_URI[sha256sum] = "0d6961572548d2c4af96f58f763e22ac620f5afef717384ddc317a0e365cfdb9"

inherit autotools pkgconfig

EXTRA_OECONF += "--with-net-snmp-config=no \
                 --without-ntpsnmpd \
                 --with-yielding_select=yes \
                 "

do_install_append () {
  rm -rf ${D}${sbindir} ${D}${libdir} ${D}${datadir}
}

FILES_${PN} = "${bindir}"
