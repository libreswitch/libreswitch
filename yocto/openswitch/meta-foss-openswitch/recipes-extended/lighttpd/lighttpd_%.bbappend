# Copyright (C) 2015 Hewlett Packard Enterprise Development LP
PR_append = "_openswitch"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# Revert some changes from the base recipe
do_install_append() {
    rm -Rf ${D}/www ${D}${sysconfdir}/init.d

    #For FHS compliance, create symbolic links to /var/log and /var/tmp for logs and temporary data
    install -d ${D}/srv/www/
    ln -sf ${localstatedir}/log ${D}/srv/www/logs
    ln -sf ${localstatedir}/tmp ${D}/srv/www/var
}

FILES_${PN} += "/srv/www/*"
