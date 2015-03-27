SUMMARY = "Target Communication Framework for the Eclipse IDE"
HOMEPAGE = "http://wiki.eclipse.org/TCF"
BUGTRACKER = "https://bugs.eclipse.org/bugs/"

LICENSE = "EPL-1.0 | EDL-1.0"
LIC_FILES_CHKSUM = "file://edl-v10.html;md5=522a390a83dc186513f0500543ad3679"

SRCREV = "0b9bcb30b8295eafa97fa3c670486db7ae834424"
#SRCREV_powerpc = "1c009fdbe262ffe31967b6cc860dce0f6fcdf5f1"
PV = "${SRCREV}"
PR = "r3"

SRC_URI = "git://github.com/eclipse/tcf.agent.git;protocol=https;branch=1.2_luna \
           file://fix_ranlib.patch \
           file://fix_tcf-agent.init.patch \
           file://tcf-agent.service \
          "

DEPENDS = "util-linux openssl"
RDEPENDS_${PN} = "bash"

S = "${WORKDIR}/git/agent"

inherit update-rc.d systemd

SYSTEMD_SERVICE_${PN} = "tcf-agent.service"
SYSTEMD_AUTO_ENABLE = "disable"

INITSCRIPT_NAME = "tcf-agent"
INITSCRIPT_PARAMS = "start 99 3 5 . stop 20 0 1 2 6 ."

# mangling needed for make
MAKE_ARCH = "`echo ${TARGET_ARCH} | sed s,i.86,i686,`"
MAKE_OS = "`echo ${TARGET_OS} | sed s,^linux.*,GNU/Linux,`"

EXTRA_OEMAKE = "MACHINE=${MAKE_ARCH} OPSYS=${MAKE_OS} 'CC=${CC}' 'AR=${AR}'"

do_compile() {
	oe_runmake
}

do_install() {
	oe_runmake install INSTALLROOT=${D}
        if ${@base_contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        	install -d ${D}${systemd_unitdir}/system
        	install -m 0644 ${WORKDIR}/tcf-agent.service ${D}${systemd_unitdir}/system/tcf-agent.service
    	fi
}

