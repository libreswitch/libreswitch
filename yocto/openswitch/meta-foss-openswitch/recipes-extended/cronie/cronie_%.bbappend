# Copyright (C) 2015 Hewlett Packard Enterprise Development LP
PR_append = "_openswitch"

EXTRA_OECONF += "--with-inotify"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://cron.hourly/log_rotate \
           "
do_install_append () {
    install -m 0755 ${WORKDIR}/cron.hourly/log_rotate ${D}${sysconfdir}/cron.hourly/
}
