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
SRC_URI[md5sum] = "4a8636260435b230636f053ffd070e34"
SRC_URI[sha256sum] = "2ab3d0b5f0456e6311dda1cc27ab75da108762773a19e46abd938bd9407b97ee"
inherit autotools pkgconfig

EXTRA_OECONF += "--with-net-snmp-config=no \
                 --without-ntpsnmpd \
                 --with-yielding_select=yes \
                 "

do_install_append () {
  rm -rf ${D}${sbindir} ${D}${libdir} ${D}${datadir}
}

FILES_${PN} = "${bindir}"
