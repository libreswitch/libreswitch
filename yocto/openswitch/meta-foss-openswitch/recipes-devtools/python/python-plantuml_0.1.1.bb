DESCRIPTION = "Plantuml is a library for generating UML diagrams from a simple text markup language"
HOMEPAGE = "http://pythonhosted.org/plantuml/"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=573a2b53821176a5d326712f7c27a1bb"

SRC_URI = "https://pypi.python.org/packages/56/f8/7e845ec6648afbfddd243064c9a82946857f99ef6e0e064c229c5137ee76/plantuml-0.1.1.tar.gz"
SRC_URI[md5sum] = "493fb00704bfc35f970b5e6e21a2d165"
SRC_URI[sha256sum] = "e8cb0d070a30dcade982a18e077279270aa9185b7ed4a0d291bb0e4442a0b7f3"
S = "${WORKDIR}/plantuml-0.1.1"

PYPI_PACKAGE_HASH = "7e845ec6648afbfddd243064c9a82946857f99ef6e0e064c229c5137ee76"

inherit setuptools

DEPENDS += " python-httplib2"

BBCLASSEXTEND = "native"
