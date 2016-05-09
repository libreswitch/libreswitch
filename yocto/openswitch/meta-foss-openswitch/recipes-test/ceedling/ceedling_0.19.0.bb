SUMMARY = "Ceedling build/unit test system"
DESCRIPTION = "Ceedling is a build system for C projects that is something of \
    an extension around Ruby's Rake (make-ish) build system. Ceedling also makes \
    TDD (Test-Driven Development) in C a breeze by integrating CMock, Unity, and \
    CException -- three other awesome open-source projects you can't live without \
    if you're creating awesomeness in the C language. Ceedling is also extensible \
    with a handy plugin mechanism"
HOMEPAGE = "http://www.throwtheswitch.org/ceedling/"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://license.txt;md5=b918df9f90e73113285176ab99998557"

DEPENDS += "thor-native"

BRANCH = "master"
SRC_URI = " \
   git://github.com/ThrowTheSwitch/Ceedling.git;name=ceedling; \
   git://github.com/ThrowTheSwitch/CException.git;name=cexception;destsuffix=git/vendor/c_exception \
   git://github.com/ThrowTheSwitch/CMock.git;name=cmock;destsuffix=git/vendor/cmock \
   git://github.com/ThrowTheSwitch/Unity.git;name=unity;destsuffix=git/vendor/unity \
   file://0001-gcov-and-valgrind-support-from-David-Thai.patch;patch=1 \
   file://0001-Hacked-to-shuffle.patch;patch=1;patchdir=vendor/unity \
   file://0001-Handle-structs-as-parameters-and-functions-with-nest.patch;patch=1;patchdir=vendor/cmock \
    "

SRCREV_ceedling = "0a04043fc920068633100a91c7ee81e2eb9d29b3"
SRCREV_cexception = "ea0c4352f8912dcadac1c328901c51190084eb7b"
SRCREV_cmock = "581642e08c6d2c71f63e6021f97c94829c0098b6"
SRCREV_unity = "7943c766b993c9a84e1f6661d2d2427f6f2df9d0"

S = "${WORKDIR}/git"

inherit ruby

BBCLASSEXTEND = "native"
