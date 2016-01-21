# Copyright (C) 2015 Hewlett Packard Enterprise Development LP
PR_append = "_openswitch"

EXTRA_OECONF += "--with-inotify"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://cron.hourly/ops-gen-logrotate \
            file://cron.daily/logrotate-ops \
           "
do_install_append () {
    install -m 0755 ${WORKDIR}/cron.hourly/ops-gen-logrotate ${D}${sysconfdir}/cron.hourly/
    install -m 0755 ${WORKDIR}/cron.daily/logrotate-ops ${D}${sysconfdir}/cron.daily/
}

# Enable Audit framework on OpenSwitch
PACKAGECONFIG += "audit"
