SUMMARY = "Tools for managing ARP packet filtering capabilities"
DESCRIPTION = "arptables is a user space tool, it is used to set up and maintain the tables of ARP rules in the Linux kernel. These rules inspect the ARP frames which they see. arptables is analogous to the iptables user space tool, but arptables is less complicated."
HOMEPAGE = "http://ebtables.netfilter.org/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://arptables.c;beginline=9;endline=21;md5=c5cffd09974558cf27d0f763df2a12dc\
                    file://arptables.8;beginline=9;endline=21;md5=311c8e4feca3f2fe17afa5c9dddc1810"
RDEPENDS_${PN} = "bash perl"
BP = "arptables-v${PV}"
S = "${WORKDIR}/${BP}"
EXTRA_OEMAKE = "\
        'CFLAGS=${CFLAGS} -I${S}/include -DARPTABLES_VERSION=\"${PV}\"' 'BUILDDIR=${S}'"

SRC_URI = "ftp://ftp.netfilter.org/pub/arptables/arptables-v${PV}.tar.gz"

SRC_URI[md5sum] = "c2e99c3aa9d78c9dfa30710ca3168182"
SRC_URI[sha256sum] = "277985e29ecd93bd759a58242cad0e02ba9d4a6e1b7795235e3b507661bc0049"

do_install () {
              install -d ${D}${mandir}
              install -d ${D}${sbindir}
              oe_runmake 'DESTDIR=${D}' install
              install -m 0644 ${D}/usr/local/man/man8/arptables.8 ${D}${mandir}
              install -m 0755 ${D}/usr/local/sbin/arptables* ${D}${sbindir}
              rm -rf ${D}/usr/local
}

inherit update-alternatives

PARALLEL_MAKE = ""
BBCLASSEXTEND = "native"
