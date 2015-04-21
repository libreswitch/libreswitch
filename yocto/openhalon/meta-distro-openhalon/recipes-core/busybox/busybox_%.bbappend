# Additional BusyBox configuration changes for Halon.
# The entries here will override any entries in the base BusyBox recipe.

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
        file://halon-bb.cfg \
"

SYSTEMD_PACKAGES_remove = "${PN}-syslog"
