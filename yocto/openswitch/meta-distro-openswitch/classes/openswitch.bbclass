# OpenSwitch repos are fetched from git, so creating tarballs for them
# only ruins the origin remotes in the devevn
BB_GENERATE_MIRROR_TARBALLS = "0"

# Most of the code requires this flag right now, otherwise the
# structures in OVS get corrupted. This needs to be removed soon
CFLAGS += "-DHALON"
CFLAGS += "-DOPS"

# Do builds with debug mode by default if the code is on the devenv
def enable_devenv_debugging(d):
    externalsrc = d.getVar('EXTERNALSRC', True)
    if externalsrc:
        return "1"
    return "0"

DEBUG_BUILD = "${@enable_devenv_debugging(d)}"
TOPDIR[vardepvalue] = ""

# Enable profiling for devenv recipes (meaning they are in external src)
def enable_devenv_profiling(d):
    externalsrc = d.getVar('EXTERNALSRC', True)
    if externalsrc:
        if os.path.isfile(os.path.join(d.getVar('TOPDIR', True), 'devenv-coverage-enabled')):
            d.setVar("INHIBIT_PACKAGE_STRIP", "1")
            d.prependVarFlag('do_compile', 'prefuncs', "profile_compile_prefunc ")
            return "-fprofile-arcs -ftest-coverage -fdebug-prefix-map=${@d.getVar('S')}=/usr/src/debug/${BPN}/${PV}-${PR} "
        # Inside the devenv, we need symbol remmaping to get the split debug packages to work properly
        return "-fdebug-prefix-map=${@d.getVar('S')}=/usr/src/debug/${BPN}/${PV}-${PR}"
    return ""
enable_devenv_profiling[vardepsexclude] = "TOPDIR S"

python profile_compile_prefunc() {
    bb.warn('Profiling enabled for package %s on the devenv' % (d.getVar('PN', True)))
}

# Debug flags is used by DEBUG_OPTIMIZATION that is used by SELECTED_OPTIMIZATION when DEBUG_BUILD is 1
DEBUG_FLAGS += "${@enable_devenv_profiling(d)}"

# Support for static analysis using HP Fortify
inherit python-dir

#-Dcom.fortify.sca.compilers.${HOST_PREFIX}gcc=com.fortify.sca.util.compilers.GccCompiler -Dcom.fortify.sca.compilers.${HOST_PREFIX}g++=com.fortify.sca.util.compilers.GppCompiler
FORTIFY_PARAMETERS="-b ${PN} "
FORTIFY_PARAMETERS_FOR_PYTHON="-b ${PN} -python-path ${STAGING_DIR_TARGET}${PYTHON_SITEPACKAGES_DIR} "

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

    for c in gcc g++; do
        case $c in
            gcc) lang=c ;;
            g++) lang=c++ ;;
        esac
        dir=${WORKDIR}/bin/${HOST_PREFIX}
        mkdir -p ${dir}
        ln -f -s ${STAGING_BINDIR_TOOLCHAIN}/${HOST_PREFIX}${c} ${dir}/${c}
        cat > ${WORKDIR}/fortify-${c} << EOF
#!/bin/bash -x
if [ "\${!#}" = '--version' ] || [ "\${!#}" = '-v' ]; then
    ${dir}/${c} \${!#}
else
    inc_flags="\$(${dir}/${c} -E --sysroot=${STAGING_DIR_TARGET} -x ${lang} /dev/null -o /dev/null -v 2>&1 |
        sed -n '/search starts here/,/End of search list./p' |
        sed -n 's/^ / -I/p')"
    sourceanalyzer ${FORTIFY_PARAMETERS} ${dir}/${c} -nostdinc \${inc_flags} "\$@"
    ${dir}/${c} "\$@"
fi
EOF
        chmod +x ${WORKDIR}/fortify-${c}
    done
}
generate_sca_wrappers[vardepsexclude] = "TOPDIR"

addtask generate_sca_wrappers after do_patch before do_configure

def get_cmake_c_compiler(d):
    externalsrc = d.getVar('EXTERNALSRC', True)
    if externalsrc:
        if os.path.isfile(os.path.join(d.getVar('TOPDIR', True), 'devenv-sca-enabled')):
            return "${WORKDIR}/fortify-gcc"
    return "${CCACHE}${HOST_PREFIX}gcc"
get_cmake_c_compiler[vardepsexclude] = "TOPDIR"

def get_cmake_cxx_compiler(d):
    externalsrc = d.getVar('EXTERNALSRC', True)
    if externalsrc:
        if os.path.isfile(os.path.join(d.getVar('TOPDIR', True), 'devenv-sca-enabled')):
            return "${WORKDIR}/fortify-g++"
    return "${CCACHE}${HOST_PREFIX}g++"
get_cmake_cxx_compiler[vardepsexclude] = "TOPDIR"

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

do_unit_test() {
    :
}

do_gtest_harness() {
    #don't run unit tests for non-checked-out code
    if [ -f $1 ]; then
        do_coverage_setup
        #TODO: how to show this output to the developers? or at least hint them where the log is
        ${TARGET_INTERPRETER} --library-path ${TARGET_LIB_PATH} $1 ${GTEST_PARAMS}
        do_coverage_report
    fi
}

do_unit_test_base() {
    export COVERAGE_ENABLED_FILE=${TOPDIR}/devenv-coverage-enabled
    export TARGET_INTERPRETER=${STAGING_DIR_TARGET}/lib/ld-linux-x86-64.so.2
    export TARGET_LIB_PATH=${STAGING_DIR_TARGET}/lib:${STAGING_DIR_TARGET}/usr/lib
    export GTEST_PARAMS="--gtest_shuffle"

    do_coverage_vars_setup
    do_unit_test
}

do_coverage_vars_setup() {
    export MODULE_NAME="${PN}"
    export COVERAGE_BASE_DIR=$(pwd)
    export COVERAGE_REPORT_DIR="${COVERAGE_BASE_DIR}/coverage"
    #lcov based coverage support for C/C++ code
    export COVERAGE_EXCLUDE_PATTERN="c++* gmock* gtest* tests*"
    export LCOV="lcov --rc lcov_branch_coverage=1"
}

do_coverage_setup() {
    #Silently exit if coverage is not enabled
    if [ ! -f $COVERAGE_ENABLED_FILE ]; then
        return
    fi

    mkdir -p ${COVERAGE_REPORT_DIR}
    ${LCOV} --zerocounters --directory ${COVERAGE_REPORT_DIR}
}

do_coverage_report() {
    #Silently exit if coverage is not enabled
    if [ ! -f $COVERAGE_ENABLED_FILE ]; then
        return
    fi

    bbnote "Exclude pattern: ${COVERAGE_EXCLUDE_PATTERN}"
    ${LCOV} --directory ${COVERAGE_BASE_DIR} --capture --output-file ${COVERAGE_REPORT_DIR}/${MODULE_NAME}.info

    #lcov 1.10 bug workaround: when lcov is run on a folder "near" the info file, it doesn't remove files as expected
    cd ${COVERAGE_REPORT_DIR}
    ${LCOV} --remove ${MODULE_NAME}.info $COVERAGE_EXCLUDE_PATTERN --output-file ${MODULE_NAME}
    cd ${COVERAGE_BASE_DIR}

    #create HTML report
    mkdir -p ${COVERAGE_REPORT_DIR}/html
    genhtml ${COVERAGE_REPORT_DIR}/${MODULE_NAME} -o ${COVERAGE_REPORT_DIR}/html --demangle-cpp --branch-coverage
    #This is not visible on the developer console due to our version of open embedded
    bbplain "Unit Test coverage report is at ${COVERAGE_REPORT_DIR}/html/index.html"
}

#Workaround for bbplain not showing on the console when executed from a shell task
python do_show_coverage_report() {
    if os.path.isfile(os.path.join(d.getVar('TOPDIR', True), 'devenv-coverage-enabled')):
        bb.plain('Unit Test coverage report is at %s/coverage/html/index.html' % (d.getVar('B', True)))
}

# Enable unit tests and coverage for devenv recipes (meaning they are in external src)
python() {
    externalsrc = d.getVar('EXTERNALSRC', True)
    if externalsrc:
        d.appendVarFlag('do_compile', 'postfuncs', "do_unit_test_base do_show_coverage_report ")
}

inherit siteinfo
