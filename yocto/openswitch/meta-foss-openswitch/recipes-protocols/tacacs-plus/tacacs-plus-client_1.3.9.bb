SUMMARY = "TACACS+ protocol client library and PAM module"

DESCRIPTION = "TACACS+ protocol client library and PAM module in C. This PAM module support authentication, authorization (account management) and accounting (session management)performed using TACACS+ protocol designed by Cisco."

HOMEPAGE = "https://github.com/jeroennijhof/pam_tacplus"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

RDEPENDS_{PN} = "autoconf libtool openssl pam libpam libcrypto audit"

SRC_URI = "git://github.com/kshridha/pam_tacplus.git;protocol=https"
SRCREV = "c3daa9e830f59c12ecbe44dc88636dd32cf679ea"

S = "${WORKDIR}/git"

FILES_${PN} += "${libdir}/security/pam_tacplus.so \
                ${libdir}/security/pam_tacplus.la"

FILES_${PN}-dbg += "${libdir}/security/.debug/pam_tacplus.so"

do_configure() {
     cd ${S}
     autoreconf -v -i --force
     ./configure
     make
     make install
}

inherit autotools
