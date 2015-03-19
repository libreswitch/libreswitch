DESCRIPTION = "ZeroMQ - Intelligent Transport Layer"
HOMEPAGE = "http://zeromq.org"
LICENSE = "LGPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=f7b40df666d41e6508d03e1c207d498f"

SRC_URI = "http://download.zeromq.org/zeromq-4.0.3.tar.gz"

SRC_URI[md5sum] = "8348341a0ea577ff311630da0d624d45"
SRC_URI[sha256sum] = "57fa9205bda2813c6f7645d1d6016838d27bac833c1edebaecc7f3626144711a"

inherit autotools pkgconfig

BBCLASSEXTEND = "native nativesdk"
