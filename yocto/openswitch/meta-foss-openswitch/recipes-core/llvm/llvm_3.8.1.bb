require llvm.inc

LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=4c0bc17c954e99fd547528d938832bfa"

DEPENDS += "zlib"
EXTRA_OECONF += "--enable-zlib"


SRC_URI[llvm.md5sum] = "538467e6028bbc9259b1e6e015d25845"
SRC_URI[llvm.sha256sum] = "6e82ce4adb54ff3afc18053d6981b6aed1406751b8742582ed50f04b5ab475f9"
SRC_URI[clang.md5sum] = "4ff2f8844a786edb0220f490f7896080"
SRC_URI[clang.sha256sum] = "4cd3836dfb4b88b597e075341cae86d61c63ce3963e45c7fe6a8bf59bb382cdf"

PACKAGE_DEBUG_SPLIT_STYLE = "debug-without-src"

# Fails to build with thumb-1 (qemuarm)
# | {standard input}: Assembler messages:
# | {standard input}:22: Error: selected processor does not support Thumb mode `stmdb sp!,{r0,r1,r2,r3,lr}'
# | {standard input}:31: Error: lo register required -- `ldmia sp!,{r0,r1,r2,r3,lr}'
# | {standard input}:32: Error: lo register required -- `ldr pc,[sp],#4'
# | make[3]: *** [/home/jenkins/oe/world/shr-core/tmp-glibc/work/armv5te-oe-linux-gnueabi/llvm3.3/3.3-r0/llvm-3.3.build/lib/Target/ARM/Release/ARMJITInfo.o] Error 1
ARM_INSTRUCTION_SET = "arm"
