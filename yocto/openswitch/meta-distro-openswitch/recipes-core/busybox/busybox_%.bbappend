# Additional BusyBox configuration changes for OpenSwitch.
# The entries here will override any entries in the base BusyBox recipe.

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
        file://ops-bb.cfg \
"

SYSTEMD_PACKAGES_remove = "${PN}-syslog"
