SUMMARY = "Open source Puppet is a configuration management system"
HOMEPAGE = "https://puppetlabs.com/puppet/puppet-open-source"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7c9045ec00cc0d6b6e0e09ee811da4a0"

SRC_URI = " \
    https://downloads.puppetlabs.com/puppet/puppet-${PV}.tar.gz \
    file://add_puppet_gemspec.patch \
    file://puppet.conf \
"
SRC_URI[md5sum] = "baae92065c1d9d6636063513d30764b5"
SRC_URI[sha256sum] = "1450d8d877516b3a586523d05d048fe47ca0255e7a3b4226cf2e471421962493"

inherit ruby

DEPENDS += " \
        ruby \
        facter \
        hiera \
"

RDEPENDS_${PN} += " \
        ruby \
        facter \
        hiera \
        bash \
"

RUBY_INSTALL_GEMS = "puppet-${PV}.gem"

do_install_append() {
    install -d ${D}${sysconfdir}/puppet
    install -d ${D}${sysconfdir}/puppet/manifests
    install -d ${D}${sysconfdir}/puppet/modules

    install -m 655 ${S}/conf/auth.conf ${D}${sysconfdir}/puppet/
    install -m 655 ${S}/conf/fileserver.conf ${D}${sysconfdir}/puppet/
    install -m 655 ${WORKDIR}/puppet.conf ${D}${sysconfdir}/puppet/
}
