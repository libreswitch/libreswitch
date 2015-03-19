SUMMARY = "Valgrind memory debugger and instrumentation framework"
HOMEPAGE = "http://valgrind.org/"
BUGTRACKER = "http://valgrind.org/support/bug_reports.html"
LICENSE = "GPLv2 & GPLv2+ & BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=c46082167a314d785d012a244748d803 \
                    file://include/pub_tool_basics.h;beginline=1;endline=29;md5=e7071929a50d4b0fc27a3014b315b0f7 \
                    file://include/valgrind.h;beginline=1;endline=56;md5=92df8a1bde56fe2af70931ff55f6622f \
                    file://COPYING.DOCS;md5=8fdeb5abdb235a08e76835f8f3260215"

X11DEPENDS = "virtual/libx11"
DEPENDS = "${@bb.utils.contains('DISTRO_FEATURES', 'x11', '${X11DEPENDS}', '', d)}"
PR = "r8_magma"

# We break down valgrind in smaller packages so that we can pull in only the 
# tools that we really need. This is useful in RAM-bound file systems

PACKAGES = " ${PN}-staticdev \
  ${PN}-nulgrind ${PN}-cachegrind ${PN}-memcheck ${PN}-massif \
  ${PN}-helgrind ${PN}-exp-sgcheck ${PN}-exp-dhat ${PN}-drd ${PN}-lackey \
  ${PN}-exp-bbv ${PN}-callgrind \
  ${PN} ${PN}-dbg ${PN}-dev ${PN}-doc ${PN}-locale \
" 

FILES_${PN}-nulgrind = "/usr/lib/valgrind/*none*"
FILES_${PN}-cachegrind = "/usr/lib/valgrind/*cachegrind*"
FILES_${PN}-memcheck = "/usr/lib/valgrind/*memcheck*"
FILES_${PN}-massif = "/usr/lib/valgrind/*massif*"
FILES_${PN}-helgrind = "/usr/lib/valgrind/*helgrind*"
FILES_${PN}-exp-sgcheck = "/usr/lib/valgrind/*exp-sgcheck*"
FILES_${PN}-exp-dhat = "/usr/lib/valgrind/*exp-dhat*"
FILES_${PN}-drd = "/usr/lib/valgrind/*drd*"
FILES_${PN}-lackey = "/usr/lib/valgrind/*lackey*"
FILES_${PN}-exp-bbv = "/usr/lib/valgrind/*exp-bbv*"
FILES_${PN}-callgrind = "/usr/lib/valgrind/*callgrind*"

SRC_URI = "http://www.valgrind.org/downloads/valgrind-${PV}.tar.bz2 \
           file://fixed-perl-path.patch \
           file://Added-support-for-PPC-instructions-mfatbu-mfatbl.patch \
           file://sepbuildfix.patch \
           file://glibc-2.20.patch \
           file://force-nostabs.patch \
           file://remove-arm-variant-specific.patch \
           file://remove-ppc-tests-failing-build.patch \
           file://add-ptest.patch \
           file://run-ptest \
           file://arm-support-vmd-instructions-without-neon-vfp.patch \  
          "

SRC_URI[md5sum] = "7c311a72a20388aceced1aa5573ce970"
SRC_URI[sha256sum] = "03047f82dfc6985a4c7d9d2700e17bc05f5e1a0ca6ad902e5d6c81aeb720edc9"

COMPATIBLE_HOST = '(i.86|x86_64|powerpc|powerpc64).*-linux'
COMPATIBLE_HOST_armv7a = 'arm.*-linux'

inherit autotools ptest

EXTRA_OECONF = "--enable-tls --without-mpicc"
EXTRA_OECONF_armv7a = "--enable-tls -host=armv7-none-linux-gnueabi --without-mpicc"
EXTRA_OEMAKE = "-w"
PARALLEL_MAKE = ""

do_install_append () {
    install -m 644 ${B}/default.supp ${D}/${libdir}/valgrind/
}

RDEPENDS_${PN} += "perl"

FILES_${PN}-dbg += "${libdir}/${PN}/*/.debug/*"

# valgrind needs debug information for ld.so at runtime in order to
# redirect functions like strlen.
RRECOMMENDS_${PN} += "${TCLIBC}-dbg"

RDEPENDS_${PN}-ptest += " sed perl glibc-utils"

do_compile_ptest() {
    oe_runmake check
}


do_install_ptest() {
    chmod +x ${B}/tests/vg_regtest

    # The test application binaries are not automatically installed.
    # Grab them from the build directory.
    #
    # The regression tests require scripts and data files that are not
    # copied to the build directory.  They must be copied from the
    # source directory. 
    saved_dir=$PWD
    for parent_dir in ${S} ${B} ; do
        cd $parent_dir

        # exclude shell or the package won't install
        rm -rf none/tests/shell* 2>/dev/null

        subdirs="tests cachegrind/tests callgrind/tests drd/tests helgrind/tests massif/tests memcheck/tests none/tests"

        # Get the vg test scripts, filters, and expected files
        for dir in $subdirs ; do
            find $dir | cpio -pvdu ${D}${PTEST_PATH}
        done
        cd $saved_dir
    done

    # clean out build artifacts before building the rpm
    find ${D}${PTEST_PATH} \
         \( -name "Makefile*" \
        -o -name "*.o" \
        -o -name "*.c" \
        -o -name "*.S" \
        -o -name "*.h" \) \
        -exec rm {} \;

    # needed by massif tests
    cp ${B}/massif/ms_print ${D}${PTEST_PATH}/massif/ms_print

    # handle multilib
    sed -i s:@libdir@:${libdir}:g ${D}${PTEST_PATH}/run-ptest
}

