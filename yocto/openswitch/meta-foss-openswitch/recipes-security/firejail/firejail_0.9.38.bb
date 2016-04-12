SUMMARY = "Linux namespaces and seccomp-bpf sandbox"

DESCRIPTION = "Firejail is a SUID program that reduces the risk of security breaches by restricting the running environment of untrusted applications using Linux namespaces and seccomp-bpf."

HOMEPAGE = "https://firejail.wordpress.com/"

SECTION = "base"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM ="file://COPYING;md5=a41ad1c85f8bc03e14593891be09cf09"

RDEPENDS_${PN} = "bash"

SRC_URI="https://github.com/netblue30/firejail/archive/${PV}.tar.gz;name=tar \
         file://main.c.patch \
         file://restrict_users.c.patch \
         file://firejail.h.patch \
        "

SRC_URI[tar.md5sum] = "eca8c30edd7c278960f0a422e5bda0d4"
SRC_URI[tar.sha256sum] ="1205b76606fd259cfbd31c5fd167c814e2096e3aebf83c84a46fb51fd8c55c67"


PACKAGES =+ "${PN}-bash-completion"

FILES_${PN}-bash-completion = "${datadir}/bash-completion"

do_install_append() {
  rm -rf ${D}/etc/firejail/*
}

inherit autotools-brokensep pkgconfig gettext
