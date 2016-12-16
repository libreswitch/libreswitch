# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# Other valid fields: BUILD_ID ID_LIKE ANSI_COLOR CPE_NAME
#                     HOME_URL SUPPORT_URL BUG_REPORT_URL
OS_RELEASE_FIELDS = "ID ID_LIKE NAME VERSION VERSION_ID PRETTY_NAME HOME_URL BUILD_ID"

HOME_URL="http://www.libreswitch.net"

# Leting the code here if we want to go back to USER name approach
# def get_build_id(d):
#     import os
#     import pwd
#     return pwd.getpwuid(os.getuid())[0]
# BUILD_ID = "${@get_build_id(d)}"

# BUILD_ID for the developer's build.
BUILD_ID = "${MACHINE}-${DISTRO_SHORTNAME}-${DISTRO_VERSION}"
BUILD_ID .= "-${METADATA_BRANCH}-${DATETIME}-dev"

# BUILD_ID will be updated by the build process through build_info.conf.
include build_info.conf

# Customize our version of the file to avoid the quotes around the values
python do_compile () {
    import shutil
    with open(d.expand('${B}/os-release'), 'w') as f:
        for field in d.getVar('OS_RELEASE_FIELDS', True).split():
            value = d.getVar(field, True)
            if value and field == 'VERSION_ID':
                value = sanitise_version(value)
            if value:
                f.write('{0}={1}\n'.format(field, value))
}
