inherit native

require go_${PV}.inc

do_compile() {
  ## Setting `$GOBIN` doesn't do any good, looks like it ends up copying binaries there.
  export GOROOT_FINAL="${SYSROOT}${libdir}/${PN}-${PV}"

  setup_go_arch

  cd src && bash -x ./make.bash
}
