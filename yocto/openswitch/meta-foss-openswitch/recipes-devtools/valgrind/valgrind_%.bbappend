# Copyright (C) 2015 Hewlett Packard Enterprise Development LP
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
        file://arm-support-vmd-instructions-without-neon-vfp.patch \
"

PR_append = "_openswitch"

# We break down valgrind in smaller packages so that we can pull in only the
# tools that we really need. This is useful in RAM-bound file systems

PACKAGES = " ${PN}-staticdev \
  ${PN}-nulgrind ${PN}-cachegrind ${PN}-memcheck ${PN}-massif \
  ${PN}-helgrind ${PN}-exp-sgcheck ${PN}-exp-dhat ${PN}-drd ${PN}-lackey \
  ${PN}-exp-bbv ${PN}-callgrind \
  ${PN} ${PN}-dbg ${PN}-dev ${PN}-doc ${PN}-locale \
"

FILES_${PN}-nulgrind = "/usr/lib/valgrind/*none*"
FILES_${PN}-cachegrind = "/usr/lib/valgrind/*cachegrind*"
FILES_${PN}-memcheck = "/usr/lib/valgrind/*memcheck*"
FILES_${PN}-massif = "/usr/lib/valgrind/*massif*"
FILES_${PN}-helgrind = "/usr/lib/valgrind/*helgrind*"
FILES_${PN}-exp-sgcheck = "/usr/lib/valgrind/*exp-sgcheck*"
FILES_${PN}-exp-dhat = "/usr/lib/valgrind/*exp-dhat*"
FILES_${PN}-drd = "/usr/lib/valgrind/*drd*"
FILES_${PN}-lackey = "/usr/lib/valgrind/*lackey*"
FILES_${PN}-exp-bbv = "/usr/lib/valgrind/*exp-bbv*"
FILES_${PN}-callgrind = "/usr/lib/valgrind/*callgrind*"
