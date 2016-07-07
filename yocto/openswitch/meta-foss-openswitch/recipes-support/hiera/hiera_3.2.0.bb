SUMMARY = "A simple pluggable Hierarchical Database"
HOMEPAGE = "https://projects.puppetlabs.com/projects/hiera"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=cb05cd62f0184c1950374f251caef471"

SRC_URI = " \
    https://downloads.puppetlabs.com/hiera/hiera-${PV}.tar.gz \
    file://add_hiera_gemspec.patch \
"
SRC_URI[md5sum] = "164af14bd4955e246329ec126cc96113"
SRC_URI[sha256sum] = "6d721b5b17775d3da53dbc6b75b1a60d30b39e1aa179d27b82457b8af47267cb"


inherit ruby

DEPENDS += " \
        ruby \
"

RUBY_INSTALL_GEMS = "hiera-${PV}.gem"
