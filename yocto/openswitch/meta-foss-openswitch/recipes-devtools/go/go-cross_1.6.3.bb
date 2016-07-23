inherit cross

require go_${PV}.inc

do_compile() {
  export GOROOT_BOOTSTRAP="${SYSROOT}${STAGING_LIBDIR_NATIVE}/go-bootstrap-${GO_BOOTSTRAP_VERSION}"

  ## Setting `$GOBIN` doesn't do any good, looks like it ends up copying binaries there.
  export GOROOT_FINAL="${SYSROOT}${libdir}/go"

  setup_go_arch

  export CGO_ENABLED="1"
  export CC=${BUILD_CC}
  export GO_GCFLAGS="${HOST_CFLAGS}"
  export GO_LDFLAGS="${HOST_LDFLAGS}"

  cd ${WORKDIR}/go-${PV}/go/src && bash -x ./make.bash
}
