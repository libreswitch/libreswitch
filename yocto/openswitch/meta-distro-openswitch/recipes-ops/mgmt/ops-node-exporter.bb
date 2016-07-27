SUMMARY = "OpenSwitch Node Exporter"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

PV="0.12.0"
SRC_URI = "git://github.com/prometheus/node_exporter.git"
SRCREV = "${PV}"
S = "${WORKDIR}/git"

SRC_URI += " \
    file://node-exporter.service \
    file://node-exporter.nginx \
"

inherit go useradd

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "--system --home /bin --user-group node-exporter"

do_compile() {
    mkdir -p go/src/github.com/prometheus
    ln -sf ${S} go/src/github.com/prometheus/node_exporter
    cd go/src/github.com/prometheus/node_exporter
    GOPATH="${B}/go" make
}

do_install() {
    install -d ${D}${bindir}
    install -m0755 ${S}/node_exporter ${D}${bindir}/
    install -d ${D}${systemd_unitdir}/system
    install -m 644 ${WORKDIR}/node-exporter.service \
        ${D}${systemd_unitdir}/system/
    install -d ${D}/etc/nginx/conf.d
    install -m 0644 ${WORKDIR}/node-exporter.nginx \
        ${D}/etc/nginx/conf.d/backend-node-exporter.conf
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "node-exporter.service"

inherit systemd
