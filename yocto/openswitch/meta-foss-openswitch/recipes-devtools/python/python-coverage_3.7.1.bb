SUMMARY = "Python code coverage library"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM ="file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI ="https://pypi.python.org/packages/09/4f/89b06c7fdc09687bca507dc411c342556ef9c5a3b26756137a4878ff19bf/coverage-3.7.1.tar.gz"
SRC_URI[md5sum] = "c47b36ceb17eaff3ecfab3bcd347d0df"
SRC_URI[sha256sum] = "d1aea1c4aa61b8366d6a42dd3650622fbf9c634ed24eaf7f379c8b970e5ed44e"

RDEPENDS_${PN} = "python-py"
DEPENDS_class-native = "python-py-native"

inherit pypi
