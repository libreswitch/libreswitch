SUMMARY = "mcelog daemon accounts memory and some other errors in various ways."
DESCRIPTION = "mcelog is required by both 32bit x86 Linux kernels (since 2.6.30) \
and 64bit Linux kernels (since early 2.6 kernel releases) to log machine checks \
and should run on all Linux systems that need error handling."
HOMEPAGE = "http://mcelog.org/"
SECTION = "System Environment/Base"

SRC_URI = "git://git.kernel.org/pub/scm/utils/cpu/mce/mcelog.git \
    file://mcelog-debash.patch \
    file://mcelog.service \
    file://run-ptest \
"

SRCREV = "8cbffb62729ba54800a0110420aaeb7d4217fdb7"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://README;md5=3d12dd2a10bdd22379cc4c0fc6949a88"

S = "${WORKDIR}/git"

inherit autotools-brokensep ptest systemd

COMPATIBLE_HOST = '(x86_64.*|i.86.*)-linux'

SYSTEMD_SERVICE_${PN} = "mcelog.service"
#SYSTEMD_AUTO_ENABLE = "disable"

do_install_append() {
    # install systemd service file
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/mcelog.service ${D}${systemd_unitdir}/system
    sed -i -e 's,@BASE_BINDIR@,${base_bindir},g' \
        -e 's,@SYSCONFDIR@,${sysconfdir},g' \
        -e 's,@SBINDIR@,${sbindir},g' \
        ${D}${systemd_unitdir}/system/mcelog.service

    # install cron job
    # uncomment if cron job required
    #install -d ${D}${sysconfdir}/cron.hourly
    #install -m 0755 ${S}/mcelog.cron ${D}${sysconfdir}/cron.hourly/
    #sed -i 's/bash/sh/' ${D}${sysconfdir}/cron.hourly/mcelog.cron
}

do_install_ptest() {
    install -d ${D}${PTEST_PATH}
    cp -r ${S}/tests ${S}/input ${D}${PTEST_PATH}
    sed -i 's#../../mcelog#mcelog#' ${D}${PTEST_PATH}/tests/test
}

RDEPENDS_${PN}-ptest += "${PN} make bash mce-inject"
