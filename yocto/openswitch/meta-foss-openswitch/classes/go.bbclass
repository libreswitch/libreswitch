def map_go_arch(a, d):
    import re

    if   re.match('^x86.64$', a):  return 'amd64'
    elif re.match('^i.86$', a):    return '386'
    elif re.match('^arm$', a):     return 'arm'
    elif re.match('^aarch64$', a): return 'arm64'
    else:
        bb.error("cannot map '%s' to a Go architecture" % a)


def go_extldflags(d):
    _, cc_args = d.getVar('CC').split(' ', 1)
    return cc_args + d.getVar('CFLAGS')


export GOOS = "linux"
export GOARCH = "${@map_go_arch(d.getVar('TARGET_ARCH', True), d)}"
export GOROOT_FINAL = "${libdir}/${TARGET_SYS}/go"
export GOBIN_FINAL = "${GOROOT_FINAL}/bin/${GOOS}_${GOARCH}"
export GOPKG_FINAL = "${GOROOT_FINAL}/pkg/${GOOS}_${GOARCH}"
export GOSRC_FINAL = "${GOROOT_FINAL}/src"
export CGO_CFLAGS = "${TARGET_CFLAGS}"
export CGO_DFLAGS = "${TARGET_LDFLAGS}"

# TODO(bluecmd): This is a hack to work around that Go doesn't have any
# good ways of passing arguments down to an external linker and doesn't
# fully respect CC due to a bug where the arguments passed inside CC are
# ignored.
# Use like "go build "-ldflags $CGO_LDFLAGS" x"
export CGO_LDFLAGS = "-linkmode=external '-extldflags=${@go_extldflags(d)}'"

DEPENDS += "go-cross"

FILES_${PN}-staticdev += "${GOSRC_FINAL}/${GO_IMPORT}"
FILES_${PN}-staticdev += "${GOPKG_FINAL}/${GO_IMPORT}*"
