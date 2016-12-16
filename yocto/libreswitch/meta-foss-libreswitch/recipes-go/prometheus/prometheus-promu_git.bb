SUMMARY = "Prometheus Utility Tool"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "git://github.com/prometheus/promu.git"
SRCREV = "5e003068ac2fcd337c84ef57314c4744dbf1024b"
S = "${WORKDIR}/git"

inherit go

do_compile() {
    mkdir -p go/src/github.com/prometheus/
    ln -sf ${S} go/src/github.com/prometheus/promu
    cd go/src/github.com/prometheus/promu
    GOPATH="${B}/go" make
}

do_install() {
    install -d ${D}${bindir}
    install -m0755 ${S}/go/src/github.com/prometheus/promu/promu ${D}${bindir}/
}

CLEANBROKEN = "1"

BBCLASSEXTEND = "native"
