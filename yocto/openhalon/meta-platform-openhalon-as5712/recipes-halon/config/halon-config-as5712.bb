SUMMARY = "Configuration files for AS5712"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

RPROVIDES_${PN} = "virtual/halon-config"

PR = "1"

SRC_URI = "git://git.openhalon.io/openhalon/as5712-config;protocol=http;preserve_origin=1 \
"

SRCREV="${AUTOREV}"

S = "${WORKDIR}/git"

do_install () {
   install -d ${D}${sysconfdir}/halon/platform/Accton/AS5712-54X
   cp -p ${S}/*.yaml ${D}${sysconfdir}/halon/platform/Accton/AS5712-54X
}

FILES_${PN} = "${sysconfdir}"

inherit halon
