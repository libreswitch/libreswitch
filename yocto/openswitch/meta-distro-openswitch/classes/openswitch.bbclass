# OpenSwitch repos are fetched from git, so creating tarballs for them
# only ruins the origin remotes in the devevn
BB_GENERATE_MIRROR_TARBALLS = "0"

# Most of the code requires this flag right now, otherwise the
# structures in OVS get corrupted. This needs to be removed soon
CFLAGS += "-DHALON"
CFLAGS += "-DOPS"

# Do builds with debug mode by default
DEBUG_BUILD = "1"

# Enable profiling for devenv recipes (meaning they are in external src)
def enable_devenv_profiling(d):
    externalsrc = d.getVar('EXTERNALSRC', True)
    if externalsrc:
        return bb.utils.contains('EXTRA_IMAGE_FEATURES', 'ops-devenv-profiling', '-p', '',d)
    return ""

# Debug flags is used by DEBUG_OPTIMIZATION that is used by SELECTED_OPTIMIZATION when DEBUG_BUILD is 1
DEBUG_FLAGS = "${@enable_devenv_profiling(d)}"

# Do cmake builds in debug mode
EXTRA_OECMAKE+="-DCMAKE_BUILD_TYPE=Debug"
EXTRA_OECMAKE+="${@bb.utils.contains('MACHINE_FEATURES', 'ops-container', '-DPLATFORM_SIMULATION=ON', '',d)}"
EXTRA_OECMAKE+="${@base_conditional('SITEINFO_ENDIANNESS', 'le', '-DCPU_LITTLE_ENDIAN=ON', '-DCPU_BIG_ENDIAN=ON', d)}"

# Add debug directory for packages
PACKAGE_DEBUG_SPLIT_STYLE??="debug-file-directory"

# For debugging/development purposes on devtool
EXTERNALSRC_BUILD??="${S}/build"

inherit siteinfo
