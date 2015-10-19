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

DISTRO_HELP_LINK=http://www.openswitch.net

DISTRO_ARCHIVE_ADDRESS?=archive.openswitch.net
DISTRO_SSTATE_ADDRESS?=sstate.openswitch.net
DISTRO_FS_TARGET = openswitch-image

DISTRO_CA_BUNDLE = ${BUILD_ROOT}/yocto/openswitch/certs/openswitch.net.crt
