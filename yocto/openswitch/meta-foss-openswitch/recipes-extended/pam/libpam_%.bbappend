# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_openswitch"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://pam.d/rest \
            file://environment \
            file://pam.d/sshd-account-access \
            file://pam.d/sshd-session-access \
           "

do_install_append() {
      install -m 0644 ${WORKDIR}/pam.d/rest ${D}${sysconfdir}/pam.d/rest
      install -m 0777 ${WORKDIR}/environment ${D}${sysconfdir}/environment
      install -m 0644 ${WORKDIR}/pam.d/sshd-account-access ${D}${sysconfdir}/pam.d/sshd-account-access
      install -m 0644 ${WORKDIR}/pam.d/sshd-session-access ${D}${sysconfdir}/pam.d/sshd-session-access
      cp ${D}${sysconfdir}/pam.d/common-auth ${D}${sysconfdir}/pam.d/common-auth-access
      cp ${D}${sysconfdir}/pam.d/common-account ${D}${sysconfdir}/pam.d/common-account-access
      cp ${D}${sysconfdir}/pam.d/common-password ${D}${sysconfdir}/pam.d/common-password-access
      cp ${D}${sysconfdir}/pam.d/common-session ${D}${sysconfdir}/pam.d/common-session-access
}

# Enable Audit framework on OpenSwitch
PACKAGECONFIG += "${@bb.utils.contains('DISTRO_FEATURES', 'audit', 'audit', '', d)}"
