require go.inc

SRC_URI = "${SRC_URI_PREFIX}/go${PV}.src.tar.gz;name=go_1_6;subdir=go-${PV}"
SRC_URI[go_1_6.md5sum] = "bf3fce6ccaadd310159c9e874220e2a2"
SRC_URI[go_1_6.sha256sum] = "6326aeed5f86cf18f16d6dc831405614f855e2d416a91fd3fdc334f772345b00"

LIC_FILES_CHKSUM = "file://${WORKDIR}/go-${PV}/go/LICENSE;md5=591778525c869cdde0ab5a1bf283cd81"

S = "${WORKDIR}/go-${PV}/go"

DEPENDS = "go-bootstrap"

SRC_URI += "file://fix-cross-compilation.patch"

do_install() {
  GOROOT_FINAL="${D}${libdir}/go"

  install -d "${D}${bindir}" "${GOROOT_FINAL}"
  tar -C "${WORKDIR}/go-${PV}/go" -cf - bin lib src pkg test |
  tar -C "${GOROOT_FINAL}" -xpf -
  mv "${GOROOT_FINAL}/bin/"* "${D}${bindir}/"
}

## TODO: implement do_clean() and ensure we actually do rebuild super cleanly
