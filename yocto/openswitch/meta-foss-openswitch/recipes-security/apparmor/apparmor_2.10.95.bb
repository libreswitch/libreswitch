SUMMARY = "AppArmor user-space tools"

DESCRIPTION = "AppArmor is MAC style security extension for the Linux kernel."

HOMEPAGE = "http://wiki.apparmor.net/"

SECTION = "base"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM ="file://LICENSE;md5=fd57a4b0bc782d7b80fd431f10bbf9d0"

SRC_URI = "https://launchpad.net/apparmor/2.11/2.11.beta1/+download/apparmor-2.10.95.tar.gz;name=tar \
  file://debugedit.patch \
  file://bison.patch \
  file://apparmor_load.sh \
  file://apparmor_unload.sh \
  file://apparmor.service \
"
SRC_URI[tar.md5sum] = "71a13b9d6ae0bca4f5375984df1a51e7"
SRC_URI[tar.sha256sum] = "3f659a599718f4a5e2a33140916715f574a5cb3634a6b9ed6d29f7b0617e4d1a"

DEPENDS = "bison flex"
RDEPENDS_${PN} = "perl bash python ${PN}-python ${PN}-perl"
RDEPENDS_${PN}-python = "python-terminal python-pydoc"
RDEPENDS_${PN}-perl = "perl-module-getopt-long perl-module-base perl-module-posix \
  perl-module-file-basename perl-module-time-local"
RRECOMMENDS_${PN} = "${PN}-profiles"

PACKAGES = "${PN}-dbg ${PN}-python ${PN}-perl ${PN}-profiles ${PN} ${PN}-doc \
  ${PN}-dev ${PN}-staticdev"

FILES_${PN}-dbg += "/usr/lib/python2.7/site-packages/*/.debug"
FILES_${PN}-python = "/usr/lib/python*"
FILES_${PN}-perl = "/usr/lib/perl"
FILES_${PN}-staticdev = "/usr/lib/*.a"
FILES_${PN}-profiles = "/etc/apparmor.d"

SYSTEMD_SERVICE_${PN} = "apparmor.service"

inherit systemd pythonnative cpan-base perlnative

B = "${S}"

# These are needed for --with-python
export STAGING_INCDIR
export STAGING_LIBDIR
export BUILD_SYS
export HOST_SYS

# These are needed for --with-perl
# Env var which tells perl if it should use host (no) or target (yes) settings
export PERLCONFIGTARGET = "${@is_target(d)}"

# Env var which tells perl where the perl include files are
export PERL_INC = "${STAGING_LIBDIR}${PERL_OWN_DIR}/perl/${@get_perl_version(d)}/CORE"
export PERL_LIB = "${STAGING_LIBDIR}${PERL_OWN_DIR}/perl/${@get_perl_version(d)}"
export PERL_ARCHLIB = "${STAGING_LIBDIR}${PERL_OWN_DIR}/perl/${@get_perl_version(d)}"
export PERLHOSTLIB = "${STAGING_LIBDIR_NATIVE}/perl-native/perl/${@get_perl_version(d)}/"

do_configure() {
  cd libraries/libapparmor/
  # AppArmor's configure refuses to install --with-perl if cross-compiling
  # for no good reason. Disable that check.
  sed -i 's/test "$cross_compiling" = yes/false/' ./configure

  PERL="${PERL}" PYTHON="${PYTHON}" ./configure \
      --build=${BUILD_SYS} \
		  --host=${HOST_SYS} \
		  --target=${TARGET_SYS} \
		  --prefix=${prefix} \
		  --libdir=${libdir} \
		  --mandir=${mandir} \
		  --includedir=${includedir} \
      --with-perl --with-python
}

do_compile() {
  STAGING_INCDIR=${STAGING_INCDIR} \
  STAGING_LIBDIR=${STAGING_LIBDIR} \
  BUILD_SYS=${BUILD_SYS} HOST_SYS=${HOST_SYS} \
  PYTHON="${PYTHON}" ${MAKE} -C ${S}/libraries/libapparmor
  ${MAKE} -C ${S}/parser apparmor_parser manpages
  ${MAKE} -C ${S}/binutils
  ${MAKE} -C ${S}/utils
  ${MAKE} -C ${S}/profiles
}

do_install() {
  ${MAKE} "DESTDIR=${D}" -C ${S}/libraries/libapparmor install
  ${MAKE} "DESTDIR=${D}" -C ${S}/parser install
  ${MAKE} "DESTDIR=${D}" -C ${S}/binutils install
  ${MAKE} "DESTDIR=${D}" -C ${S}/utils install

  # We do not need the init helpers as we're using systemd
  rm -fr ${D}/lib/
  install -d ${D}${systemd_unitdir}/system
  install -m 644 ${WORKDIR}/*.service ${D}${systemd_unitdir}/system
  install -m 755 ${WORKDIR}/apparmor_load.sh ${D}${bindir}/apparmor_load
  install -m 755 ${WORKDIR}/apparmor_unload.sh ${D}${bindir}/apparmor_unload

  #
  # Install profiles
  # TODO(bluecmd): We will probably want to move these to ops-apparmor-profiles
  # or something like that. When we inevitably start to modify these it will be
  # a mess otherwise.
  #
  install -d ${D}/etc/apparmor.d/abstractions
  # Cherry pick profiles we care about
  cd ${S}/profiles/apparmor.d
  cp -Rp abstractions/apparmor_api ${D}/etc/apparmor.d/abstractions

  # Remove external service includes that we do not need
  sed -i '/#include/d' abstractions/authentication abstractions/nameservice

  for i in authentication base bash consoles openssl nameservice \
    perl private-files* python ssl_* web-data wutmp
  do
    cp -Rp abstractions/$i ${D}/etc/apparmor.d/abstractions
  done
  # Remove dovecot and XDG things
  rm -f tunables/dovecot
  rm -fr tunables/xdg-*
  sed -i '/xdg/d' tunables/global
  # Install all other tunables
  cp -Rp tunables ${D}/etc/apparmor.d/
}
