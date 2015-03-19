DESCRIPTION = "Scapy is a powerful interactive packet manipulation tool, \
packet generator, network scanner, network discovery, packet sniffer, etc. \
It can for the moment replace hping, 85% of nmap, arpspoof, arp-sk, arping, \
tcpdump, tethereal, p0f, ...."
SECTION = "devel/python"
HOMEPAGE = "http://www.secdev.org/projects/scapy/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
PRIORITY = "optional"
SRCNAME = "scapy"

PR = "r4"

SRC_URI = "https://pypi.python.org/packages/source/s/scapy/scapy-${PV}.tar.gz \
           file://vxlan.patch;striplevel=1 \
           file://whitespace.patch \
          "

SRC_URI[md5sum] = "1a5115d1f33548501d01d735bd689f13"
SRC_URI[sha256sum] = "22a11787273110fb19cec6215c73e0a90a0d049a77142c34252bb96e70198692"

S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit distutils

RDEPENDS_${PN} = "\
  python-netclient \
  python-netserver \
  python-shell \
  python-compression \
  python-zlib \
  python-subprocess \
  python-fcntl \
  python-crypt \
  python-threading \
"
