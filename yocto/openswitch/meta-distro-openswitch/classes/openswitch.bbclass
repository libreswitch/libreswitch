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
        if os.path.isfile(os.path.join(d.getVar('TOPDIR', True), 'devenv-coverage-enabled')):
            d.setVar("INHIBIT_PACKAGE_STRIP", "1")
            d.prependVarFlag('do_compile', 'prefuncs', "profile_compile_prefunc ")
            return "-fprofile-arcs -ftest-coverage"
    return ""

python profile_compile_prefunc() {
    bb.warn('Profiling enabled for package %s on the devenv' % (d.getVar('PN', True)))
}

# Debug flags is used by DEBUG_OPTIMIZATION that is used by SELECTED_OPTIMIZATION when DEBUG_BUILD is 1
DEBUG_FLAGS = "${@enable_devenv_profiling(d)}"

# Support for static analysis using HP Fortify
inherit python-dir

#-Dcom.fortify.sca.compilers.${HOST_PREFIX}gcc=com.fortify.sca.util.compilers.GccCompiler -Dcom.fortify.sca.compilers.${HOST_PREFIX}g++=com.fortify.sca.util.compilers.GppCompiler
FORTIFY_PARAMETERS = "-b ${PN} -python-path ${STAGING_DIR_TARGET}${PYTHON_SITEPACKAGES_DIR}  "

do_generate_sca_wrappers() {
    if which sourceanalyzer > /dev/null && [ -f "${TOPDIR}/devenv-sca-enabled" ] ; then
        sourceanalyzer -b ${PN} --clean
    else
        if [ -n "${EXTERNALSRC}" ] && [ -f "${TOPDIR}/devenv-sca-enabled" ] ; then
            echo
            echo
            echo "Unable to find the Fortify sourceanalyzer tool, can't build the code in the devenv since ops-devenv-fortify-sca is enabled"
            echo
            echo
            return 255
        fi
    fi

    for c in gcc g++ ar ld; do
        dir=${WORKDIR}/bin/${HOST_PREFIX}
        mkdir -p ${dir}
        ln -f -s ${STAGING_BINDIR_TOOLCHAIN}/${HOST_PREFIX}${c} ${dir}/${c}
        cat > ${WORKDIR}/fortify-${c} << EOF
if [ \${!#} = '--version' ] || [ \${!#} = '-v' ]; then
    ${dir}/${c} \${!#}
else
    sourceanalyzer ${FORTIFY_PARAMETERS} ${dir}/${c} \$@
fi
EOF
        chmod +x ${WORKDIR}/fortify-${c}
    done
}

addtask generate_sca_wrappers after do_patch before do_configure

def get_cmake_c_compiler(d):
    externalsrc = d.getVar('EXTERNALSRC', True)
    if externalsrc:
        if os.path.isfile(os.path.join(d.getVar('TOPDIR', True), 'devenv-sca-enabled')):
            return "${WORKDIR}/fortify-gcc"
    return "${CCACHE}${HOST_PREFIX}gcc"

def get_cmake_cxx_compiler(d):
    externalsrc = d.getVar('EXTERNALSRC', True)
    if externalsrc:
        if os.path.isfile(os.path.join(d.getVar('TOPDIR', True), 'devenv-sca-enabled')):
            return "${WORKDIR}/fortify-g++"
    return "${CCACHE}${HOST_PREFIX}g++"

export CC = "${@get_cmake_c_compiler(d)} ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
export CXX = "${@get_cmake_cxx_compiler(d)} ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
export FC = "${CCACHE}${HOST_PREFIX}gfortran ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
export CPP = "${CCACHE}${HOST_PREFIX}gcc -E${TOOLCHAIN_OPTIONS} ${HOST_CC_ARCH}"
OECMAKE_C_COMPILER = "${@get_cmake_c_compiler(d)}"
OECMAKE_CXX_COMPILER = "${@get_cmake_cxx_compiler(d)}"

# Do cmake builds in debug mode
EXTRA_OECMAKE+="-DCMAKE_BUILD_TYPE=Debug"
# Enable simulation flag for cmake-based projects
EXTRA_OECMAKE+="${@bb.utils.contains('MACHINE_FEATURES', 'ops-container', '-DPLATFORM_SIMULATION=ON', '',d)}"
# Provide cmake-based projects endianness information
EXTRA_OECMAKE+="${@base_conditional('SITEINFO_ENDIANNESS', 'le', '-DCPU_LITTLE_ENDIAN=ON', '-DCPU_BIG_ENDIAN=ON', d)}"

# Add debug directory for packages
PACKAGE_DEBUG_SPLIT_STYLE??="debug-file-directory"

# For debugging/development purposes on devtool
EXTERNALSRC_BUILD??="${S}/build"

DEPENDS += "gmock gtest"

inherit siteinfo
