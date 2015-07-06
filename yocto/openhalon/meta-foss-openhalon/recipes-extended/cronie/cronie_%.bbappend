# Copyright 201 Hewlett-Packard Development Company, L.P. 
PR_append = "_openhalon"

EXTRA_OECONF += "--with-inotify"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://cron.hourly/log_rotate \
           "
do_install_append () {
    install -m 0755 ${WORKDIR}/cron.hourly/log_rotate ${D}${sysconfdir}/cron.hourly/
}
