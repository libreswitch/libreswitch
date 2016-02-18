# Copyright (C) 2016 Hewlett Packard Enterprise Development LP

PR_append = "_genericx86_64"

# Disable audit service on containers, since we can't count on the kernel support for it
SYSTEMD_SERVICE_${PN} = ""
FILES_${PN} += "${systemd_unitdir}/system/auditd.service"
