inherit setuptools3
require python-requests.inc

RDEPENDS_${PN} = "${PYTHON_PN}-email ${PYTHON_PN}-json ${PYTHON_PN}-netserver ${PYTHON_PN}-urllib3"
