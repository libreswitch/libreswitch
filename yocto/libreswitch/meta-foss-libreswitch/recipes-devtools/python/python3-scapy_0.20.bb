DESCRIPTION = "Scapy is a powerful interactive packet manipulation tool, \
packet generator, network scanner, network discovery, packet sniffer, etc. \
It can for the moment replace hping, 85% of nmap, arpspoof, arp-sk, arping, \
tcpdump, tethereal, p0f, ...."
SECTION = "devel/python"
HOMEPAGE = "http://www.secdev.org/projects/scapy/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://bin/scapy;beginline=3;endline=18;md5=a5be896f88f8396346f67f7a8878ee09"
PRIORITY = "optional"
SRCNAME = "scapy"
PR = "ml2"

SRC_URI = "https://pypi.python.org/packages/36/0a/d6f4f3c621a1d5e063ccae5e91816f565d92ecf0a372193f141b15859539/scapy-python3-0.20.tar.gz"
SRC_URI[md5sum] = "708456a38987bb76b35d6594875a5b2b"

S = "${WORKDIR}/scapy-python3-0.20"

inherit setuptools3

RDEPENDS_${PN} = "\
  ${PYTHON_PN}-netclient \
  ${PYTHON_PN}-netserver \
  ${PYTHON_PN}-pydoc \
  ${PYTHON_PN}-pkgutil \
"
