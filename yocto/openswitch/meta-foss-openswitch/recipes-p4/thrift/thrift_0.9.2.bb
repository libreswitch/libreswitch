SUMMARY = "Apache Thrift - scalable cross-language service development"
DESCRIPTION = "The Apache Thrift software framework, for scalable \
cross-language services development, combines a software stack with a \
code generation engine to build services that work efficiently and \
seamlessly between C++, Java, Python, PHP, Ruby, Erlang, Perl, Haskell, \
C#, Cocoa, JavaScript, Node.js, Smalltalk, OCaml and Delphi and other \
languages."
HOMEPAGE = "https://thrift.apache.org/"
SECTION = "console/tools"
LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=b6b281b97b28a39ba00bd2bc2df39244"

SRC_URI = "http://archive.apache.org/dist/thrift/${PV}/thrift-${PV}.tar.gz \
"

SRC_URI[md5sum] = "89f63cc4d0100912f4a1f8a9dee63678"
SRC_URI[sha256sum] = "cef50d3934c41db5fa7724440cc6f10a732e7a77fe979b98c23ce45725349570"

inherit autotools pkgconfig autotools-brokensep

RDEPENDS_thrift = " \
	boost-filesystem \
	boost-program-options \
	boost-system \
	boost-test \
	glib-2.0 \
	libcrypto \
	libevent \
	libssl \
"

RDEPENDS_thrift-native = " \
	boost-native \
	libevent-native \
	openssl-native \
"

DEPENDS = " \
	boost \
"

EXTRA_OECONF = " \
	--with-cpp \
	--with-python=${PYTHON} \
	--with-c_glib \
	--with-go \
	--without-csharp \
	--without-d \
	--without-erlang \
	--without-haskell \
	--without-java  \
	--without-perl \
	--without-php \
	--without-php_extension \
	--without-qt4 \
	--without-ruby \
	--without-libevent \
	--without-zlib \
	--without-nodejs \
	--disable-tests \
	--disable-tutorial \
"

BBCLASSEXTEND = "native"

FILES_${PN}-dev_remove = "${libdir}/lib*.so"
FILES_${PN} += "${libdir}/lib${PN}-${PV}.so"
FILES_${PN}-dev += "${libdir}/lib${PN}.so"
FILES_${PN}-dev += "${libdir}/lib${PN}_c_glib.so"

do_configure() {
	export ac_cv_func_malloc_0_nonnull=yes
	export ac_cv_func_realloc_0_nonnull=yes
	oe_runconf
	# forcibly remove RPATH from libtool
	sed -i 's|^hardcode_libdir_flag_spec=.*|hardcode_libdir_flag_spec=""|g' *libtool
	sed -i 's|^runpath_var=LD_RUN_PATH|runpath_var=_NO_RPATH_|g' *libtool
}

do_install_append() {
	# Gross hack to deal with the fact that this file is getting
	# libstdc++.la hardcoded in it and programs trying to link
	# against libthrift by way of libthrift.la are failing because
	# libtool is picking up the hosts' libstdc++.la file.
	find "${D}" -name libthrift.la -print0 |
	xargs -r0 sed -r -i 's:\S*/\S+/libstdc\+\+\.la::'
}
