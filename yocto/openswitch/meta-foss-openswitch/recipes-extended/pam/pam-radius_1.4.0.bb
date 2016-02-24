DESCRIPTION = "PAM Authentication and Accounting module"
HOMEPAGE = "http://freeradius.org/pam_radius_auth/"
SECTION = "libs"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=cbbd794e2a0a289b9dfcc9f513d1996e"

DEPENDS = "libpam"
PR = "r1"
S = "${WORKDIR}/pam_radius-${PV}"

SRC_URI = "ftp://ftp.freeradius.org/pub/radius/pam_radius-1.4.0.tar.gz \
           file://0001-Use-CHAP-for-PAM-RADIUS-user-authentication.patch \
           "
SRC_URI[md5sum] = "80960fdc9b720677dbb51d17311664a0"
SRC_URI[sha256sum] = "742d79fc39824726c098e746bd3dc3484f983f5ee082c621c1e848b2c3725305"

# This app looks like autotools based, but is really not
# so the are some hacks that we need to do:
# - No out of tree builds
# - Custom install target

inherit autotools

B = "${S}"

do_install() {
    install -d ${D}/lib/security
    install -m 0755 ${S}/pam_radius_auth.so ${D}/lib/security
    install -m 0755 ${S}/pam_radius_chap_auth.so ${D}/lib/security
}

# Create a package called pam-plugin-foo to match the way libpam packages in yocto
PACKAGES = "pam-plugin-radius-auth pam-plugin-radius-auth-dbg pam-plugin-radius-chap-auth"
PROVIDES = "${PN} ${PACKAGES}"

FILES_pam-plugin-radius-auth = "/lib/security/pam_radius_auth.so"
FILES_pam-plugin-radius-chap-auth = "/lib/security/pam_radius_chap_auth.so"
FILES_pam-plugin-radius-auth-dbg = "/lib/security/.debug /usr/src/debug"
