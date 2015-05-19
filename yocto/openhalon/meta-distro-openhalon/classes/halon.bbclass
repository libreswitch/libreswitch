# Halon repos are fetched from git, so creating tarballs for them
# only ruins the origin remotes in the devevn
BB_GENERATE_MIRROR_TARBALLS = "0"

# Most of the code requires the HALON flag right now, otherwise the
# structures in OVS get corrupted. This needs to be removed soon
CFLAGS += "-DHALON"

# Do cmake builds in debug mode
EXTRA_OECMAKE="-DCMAKE_BUILD_TYPE=Debug"

# Add debug directory for packages
PACKAGE_DEBUG_SPLIT_STYLE="debug-file-directory"

# For debugging/development purposes on devtool
EXTERNALSRC_BUILD="${S}/build"
