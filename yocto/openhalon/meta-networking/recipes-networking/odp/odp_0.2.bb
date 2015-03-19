DESCRIPTION = "Open Data Plane"
HOMEPAGE = "http://www.opendataplane.org"
SECTION = "Networking"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4ccfa994aa96974cfcd39a59faee20a2"

PR = "r2"
SRCREV = "odp-0.2"

# ARM support only for v7
COMPATIBLE_HOST = '(i.86|x86_64|powerpc|powerpc64).*-linux'
COMPATIBLE_HOST_armv7a = 'arm.*-linux'

SRC_URI = " \
    git://git.linaro.org/git/lng/odp.git;protocol=http \
"

S = "${WORKDIR}/git"

do_compile() {
	make all
}

do_install() {
	install -d ${D}${libdir}
	install -d ${D}${includedir}
	install -m 644 build/lib/libodp.a ${D}${libdir}
	cp -R build/include/* ${D}${includedir}
}

# Since we don't create any main package, we need to remove the dependency of the -dev one on the main
RDEPENDS_${PN}-dev = ""
