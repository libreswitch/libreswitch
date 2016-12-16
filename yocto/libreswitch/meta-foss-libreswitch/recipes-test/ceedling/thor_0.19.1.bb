SUMMARY = "Thor is a toolkit for building powerful command-line interfaces"
DESCRIPTION = "Thor is a simple and efficient tool for building self-documenting \
    command line utilities. It removes the pain of parsing command line \
    options, writing "USAGE:" banners, and can also be used as an alternative \
    to the Rake build tool. The syntax is Rake-like, so it should be familiar \
    to most Rake users"
HOMEPAGE = "http://whatisthor.com/"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=690cce21f4e069148a52834e1ecf352e"

BRANCH = "master"
SRC_URI = " \
    git://github.com/erikhuda/thor.git;branch=${BRANCH} \
    "

SRCREV = "c74e1d34ef8b3270dcaa821fc1c7b38238929a27"

S = "${WORKDIR}/git"

inherit ruby

BBCLASSEXTEND = "native"
