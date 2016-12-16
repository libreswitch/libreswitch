# Copyright (C) 2016 Hewlett Packard Enterprise Development LP

# On generic X86 build (which runs on virtual platforms),
# serial consoles are absent. There is no need to run
# getty on those platforms.

# systemd 50-default.conf changes sysctl kernel.core_pattern .
# Removing 50-default.conf will ensure sysctl kernel.core_pattern will
# remain unchanged .

do_install_append() {
    /bin/rm -rf ${D}/${sysconfdir}/systemd/system/getty.target.wants/getty@tty1.service
    /bin/rm -rf ${D}/${sysconfdir}/systemd/system/getty.target.wants
    /bin/rm -rf ${D}/${systemd_unitdir}/system/getty@.service
    /bin/rm -f  ${D}${libdir}/sysctl.d/50-coredump.conf
}
