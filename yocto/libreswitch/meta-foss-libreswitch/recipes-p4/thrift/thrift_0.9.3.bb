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

LIC_FILES_CHKSUM = "file://LICENSE;md5=e4ed21f679b2aafef26eac82ab0c2cbf"

SRC_URI = "http://archive.apache.org/dist/thrift/${PV}/thrift-${PV}.tar.gz \
  file://pkg-config-for-cmake.patch \
"

SRC_URI[md5sum] = "88d667a8ae870d5adeca8cb7d6795442"
SRC_URI[sha256sum] = "b0740a070ac09adde04d43e852ce4c320564a292f26521c46b78e0641564969e"

inherit cmake pkgconfig

DEPENDS = "boost libevent flex-native bison-native \
           openssl \
"

EXTRA_OECMAKE = " -DBUILD_TUTORIALS=OFF -DBUILD_TESTING=OFF -DBUILD_EXAMPLES=OFF \
  -DWITH_QT4=OFF -DWITH_QT5=OFF -DBUILD_PYTHON=OFF -DBUILD_C_GLIB=OFF \
"
EXTRA_OECMAKE_class-native = " -DBUILD_TUTORIALS=OFF -DBUILD_TESTING=OFF -DBUILD_EXAMPLES=OFF \
  -DWITH_QT4=OFF -DWITH_QT5=OFF -DBUILD_PYTHON=OFF -DBUILD_C_GLIB=OFF \
"

BBCLASSEXTEND = "native"
