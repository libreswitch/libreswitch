# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_openswitch"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://pam.d/rest \
            file://environment \
           "

do_install_append() {
      install -m 0644 ${WORKDIR}/pam.d/rest ${D}${sysconfdir}/pam.d/rest
      install -m 0777 ${WORKDIR}/environment ${D}${sysconfdir}/environment
      cp ${D}${sysconfdir}/pam.d/common-auth ${D}${sysconfdir}/pam.d/common-auth-access
      cp ${D}${sysconfdir}/pam.d/common-account ${D}${sysconfdir}/pam.d/common-account-access
      cp ${D}${sysconfdir}/pam.d/common-password ${D}${sysconfdir}/pam.d/common-password-access
      cp ${D}${sysconfdir}/pam.d/common-session ${D}${sysconfdir}/pam.d/common-session-access
}

# Enable Audit framework on OpenSwitch
PACKAGECONFIG += "audit"
