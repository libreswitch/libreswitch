def map_go_arch(a, d):
    import re

    if   re.match('^x86.64$', a):  return 'amd64'
    elif re.match('^i.86$', a):    return '386'
    elif re.match('^arm$', a):     return 'arm'
    elif re.match('^aarch64$', a): return 'arm64'
    else:
        bb.error("cannot map '%s' to a Go architecture" % a)

export GOOS = "linux"
export GOARCH = "${@map_go_arch(d.getVar('TARGET_ARCH', True), d)}"
# We do need to setup goroot, otherwise the sstate'ed go tool will inject
# the calculate the wrong path
export GOROOT = "${STAGING_DIR_NATIVE}/${libdir}/${TARGET_SYS}/go"
export GOROOT_class-native = "${libdir}/go"
export GOROOT_FINAL = "${libdir}/${TARGET_SYS}/go"
export GOBIN_FINAL = "${GOROOT_FINAL}/bin/${GOOS}_${GOARCH}"
export GOPKG_FINAL = "${GOROOT_FINAL}/pkg/${GOOS}_${GOARCH}"
export GOSRC_FINAL = "${GOROOT_FINAL}/src"
export CFLAGS=""
export LDFLAGS=""
export CGO_CFLAGS="${BUILDSDK_CFLAGS} --sysroot=${STAGING_DIR_TARGET}"
export CGO_LDFLAGS="${BUILDSDK_LDFLAGS} --sysroot=${STAGING_DIR_TARGET}"
# The go code may fetch further code during the build
export http_proxy
export https_proxy

DEPENDS += "go-cross"
DEPENDS_class-native = "go-native"

FILES_${PN}-staticdev += "${GOSRC_FINAL}/${GO_IMPORT}"
FILES_${PN}-staticdev += "${GOPKG_FINAL}/${GO_IMPORT}*"
