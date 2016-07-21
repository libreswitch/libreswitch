SUMMARY = "nginx is an HTTP and reverse proxy server"
HOMEPAGE = "http://nginx.org/en/"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0bb58ed0dfd4f5dbece3b52aba79f023"

DEPENDS = "openssl zlib libpcre"
RDEPENDS_${PN} = "${PN}-config ops-certificate"
PACKAGES += "${PN}-config"

SRC_URI = "http://nginx.org/download/nginx-1.9.11.tar.gz \
           file://nginx-cross.patch \
           file://nginx.conf \
           file://nginx-errors.conf \
           file://nginx-security.conf \
           file://nginx-static.conf \
           file://nginx.service \
           file://dhparam.pem \
"

SRC_URI[md5sum] = "76eb5853a1190e0cfc691aa21c545de3"
SRC_URI[sha256sum] = "6a5c72f4afaf57a6db064bba0965d72335f127481c5d4e64ee8714e7b368a51f"

inherit autotools systemd

EXTRA_OECONF = "\
  --with-http_v2_module \
  --with-http_ssl_module \
  --without-http_fastcgi_module \
  --without-http_uwsgi_module \
  --without-http_scgi_module \
"

B = "${S}"

do_configure () {
  # TODO(bluecmd): The nginx configure script doesn't like cross compiling
  # so we need to help it by giving it the correct numbers. This will only
  # work for x86-64 platform and needs to be tuned for other platforms.
  ./configure \
    --crossbuild=Linux:${TUNE_ARCH} \
    --with-endian=${@base_conditional('SITEINFO_ENDIANNESS', 'le', 'little', 'big', d)} \
    --with-int=4 --with-long=8 --with-long-long=8 --with-ptr-size=8 \
    --with-sig-atomic-t=8 --with-size-t=8 --with-off-t=8 \
    --with-time-t=8 --with-sys-nerr=132 \
    --conf-path=${sysconfdir}/nginx/nginx.conf \
    --http-log-path=${localstatedir}/log/nginx/access.log \
    --error-log-path=${localstatedir}/log/nginx/error.log \
    --prefix=${prefix} --pid-path=${localstatedir}/run/nginx/nginx.pid \
    ${EXTRA_OECONF}
}

do_install () {
  mkdir -p ${D}${sbindir}/
  mkdir -p ${D}${sysconfdir}/nginx/conf.d/
  install -m 0755 ${B}/objs/nginx ${D}${sbindir}/
  install -m 0644 ${WORKDIR}/nginx.conf ${D}${sysconfdir}/nginx/
  install -m 0644 ${WORKDIR}/dhparam.pem ${D}${sysconfdir}/nginx/
  install -m 0644 ${WORKDIR}/nginx-*.conf ${D}${sysconfdir}/nginx/conf.d/
  mkdir -p ${D}${systemd_unitdir}/system
  install -m 0644 ${WORKDIR}/nginx.service ${D}${systemd_unitdir}/system/
}

FILES_${PN} = "${sbindir} ${systemd_unitdir}"
FILES_${PN}-config = "${sysconfdir}"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "nginx.service"
