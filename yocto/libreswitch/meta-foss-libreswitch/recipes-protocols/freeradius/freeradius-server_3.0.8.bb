SUMMARY = "FreeRADIUS Server"
HOMEPAGE = "http://wiki.freeradius.org"
LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=8271badacbbc8d1e3c62027d15cd176d"

SRC_URI = "ftp://ftp.freeradius.org/pub/freeradius/${PN}-${PV}.tar.bz2 \
           file://0001-Fix-cross-compile.patch \
           "

SRC_URI[md5sum] = "59427bd3cc2ebd5ad62ee9dcc7d943c5"
SRC_URI[sha256sum] = "b89721c609e5a106936112fe8122e470f02a5197bb614e202d2c386f4821d902"

DEPENDS = "talloc libpcre libpcap openssl"

EXTRA_OECONF += "--disable-static --with-pcre-include-dir=${STAGING_INCDIR} \
"

CACHED_CONFIGUREVARS = "ax_cv_cc_builtin_choose_expr=yes \
 ax_cv_cc_builtin_types_compatible_p=yes ax_cv_cc_builtin_bswap64=yes \
 ax_cv_cc_bounded_attribute=yes \
 ac_cv_lib_collectdclient_lcc_connect=no \
 ac_cv_lib_execinfo_backtrace_symbols=yes \
"

do_configure_append() {
    cd ${B}
    make reconfig

    parentdir=`pwd`
    mysubdirs="$mysubdirs `find src/modules/ -name configure.ac -print | sed 's%/configure.ac%%'`"
    mysubdirs=`echo $mysubdirs`

    for F in $mysubdirs
    do
            echo "Configuring in $F..."
            (cd $F && grep "^AC_CONFIG_HEADER" configure.ac > /dev/null || exit 0; autoheader -I$parentdir)
            (cd $F && autoconf -I$parentdir)
    done
}

inherit autotools-brokensep systemd
