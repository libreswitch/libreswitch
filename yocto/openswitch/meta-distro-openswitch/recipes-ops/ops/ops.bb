SUMMARY = "OpenSwitch"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

BRANCH ?= "${OPS_REPO_BRANCH}"

SRC_URI = "${OPS_REPO_BASE_URL}/ops;protocol=${OPS_REPO_PROTOCOL};branch=${BRANCH}"

SRCREV = "1af0a0dbbd244ee284d0ac46ebce9ca066912b79"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

FILES_${PN} = "/usr/share/openvswitch/ /usr/share/openvswitch/*.extschema /usr/share/openvswitch/*.xml /usr/share/openvswitch/*.ovsschema"

OPS_SCHEMA_PATH="${S}/schema"

inherit openswitch cmake

# XXX: Remove this when the repo moves to cmake
do_configure() {
	if test -e "${S}/CMakeLists.txt" ; then
		cmake_do_configure
	fi
}

do_compile() {
	if test -e "${S}/CMakeLists.txt" ; then
		cmake_do_compile
	else
		oe_runmake -C "${S}"
	fi
}

do_install() {
	if test -e "${S}/CMakeLists.txt" ; then
		cmake_do_install
	else
		oe_runmake -C "${S}" install DESTDIR=${D} PREFIX=${prefix}
	fi
}
