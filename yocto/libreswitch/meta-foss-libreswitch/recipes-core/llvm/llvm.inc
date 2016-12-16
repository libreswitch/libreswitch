DESCRIPTION = "The Low Level Virtual Machine"
HOMEPAGE = "http://llvm.org"

# 3-clause BSD-like
# University of Illinois/NCSA Open Source License
LICENSE = "NCSA"

DEPENDS = "libffi libxml2-native"

inherit cmake pkgconfig pythonnative

SRC_URI = " \
    http://llvm.org/releases/${PV}/llvm-${PV}.src.tar.xz;name=llvm \
    http://llvm.org/releases/${PV}/cfe-${PV}.src.tar.xz;name=clang;subdir=llvm-${PV}.src/tools/ \
"

S = "${WORKDIR}/llvm-${PV}.src"

def map_llvm_arch(a, d):
    import re

    if   re.match('^x86.64$', a):  return 'X86'
    elif re.match('^arm$', a)    : return 'ARM'
    elif re.match('^aarch64$', a): return 'AArch64'
    elif re.match('^powerpc$', a): return 'PowerPC'
    else:
        bb.error("cannot map '%s' to a LLVM architecture" % a)


EXTRA_OECMAKE = "\
  -DLLVM_INCLUDE_TESTS=OFF -DCLANG_INCLUDE_TESTS=OFF -DLLVM_INCLUDE_GO_TESTS=OFF \
  -DBUILD_SHARED_LIBS=ON \
  -DLLVM_TARGETS_TO_BUILD='${@map_llvm_arch(d.getVar('TARGET_ARCH', True), d)};BPF' \
"

EXTRA_OECMAKE_append_class-target = "\
  -DCROSS_TOOLCHAIN_FLAGS_NATIVE='-DCMAKE_TOOLCHAIN_FILE=${WORKDIR}/native_toolchain.cmake' \
"

do_generate_native_toolchain_file() {
        cat > ${WORKDIR}/native_toolchain.cmake <<EOF
set( CMAKE_SYSTEM_NAME `echo ${BUILD_OS} | sed -e 's/^./\u&/' -e 's/^\(Linux\).*/\1/'` )
set( CMAKE_SYSTEM_PROCESSOR ${BUILD_ARCH} )
set( CMAKE_C_COMPILER ${BUILD_CC} )
set( CMAKE_CXX_COMPILER ${BUILD_CXX} )
set( CMAKE_ASM_COMPILER ${BUILD_AS} )
set( CMAKE_AR ${BUILD_AR} CACHE FILEPATH "Archiver" )
set( CMAKE_C_FLAGS "${BUILD_CC_ARCH} ${BUILD_CFLAGS}" CACHE STRING "CFLAGS" )
set( CMAKE_CXX_FLAGS "${BUILD_CC_ARCH} ${BUILD_CXXFLAGS}" CACHE STRING "CXXFLAGS" )
set( CMAKE_ASM_FLAGS "${BUILD_CC_ARCH} ${BUILD_CFLAGS}" CACHE STRING "ASM FLAGS" )
set( CMAKE_C_FLAGS_RELEASE "${SELECTED_OPTIMIZATION} ${BUILD_CFLAGS} -DNDEBUG" CACHE STRING "CFLAGS for release" )
set( CMAKE_CXX_FLAGS_RELEASE "${SELECTED_OPTIMIZATION} ${BUILD_CXXFLAGS} -DNDEBUG" CACHE STRING "CXXFLAGS for release" )
set( CMAKE_ASM_FLAGS_RELEASE "${SELECTED_OPTIMIZATION} ${BUILD_CFLAGS} -DNDEBUG" CACHE STRING "ASM FLAGS for release" )
set( CMAKE_C_LINK_FLAGS "${BUILD_CC_ARCH} ${BUILD_CPPFLAGS} ${BUILD_LDFLAGS}" CACHE STRING "LDFLAGS" )
set( CMAKE_CXX_LINK_FLAGS "${BUILD_CC_ARCH} ${BUILD_CXXFLAGS} ${BUILD_LDFLAGS}" CACHE STRING "LDFLAGS" )

# only search in the paths provided so cmake doesnt pick
# up libraries and tools from the native build machine
set( CMAKE_FIND_ROOT_PATH ${STAGING_DIR_HOST} ${STAGING_DIR_NATIVE} ${CROSS_DIR} ${OECMAKE_PERLNATIVE_DIR} ${OECMAKE_EXTRA_ROOT_PATH} ${EXTERNAL_TOOLCHAIN})
set( CMAKE_FIND_ROOT_PATH_MODE_PACKAGE ONLY )
set( CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY )
set( CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY )

# Use native cmake modules
set( CMAKE_MODULE_PATH ${STAGING_DATADIR}/cmake/Modules/ )

# add for non /usr/lib libdir, e.g. /usr/lib64
set( CMAKE_LIBRARY_PATH ${libdir} ${base_libdir})

EOF
}

addtask generate_native_toolchain_file after do_patch before do_configure

do_configure_prepend_class-native() {
    # Use host paths for native tools
    sed -i -e '/CMAKE_FIND_ROOT_PATH_MODE/d' ${WORKDIR}/toolchain.cmake
}

do_configure_prepend() {
    cd ${S}/tools/
    if [ -d cfe-${PV}.src ] ; then
        mv cfe-${PV}.src clang
    fi
}

do_compile_prepend_class-target() {
    # Fix paths in llvm-config
    sed -i "s|sys::path::parent_path(CurrentPath))\.str()|sys::path::parent_path(sys::path::parent_path(CurrentPath))).str()|g" ${S}/tools/llvm-config/llvm-config.cpp

    # Fix the hardcoded libdir in llvm-config
    sed -i 's:/lib\>:/${baselib}:g' ${S}/tools/llvm-config/llvm-config.cpp
}

do_compile() {
    oe_runmake
}

do_install_append_class-target() {
    # Disable checks on the native tools, since these should came from the native recipe
    sed -i -e 's/\(.*APPEND.*_IMPORT_CHECK_FILES_FOR_.*{_IMPORT_PREFIX}\/bin\/.*\)/#\1/' ${D}/usr/share/llvm/cmake/LLVMExports-noconfig.cmake
}

SYSROOT_PREPROCESS_FUNCS_append_class-target = " llvm_sysroot_preprocess"
SYSROOT_PREPROCESS_FUNCS_append_class-native = " llvm_native_sysroot_preprocess"

llvm_sysroot_preprocess() {
    install -d ${SYSROOT_DESTDIR}${bindir}
    cp ${B}/NATIVE/bin/llvm-config ${SYSROOT_DESTDIR}/${bindir}
    cp ${B}/NATIVE/bin/llvm-tblgen ${SYSROOT_DESTDIR}/${bindir}
}

llvm_native_sysroot_preprocess() {
    sysroot_stage_dir ${D}${STAGING_DIR_NATIVE}/usr/libexec ${SYSROOT_DESTDIR}${bindir}
}

PACKAGES_prepend = "clang clang-static-dev clang-static-analysis ${PN}-tools "
PACKAGES += "${PN}-bugpointpasses ${PN}-llvmhello ${PN}-liblto"

FILES_clang = "${bindir}/*clang* ${bindir}/sancov ${bindir}/c-index-test /usr/share/clang \
  ${libdir}/libclang*.so ${libdir}/libclang.so.?.? ${libdir}/clang/"
RDEPENDS_clang = "libxml2"
INSANE_SKIP_clang = "dev-so"
FILES_clang-static-analysis = "/usr/share/scan-build/* /usr/share/scan-view/* /usr/libexec/*analyzer"
FILES_clang-staticdev = "${libdir}/libclang*.a"

FILES_${PN} += "${libdir}/libLLVM*.so"
FILES_${PN}-tools = "${bindir}"
FILES_${PN}-dev = "/usr/share/llvm/* ${includedir}"
FILES_${PN}-staticdev = "${libdir}/libLLVM*.a"

FILES_${PN}-bugpointpasses = "${libdir}/BugpointPasses.so"
FILES_${PN}-llvmhello = "${libdir}/LLVMHello.so"
FILES_${PN}-liblto = "${libdir}/libLTO.so"

BBCLASSEXTEND = "native nativesdk"
