# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_openswitch"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

do_install_append() {
      cp ${D}${sysconfdir}/pam.d/common-auth ${D}${sysconfdir}/pam.d/common-auth-access
      cp ${D}${sysconfdir}/pam.d/common-account ${D}${sysconfdir}/pam.d/common-account-access
      cp ${D}${sysconfdir}/pam.d/common-password ${D}${sysconfdir}/pam.d/common-password-access
      cp ${D}${sysconfdir}/pam.d/common-session ${D}${sysconfdir}/pam.d/common-session-access
}
