DESCRIPTION = "Go is an open source programming language that makes it easy to build simple, reliable, and efficient software."
HOMEPAGE = "https://golang.org/"
LICENSE = "BSD-3-Clause"

SRC_URI_PREFIX = "http://golang.org/dl/"
GO_BOOTSTRAP_VERSION="1.4.3"

setup_go_arch() {
  export GOHOSTOS="linux"
  export GOOS="linux"

  case "${TARGET_ARCH}" in
    x86_64)
      GOARCH=amd64
      ;;
    i586)
      GOARCH=386
      ;;
    arm)
      GOARCH=${TARGET_ARCH}
      case "${TUNE_PKGARCH}" in
        cortexa*)
          export GOARM=7
          ;;
      esac
      ;;
    aarch64)
      # ARM64 is invalid for Go 1.4
      GOARCH=arm64
      ;;
    *)
      GOARCH="${TARGET_ARCH}"
      ;;
  esac
  export GOARCH
}
