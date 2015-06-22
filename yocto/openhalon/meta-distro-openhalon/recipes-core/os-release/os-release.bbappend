# Copyright 2015 Hewlett-Packard Development Company, L.P. 

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# Other valid fields: BUILD_ID ID_LIKE ANSI_COLOR CPE_NAME
#                     HOME_URL SUPPORT_URL BUG_REPORT_URL
OS_RELEASE_FIELDS = "ID ID_LIKE NAME VERSION VERSION_ID PRETTY_NAME HOME_URL BUILD_ID"

HOME_URL="https://wiki.openhalon.io"

def get_build_id(d):
    import os
    import pwd
    return pwd.getpwuid(os.getuid())[0]

BUILD_ID = "${@get_build_id(d)}"

include build_info.conf
