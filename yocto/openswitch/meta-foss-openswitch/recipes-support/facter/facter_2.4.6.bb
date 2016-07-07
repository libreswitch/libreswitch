SUMMARY = "Facter gathers basic facts about nodes (systems)"
HOMEPAGE = "http://puppetlabs.com/facter"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ce69a88023d6f6ab282865ddef9f1e41"

SRC_URI = " \
    http://downloads.puppetlabs.com/facter/facter-${PV}.tar.gz \
    file://add_facter_gemspec.patch \
"
SRC_URI[md5sum] = "43fee9f5cc63abe9bb057586c744303b"
SRC_URI[sha256sum] = "81e25889c25f7905795281100f45695bde952057d08072b2790545618d2bfdf4"
inherit ruby

DEPENDS += " \
        ruby \
"

RUBY_INSTALL_GEMS = "facter-${PV}.gem"
