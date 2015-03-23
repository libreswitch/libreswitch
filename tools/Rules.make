# Copyright (C) 2015 Hewlett-Packard Development Company, L.P.
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

PRODUCT?=halon

export HALON_ARCHIVE_ADDRESS=archive.openhalon.io
export HALON_SSTATE_ADDRESS=sstate.openhalon.io

# Toolchain variables
OE_HOST_SYSROOT=$(HALON_ROOT)/build/tmp/sysroots/$(shell uname -m)-linux/
# Some toolchain dirs are named different than their toolchain prefix
# For example ppc
TOOLCHAIN_BIN_PATH=$(OE_HOST_SYSROOT)/usr/bin/$(TOOLCHAIN_DIR_PREFIX)

# Export this variables from the environment to simplify key management when using an agent
export SSH_AGENT_PID
export SSH_AUTH_SOCK
ifneq ($(VERBOSE),)
 export VERBOSE
endif
export BUILDDIR=$(HALON_ROOT)/build
export BB_ENV_EXTRAWHITE=MACHINE DISTRO TCMODE TCLIBC HTTP_PROXY http_proxy HTTPS_PROXY https_proxy FTP_PROXY ftp_proxy ALL_PROXY all_proxy NO_PROXY no_proxy SSH_AGENT_PID SSH_AUTH_SOCK BB_SRCREV_POLICY SDKMACHINE BB_NUMBER_THREADS BB_NO_NETWORK PARALLEL_MAKE GIT_PROXY_COMMAND SOCKS5_PASSWD SOCKS5_USER SCREENDIR STAMPS_DIR HALON_PLATFORM_DTS_FILE HALON_ROOT
export PATH:=$(HALON_ROOT)/yocto/poky/scripts:$(HALON_ROOT)/yocto/poky/bitbake/bin:$(HALON_ROOT)/tools/bin:$(PATH)
export LD_LIBRARY_PATH:=$(HALON_ROOT)/tools/lib:$(LD_LIBRARY_PATH)

# Some well known locations
KERNEL_STAGING_DIR=$(shell cd $(BUILDDIR) ; $(HALON_ROOT)/yocto/poky/bitbake/bin/bitbake -e | awk -F= '/^STAGING_KERNEL_DIR=/ { gsub(/"/, "", $$2); print $$2 }')
DISTRO_VERSION=$(shell cd $(BUILDDIR) ; $(HALON_ROOT)/yocto/poky/bitbake/bin/bitbake -e | awk -F= '/^DISTRO_VERSION=/ { gsub(/"/, "", $$2); print $$2 }')
HALON_UIMAGE_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/uImage
HALON_IMAGE_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/Image
HALON_ZIMAGE_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/zImage
HALON_BZIMAGE_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/bzImage
HALON_SIMPLEIMAGE_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/simpleImage.$(CONFIGURED_PLATFORM)
HALON_SIMPLEIMAGE_INITRAMFS_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/simpleImage.$(CONFIGURED_PLATFORM)-initramfs-$(CONFIGURED_PLATFORM).bin
HALON_VMLINUX_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/vmlinux
HALON_CPIO_FS_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/$(HALON_PRODUCT_FS_TARGET)-$(CONFIGURED_PLATFORM).cpio.gz
HALON_TARGZ_FS_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/$(HALON_PRODUCT_FS_TARGET)-$(CONFIGURED_PLATFORM).tar.gz
HALON_HDDIMG_FS_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/$(HALON_PRODUCT_FS_TARGET)-$(CONFIGURED_PLATFORM).hddimg
HALON_OVA_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/$(HALON_PRODUCT_FS_TARGET)-$(CONFIGURED_PLATFORM).ova
HALON_BOX_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/$(HALON_PRODUCT_FS_TARGET)-$(CONFIGURED_PLATFORM).box
HALON_ONIE_INSTALLER_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/$(ONIE_INSTALLER_FILE)

# Some makefile macros

# Parameters: first argument is the fatal error message
define FATAL_ERROR
	$(ECHO) ; \
	 $(ECHO) "$(RED)ERROR:$(GRAY) $(1)" ; \
	 $(ECHO) ; \
	 exit 1
endef

# Parameters: first argument is the recipe to bake
define BITBAKE
	pushd . > /dev/null ; \
	 cd $(BUILDDIR) ; \
	 umask 002 ; \
	 $(HALON_ROOT)/yocto/poky/bitbake/bin/bitbake $(1) || exit 1 ; \
	 popd  > /dev/null ;
endef

# Parameters: first argument is the recipe to bake
define BITBAKE_NO_FAILURE
	pushd . > /dev/null ; \
	 cd $(BUILDDIR) ; \
	 umask 002 ; \
	 $(HALON_ROOT)/yocto/poky/bitbake/bin/bitbake $(1) ; \
	 popd  > /dev/null ;
endef

# Rule to regenerate the site.conf file if proxies changed
include tools/config/proxy.conf

build/conf/site.conf: tools/config/site.conf.in tools/config/proxy.conf
	$(V)\
	 sed \
	   -e "s|##HALON_ROOT##|$(HALON_ROOT)|" \
           -e "s|##HALON_PROXY_PORT##|$(HALON_PROXY_PORT)|" \
	   -e "s|##HALON_PROXY##|$(HALON_PROXY)|" \
	   tools/config/site.conf.in > $@

build/conf/local.conf: .platform
	$(V)\
	 sed \
	   -e "s|##PRODUCT##|$(PRODUCT)|" \
	   -e "s|##PLATFORM##|$(CONFIGURED_PLATFORM)|" \
           -e "s|##HALON_SSTATE_ADDRESS##|$(HALON_SSTATE_ADDRESS)|" \
	   -e "s|##HALON_ARCHIVE_ADDRESS##|$(HALON_ARCHIVE_ADDRESS)|" \
	   tools/config/local.conf.in > $@

header:: build/conf/site.conf build/conf/local.conf

# Pull Halon specific rules
-include yocto/*/meta-i$(PRODUCT)/Rules.make

# Pull platform specific rules
-include yocto/*/meta-platform-$(CONFIGURED_PLATFORM)/Rules.make
export HALON_PLATFORM_DTS_FILE

########## Common targets shared by most platforms ##############
HOST_ARCH=$(shell uname -m)

.PHONY: kernel _kernel kernelconfig
kernel: header _kernel

_KERNEL_TARGET ?= _kernel

HALON_KERNEL_SYMBOLS_FILE ?= $(HALON_VMLINUX_FILE)
_kernel: 
	$(V) $(ECHO) "$(YELLOW)Building kernel...$(GRAY)\n"
	$(V)$(call BITBAKE,virtual/kernel)
	$(V)if test -f $(HALON_KERNEL_FILE) ; then ln -sf $(HALON_KERNEL_FILE) images/kernel-$(CONFIGURED_PLATFORM).bin ; fi
	$(V)if test -f $(HALON_KERNEL_SYMBOLS_FILE) ; then ln -sf $(HALON_KERNEL_SYMBOLS_FILE) images/kernel-$(CONFIGURED_PLATFORM).elf ; fi
	$(V) $(ECHO)

$(HALON_KERNEL_FILE) images/kernel-$(CONFIGURED_PLATFORM).bin:
	$(V) $(MAKE) $(_KERNEL_TARGET)

.PHONY: fs _fs
ifneq ($(findstring fs,$(MAKECMDGOALS)),)
 ifeq ($(HALON_PRODUCT_FS_TARGET),undefined)
  $(error ====== HALON_PRODUCT_FS_TARGET variable is empty, please specify it on your board meta-<product>/Rules.make =====)
 endif
endif
fs: header _fs

_fs images/fs-$(CONFIGURED_PLATFORM):
	$(V) $(ECHO) "$(YELLOW)Building fs ($(HALON_PRODUCT_FS_TARGET))...$(GRAY)\n"
	$(V)$(call BITBAKE,$(HALON_PRODUCT_FS_TARGET))
	$(V)ln -sf $(HALON_FS_FILE) images/`basename $(HALON_FS_FILE)`
	$(V)for extra_fs in $(HALON_EXTRA_FS_FILES) ; do ln -sf $$extra_fs images/`basename $$extra_fs` ; done
	@# If we have a tar.gz file, also link it, useful for docker images
	$(V)if [ -f $(HALON_TARGZ_FS_FILE) ] ; then ln -sf $(HALON_TARGZ_FS_FILE) images/`basename $(HALON_TARGZ_FS_FILE)` ; fi
	$(V)ln -sf `basename $(HALON_FS_FILE)` images/fs-$(CONFIGURED_PLATFORM)
	$(V)ln -sf `dirname $(HALON_FS_FILE)`/`basename $(HALON_FS_FILE) |  cut -d'.' -f1`.manifest images/`basename $(HALON_FS_FILE) | cut -d'.' -f1`.manifest
	$(V) $(ECHO)

.PHONY: bake _bake
ifneq ($(findstring bake,$(MAKECMDGOALS)),)
 ifeq ($(RECIPE),)
  $(error ====== RECIPE variable is empty, please specify which recipe you want to bake =====)
 endif
endif
bake: header _bake

_bake: 
	$(V)$(call BITBAKE,$(RECIPE))

.PHONY: cleansstate _cleansstate
ifneq ($(findstring cleansstate,$(MAKECMDGOALS)),)
 ifeq ($(RECIPE),)
  $(error ====== RECIPE variable is empty, please specify which recipe you want to clean =====)
 endif
endif
cleansstate: header _cleansstate

_cleansstate:
	$(V)$(call BITBAKE,-c cleansstate $(RECIPE))

ifneq ($(findstring devshell,$(MAKECMDGOALS)),)
 ifeq ($(RECIPE),)
  $(error ====== RECIPE variable is empty, please specify which recipe you want the devshell for  =====)
 endif
endif

# OK, here is an interesting behavior: when se call screen from devshell
# in recent versions of Yocto (Dora and up), it doesn't like to be called
# when the MAKEOVERRIDES variable is set, therefore causing the devshell
# to fail. Unsetting it manually
devshell: header 
	$(V)unset MAKEOVERRIDES ; $(call BITBAKE, -c devshell $(RECIPE))

.PHONY: sdk _sdk
sdk: header _sdk
	$(V) ln -fs $(HALON_ROOT)/build/tmp/deploy/sdk/$(PRODUCT)-glibc-`uname -m`-*-toolchain-*.sh images

_sdk: 
	$(V) $(ECHO) "$(YELLOW)Building SDK...$(GRAY)\n"
	$(V)$(call BITBAKE,meta-toolchain-$(PRODUCT))

# Used to generate the dtb for the board
.PHONY: dtb
dtb: images/$(CONFIGURED_PLATFORM).dtb

# We need to remove the chosen section of the file, since will be added by uboot
images/$(CONFIGURED_PLATFORM).dtb: $(HALON_PLATFORM_DTS_FILE)
	$(V) $(ECHO) " Generating dtb from dts..."
	$(V) cp $< images/$(CONFIGURED_PLATFORM).dts
	$(V) dtc images/$(CONFIGURED_PLATFORM).dts -O dtb -o $@ -p 2048

MKIMAGE=tools/bin/mkimage

$(MKIMAGE): build/tmp/sysroots/$(HOST_ARCH)-linux/usr/bin/uboot-mkimage
	$(V) ln -sf $(HALON_ROOT)/build/tmp/sysroots/$(HOST_ARCH)-linux/usr/bin/mkimage $@

build/tmp/sysroots/$(HOST_ARCH)-linux/usr/bin/uboot-mkimage: 
	$(V)$(ECHO) " Building mkimage..."
	$(V)$(call BITBAKE,u-boot-mkimage-native)

# FIT image for uboot
.PHONY: itb
itb:: images/$(CONFIGURED_PLATFORM).itb
	$(V) $(ECHO) "$(YELLOW)Building itb file...$(GRAY)\n"
	$(V)$(MAKE) images/$(CONFIGURED_PLATFORM).itb

images/$(CONFIGURED_PLATFORM).itb:: $(HALON_PLATFORM_ITS_FILE) $(MKIMAGE)
	$(V) $(ECHO) " Generating itb from its..."
	$(V)cat $(HALON_PLATFORM_ITS_FILE) > images/$(CONFIGURED_PLATFORM).its
	$(V) $(MKIMAGE) -f images/$(CONFIGURED_PLATFORM).its $@

# ONIE installer
.PHONY: onie-installer
onie-installer: header _onie-installer

_onie-installer::
	$(V) $(ECHO) "$(YELLOW)Building ONIE Installer file ($(ONIE_INSTALLER_RECIPE))...$(GRAY)\n"
	$(V)$(call BITBAKE,$(ONIE_INSTALLER_RECIPE))
	$(V)ln -sf $(HALON_ONIE_INSTALLER_FILE) images/`basename $(HALON_ONIE_INSTALLER_FILE)`

ifneq ($(findstring onie-installer,$(MAKECMDGOALS)),)
 ifeq ($(ONIE_INSTALLER_RECIPE),)
  $(error ====== ONIE_INSTALLER_RECIPE variable is empty, please define it in your platform's Rules.make  =====)
 endif
endif

