# Copyright (C) 2015-2016 Hewlett Packard Enterprise Development LP
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

export DISTRO_ARCHIVE_ADDRESS
export DISTRO_SSTATE_ADDRESS
export CURL_CA_BUNDLE=$(DISTRO_CA_BUNDLE)

# Toolchain variables
OE_HOST_SYSROOT=$(BUILD_ROOT)/build/tmp/sysroots/$(shell uname -m)-linux/
# Some toolchain dirs are named different than their toolchain prefix
# For example ppc
TOOLCHAIN_BIN_PATH=$(OE_HOST_SYSROOT)/usr/bin/$(TOOLCHAIN_DIR_PREFIX)

# Export this variables from the environment to simplify key management when using an agent
export SSH_AGENT_PID
export SSH_AUTH_SOCK
ifneq ($(VERBOSE),)
 export VERBOSE
endif
export BUILDDIR=$(BUILD_ROOT)/build
export BB_ENV_EXTRAWHITE=MACHINE DISTRO TCMODE TCLIBC HTTP_PROXY http_proxy HTTPS_PROXY https_proxy FTP_PROXY ftp_proxy ALL_PROXY all_proxy NO_PROXY no_proxy SSH_AGENT_PID SSH_AUTH_SOCK BB_SRCREV_POLICY SDKMACHINE BB_NUMBER_THREADS BB_NO_NETWORK PARALLEL_MAKE GIT_PROXY_COMMAND SOCKS5_PASSWD SOCKS5_USER SCREENDIR STAMPS_DIR PLATFORM_DTS_FILE BUILD_ROOT NFSROOTPATH NFSROOTIP
export PATH:=$(BUILD_ROOT)/yocto/poky/scripts:$(BUILD_ROOT)/yocto/poky/bitbake/bin:$(BUILD_ROOT)/tools/bin:$(PATH)
export LD_LIBRARY_PATH:=$(BUILD_ROOT)/tools/lib:$(LD_LIBRARY_PATH)
export http_proxy
export https_proxy

# Some well known locations
KERNEL_STAGING_DIR=$(shell cd $(BUILDDIR) ; $(BUILD_ROOT)/yocto/poky/bitbake/bin/bitbake -e | awk -F= '/^STAGING_KERNEL_DIR=/ { gsub(/"/, "", $$2); print $$2 }')
DISTRO_VERSION=$(shell cd $(BUILDDIR) ; $(BUILD_ROOT)/yocto/poky/bitbake/bin/bitbake -e | awk -F= '/^DISTRO_VERSION=/ { gsub(/"/, "", $$2); print $$2 }')
STAGING_DIR_TARGET=$(shell cd $(BUILDDIR) ; $(BUILD_ROOT)/yocto/poky/bitbake/bin/bitbake -e | awk -F= '/^STAGING_DIR_TARGET=/ { gsub(/"/, "", $$2); print $$2 }')
STAGING_DIR_NATIVE=$(shell cd $(BUILDDIR) ; $(BUILD_ROOT)/yocto/poky/bitbake/bin/bitbake -e | awk -F= '/^STAGING_DIR_NATIVE=/ { gsub(/"/, "", $$2); print $$2 }')
DEPLOY_DIR_IMAGE=$(shell cd $(BUILDDIR) ; $(BUILD_ROOT)/yocto/poky/bitbake/bin/bitbake -e | awk -F= '/^DEPLOY_DIR_IMAGE=/ { gsub(/"/, "", $$2); print $$2 }')
DEPLOY_DIR_IMAGE_ALL=$(subst $(CONFIGURED_PLATFORM),,$(DEPLOY_DIR_IMAGE))
# Used to identify the valid layers
YOCTO_LAYERS=$(shell cd $(BUILDDIR) ; $(BUILD_ROOT)/yocto/poky/bitbake/bin/bitbake -e | awk -F'=' '/^BBLAYERS=/ { print $$2 }')
BASE_UIMAGE_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/uImage
BASE_IMAGE_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/Image
BASE_ZIMAGE_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/zImage
BASE_BZIMAGE_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/bzImage
BASE_SIMPLEIMAGE_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/simpleImage.$(CONFIGURED_PLATFORM)
BASE_SIMPLEIMAGE_INITRAMFS_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/simpleImage.$(CONFIGURED_PLATFORM)-initramfs-$(CONFIGURED_PLATFORM).bin
BASE_VMLINUX_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/vmlinux
BASE_CPIO_FS_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/$(DISTRO_FS_TARGET)-$(CONFIGURED_PLATFORM).cpio.gz
BASE_TARGZ_FS_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/$(DISTRO_FS_TARGET)-$(CONFIGURED_PLATFORM).tar.gz
BASE_TARGZ_DBG_FS_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/$(DISTRO_FS_TARGET)-$(CONFIGURED_PLATFORM).dbg.tar.gz
BASE_HDDIMG_FS_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/$(DISTRO_FS_TARGET)-$(CONFIGURED_PLATFORM).hddimg
BASE_OVA_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/$(DISTRO_FS_TARGET)-$(CONFIGURED_PLATFORM).ova
BASE_BOX_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/$(DISTRO_FS_TARGET)-$(CONFIGURED_PLATFORM).box
BASE_ONIE_INSTALLER_FILE = $(BUILDDIR)/tmp/deploy/images/$(CONFIGURED_PLATFORM)/$(ONIE_INSTALLER_FILE)
BASE_DOCKER_IMAGE = openswitch/${CONFIGURED_PLATFORM}
HOST_INTERPRETER := $(shell readelf -a /bin/sh | grep interpreter | awk '{ print substr($$4, 0, length($$4)-1)}')
TARGET_INTERPRETER=$(STAGING_DIR_TARGET)/lib/ld-linux-x86-64.so.2

UUIDGEN_NATIVE=$(STAGING_DIR_NATIVE)/usr/bin/uuidgen
PYTEST_NATIVE=$(STAGING_DIR_NATIVE)/usr/bin/py.test
# Rake binary path
RAKE_NATIVE = $(STAGING_DIR_NATIVE)/usr/bin/rake

# Static Code Analysis tool. Right now we support Fortify, but others like coverity could be added
SCA_TOOLCHAIN ?= fortify
SCA_TOOL ?= sourceanalyzer
SCA_TOOL_SCAN_CMD ?= -scan -b $$(basename $$(pwd)) -f $$(basename $$(pwd)).fpr

# Leave blank to use default location
SSTATE_DIR?=""

# Some makefile macros

# Parameters: first argument is the fatal error message
define FATAL_ERROR
	$(ECHO) ; \
	 $(ECHO) "$(RED)ERROR:$(GRAY) $(1)" ; \
	 $(ECHO) ; \
	 exit 1
endef

# Parameters: first argument is the message
define WARNING
	$(ECHO) ; \
	 $(ECHO) "$(BLUE)WARNING:$(GRAY) $(1)" ; \
	 $(ECHO)
endef

# Parameters: first argument is the recipe to bake
define BITBAKE
	cd $(BUILDDIR) ; umask 002 ; \
	 $(BUILD_ROOT)/yocto/poky/bitbake/bin/bitbake $(if $(VERBOSE),-v,) $(1)
endef

# Parameters: first argument is the recipe to bake
define BITBAKE_NO_FAILURE
	cd $(BUILDDIR) ; umask 002 ; \
	 $(BUILD_ROOT)/yocto/poky/bitbake/bin/bitbake $(1) || exit 1
endef

define DEVTOOL
	 cd $(BUILDDIR) ; umask 002 ; \
	 $(BUILD_ROOT)/yocto/poky/scripts/devtool $(1) || exit 1
endef

define PARSE_TWO_ARGUMENTS
ifeq ($(1),$(firstword $(MAKECMDGOALS)))
  # use the rest as arguments for "$(1)"
  EXTRA_ARGS_1 := $(wordlist 2, 2,$(MAKECMDGOALS))
  EXTRA_ARGS_2 := $(wordlist 3, $(words $(MAKECMDGOALS)),$(MAKECMDGOALS))
  # ...and turn them into do-nothing targets
  $(eval $(wordlist 2,$(words $(MAKECMDGOALS)),$(MAKECMDGOALS))::;@:)
endif
endef

UT_PARAMS ?= --gtest_shuffle

define EXECUTE_UT_TEST_HARNESS
$(TARGET_INTERPRETER) --library-path $(STAGING_DIR_TARGET)/lib:$(STAGING_DIR_TARGET)/usr/lib $(1) $(UT_PARAMS)
endef

VALGRIND ?= valgrind
VALGRIND_OPTIONS ?= --leak-check=full --track-origins=yes
define EXECUTE_UT_TEST_HARNESS_ON_VALGRIND
$(VALGRIND) $(VALGRIND_OPTIONS) $(TARGET_INTERPRETER) --library-path $(STAGING_DIR_TARGET)/lib:$(STAGING_DIR_TARGET)/usr/lib $(1)
endef

# Rule to regenerate the site.conf file if proxies changed
include tools/config/proxy.conf

build/conf/site.conf: tools/config/site.conf.in tools/config/proxy.conf
	$(V)mkdir -p $(dir $@)
	$(V)cp tools/config/site.conf.in $@
	$(V)if [ -n "$(GIT_PROXY_COMMAND)" ] ; then \
           sed -i -e "s|##GIT_PROXY_COMMAND##|GIT_PROXY_COMMAND = \"$(GIT_PROXY_COMMAND)\"|" $@ ; \
	 fi
	$(V)if [ -n "$(PROXY)" ] ; then \
	   sed -i -e "s|##ALL_PROXY##|ALL_PROXY = \"http://$(PROXY):$(PROXY_PORT)\"|" $@ ; \
	 fi

build/conf/local.conf: .platform
	$(V)mkdir -p $(dir $@)
	$(V)\
	 sed \
	   -e "s|##DISTRO##|$(DISTRO)|" \
	   -e "s|##PLATFORM##|$(CONFIGURED_PLATFORM)|" \
           -e "s|##DISTRO_SSTATE_ADDRESS##|$(DISTRO_SSTATE_ADDRESS)|" \
	   -e "s|##DISTRO_ARCHIVE_ADDRESS##|$(DISTRO_ARCHIVE_ADDRESS)|" \
	   tools/config/local.conf.in > $@
	$(V)if [ -n "$(SSTATE_DIR)" ] ; then \
      if [ -d $(SSTATE_DIR) ] && [ -w $(SSTATE_DIR) ] ; then \
	      $(ECHO) "$(BLUE)Using shared state cache from $(SSTATE_DIR)...$(GRAY)\n" ; \
	      sed -i -e "s|^#SSTATE_DIR.*|SSTATE_DIR = \"$(SSTATE_DIR)\"|" $@ ; \
      else \
	      $(ECHO) "$(RED)IGNORING shared state cache $(SSTATE_DIR): not a writable directory$(GRAY)\n" ; \
      fi \
    fi
	$(V)if [ -n "$(DL_DIR)" ] ; then \
      if [ -d $(DL_DIR) ] && [ -w $(DL_DIR) ] ; then \
	      $(ECHO) "$(BLUE)Using shared download directory from $(DL_DIR)...$(GRAY)\n" ; \
	      sed -i -e "s|^#DL_DIR.*|DL_DIR = \"$(DL_DIR)\"|" $@ ; \
      else \
	      $(ECHO) "$(RED)IGNORING shared download directory $(DL_DIR): not a writable directory$(GRAY)\n" ; \
      fi \
    fi

header:: build/conf/site.conf build/conf/local.conf

-include yocto/*/meta-platform-$(DISTRO)-$(CONFIGURED_PLATFORM)/Rules.make
export PLATFORM_DTS_FILE

########## Common targets shared by most platforms ##############
HOST_ARCH=$(shell uname -m)

.PHONY: kernel _kernel _kernel_links kernelconfig
kernel: header _kernel

_KERNEL_TARGET ?= _kernel

DISTRO_KERNEL_SYMBOLS_FILE ?= $(BASE_VMLINUX_FILE)
_kernel:
	$(V) $(ECHO) "$(BLUE)Building kernel...$(GRAY)\n"
	$(V)$(call BITBAKE,virtual/kernel)
	$(V) $(MAKE) _kernel_links
	$(V) $(ECHO)

_kernel_links:
	$(V)if test -f $(DISTRO_KERNEL_FILE) ; then ln -sf $(DISTRO_KERNEL_FILE) images/kernel-$(CONFIGURED_PLATFORM).bin ; fi
	$(V)if test -f $(DISTRO_KERNEL_SYMBOLS_FILE) ; then ln -sf $(DISTRO_KERNEL_SYMBOLS_FILE) images/kernel-$(CONFIGURED_PLATFORM).elf ; fi

$(DISTRO_KERNEL_FILE) images/kernel-$(CONFIGURED_PLATFORM).bin:
	$(V) $(MAKE) $(_KERNEL_TARGET)

.PHONY: fs _fs _fs_links
ifneq ($(findstring fs,$(MAKECMDGOALS)),)
 ifeq ($(DISTRO_FS_TARGET),undefined)
  $(error ====== DISTRO_FS_TARGET variable is empty, please specify it on your board meta-<product>/Rules.make =====)
 endif
endif
fs: header _fs

_fs images/fs-$(CONFIGURED_PLATFORM):
	$(V) $(ECHO) "$(BLUE)Building fs ($(DISTRO_FS_TARGET))...$(GRAY)\n"
	$(V)$(call BITBAKE,$(DISTRO_FS_TARGET))
	$(V) $(MAKE) _fs_links
	$(V) $(ECHO)

_fs_links:
	$(V)ln -sf $(DISTRO_FS_FILE) images/`basename $(DISTRO_FS_FILE)`
	$(V)for extra_fs in $(DISTRO_EXTRA_FS_FILES) ; do ln -sf $$extra_fs images/`basename $$extra_fs` ; done
	@# If we have a tar.gz file, also link it, useful for docker images
	$(V)if [ -f $(BASE_TARGZ_FS_FILE) ] ; then ln -sf $(BASE_TARGZ_FS_FILE) images/`basename $(BASE_TARGZ_FS_FILE)` ; fi
	$(V)if [ -f $(BASE_TARGZ_DBG_FS_FILE) ] ; then ln -sf $(BASE_TARGZ_DBG_FS_FILE) images/`basename $(BASE_TARGZ_DBG_FS_FILE)` ; fi
	$(V)ln -sf `basename $(DISTRO_FS_FILE)` images/fs-$(CONFIGURED_PLATFORM)
	$(V)ln -sf `dirname $(DISTRO_FS_FILE)`/`basename $(DISTRO_FS_FILE) |  cut -d'.' -f1`.manifest images/`basename $(DISTRO_FS_FILE) | cut -d'.' -f1`.manifest

.PHONY: bake _bake
$(eval $(call PARSE_ARGUMENTS,bake))
ifneq ($(findstring bake,$(MAKECMDGOALS)),)
 RECIPE?=$(EXTRA_ARGS)
 ifeq ($(RECIPE),)
  $(error ====== RECIPE variable is empty, please specify which recipe you want to bake =====)
 endif
endif
bake: header _bake

_bake:
	$(V)$(call BITBAKE,$(RECIPE))

.PHONY: cleansstate _cleansstate
$(eval $(call PARSE_ARGUMENTS,cleansstate))
ifneq ($(findstring cleansstate,$(MAKECMDGOALS)),)
 RECIPE?=$(EXTRA_ARGS)
 ifeq ($(RECIPE),)
  $(error ====== RECIPE variable is empty, please specify which recipe you want to clean =====)
 endif
endif
cleansstate: header _cleansstate

_cleansstate:
	$(V)$(call BITBAKE,-c cleansstate $(RECIPE))

CONTAINER_NAME?=openswitch
.PHONY: deploy_lxc
deploy_lxc:
	$(V) if ! which lxc-create > /dev/null ; then \
	  $(call FATAL_ERROR,LXC does not seems installed, could not find lxc-create) ; \
	fi
	$(V) if ! test -f images/`basename $(BASE_TARGZ_FS_FILE)` ; then \
	  $(call FATAL_ERROR,Your platform has not generated a .tar.gz file that can be used to create the LXC container) ; \
	fi
	$(V) $(ECHO) "Exporting an LXC container with name '$(CONTAINER_NAME)' may ask for admin password..."
	$(V) $(ECHO) -n "Checking that no LXC container with the same name already exists..."
	$(V) if $(SUDO) lxc-info -n $(CONTAINER_NAME) >/dev/null 2>&1 ; then \
	  echo ; \
	  $(call FATAL_ERROR, A LXC container '$(CONTAINER_NAME)' already exists... aborting.\nYou may remove it with 'sudo lxc-destroy -n $(CONTAINER_NAME)') ; \
	else \
	  echo done ; \
	fi
	$(V) export OPENSWITCH_IMAGE=$(BUILD_ROOT)/images/`basename $(BASE_TARGZ_FS_FILE)` ; \
	export BUILD_ROOT ; \
	$(SUDO) -E lxc-create -n $(CONTAINER_NAME) -f /dev/null -t $(BUILD_ROOT)/tools/lxc/lxc-openswitch
	$(V) $(ECHO) "Exporting completed.\nRun with 'sudo lxc-start -n $(CONTAINER_NAME)'"

.PHONY: export_docker_image
$(eval $(call PARSE_ARGUMENTS,export_docker_image))
DOCKER_IMAGE:=$(EXTRA_ARGS)
ifeq ($(DOCKER_IMAGE),)
DOCKER_IMAGE=$(BASE_DOCKER_IMAGE)
endif
export_docker_image:
	$(V) if ! which docker > /dev/null ; then \
	    $(call FATAL_ERROR, Docker is not installed. \
	                        Could not find 'docker' command.) ; \
	fi
	$(V) if docker images $(DOCKER_IMAGE) | grep $(DOCKER_IMAGE) >/dev/null ; then \
	    $(call FATAL_ERROR, Docker image '$(DOCKER_IMAGE)' is already created.\n \
	                       \tYou can remove it using - docker rmi $(DOCKER_IMAGE)) ; \
	fi
	$(V) if ! test -f images/`basename $(BASE_TARGZ_FS_FILE)` ; then \
	    $(call FATAL_ERROR, Unable to find $(BASE_TARGZ_FS_FILE)\n \
	                       \tRun 'make' at the top level to create root-fs.) ; \
	fi
	$(V) $(ECHO) "$(BLUE)Exporting '$(BASE_TARGZ_FS_FILE)' as image '$(DOCKER_IMAGE)'...$(GRAY)\n"
	$(V) /bin/zcat $(BASE_TARGZ_FS_FILE) | docker import - $(DOCKER_IMAGE)
	$(V) $(ECHO)

.PHONY: deploy_nfsroot
NFSROOTPATH?=$(BUILD_ROOT)/nfsroot-${CONFIGURED_PLATFORM}
export NFSROOTPATH
deploy_nfsroot:
	$(V) if ! which exportfs > /dev/null ; then \
	  $(call FATAL_ERROR,Missing exportfs utility, unable to export rootfs. Did you install the NFS server package?) ; \
	fi
	$(V) if ! test -f images/$(notdir $(BASE_TARGZ_FS_FILE)) ; then \
	  $(call FATAL_ERROR,Your platform has not generated a .tar.gz file that can be used to deploy the NFS root) ; \
	fi
	$(V) if [ -d $(NFSROOTPATH) ] ; then \
	  $(call WARNING,Removing previous deployed nfsroot directory at $(NFSROOTPATH) before re-deploying) ; \
	  $(ECHO) "Press any key to continue wipping out previous nfsroot, or ctrl+c to abort..." ; \
	  read ; \
	  $(SUDO) rm -Rf $(NFSROOTPATH) ; \
	fi
	$(V) mkdir -p $(NFSROOTPATH)
	$(V) $(ECHO) -n "Extracting the NFS root into $(NFSROOTPATH)... "
	$(V) tar -xzf images/$(notdir $(BASE_TARGZ_FS_FILE)) -C $(NFSROOTPATH)
	$(V) $(ECHO) done
	$(V) if ! [ -f /etc/exports.d/$(notdir $(NFSROOTPATH)).exports ] ; then \
	  $(ECHO) "\nExporting NFS directory, may ask for admin password..." ; \
	  $(SUDO) mkdir -p /etc/exports.d ; \
	  $(SUDO) bash -c 'echo "$(NFSROOTPATH) *(rw,no_root_squash,sync,no_subtree_check,insecure)" > /etc/exports.d/$(notdir $(NFSROOTPATH)).exports' ; \
	  if which service > /dev/null ; then \
	    $(SUDO) service nfs-kernel-server start ; \
	  fi ; \
	  echo ; \
	fi

.PHONY: devshell
$(eval $(call PARSE_ARGUMENTS,devshell))
ifneq ($(findstring devshell,$(MAKECMDGOALS)),)
 RECIPE?=$(EXTRA_ARGS)
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
	$(V) ln -fs $(BUILD_ROOT)/build/tmp/deploy/sdk/$(DISTRO)-glibc-`uname -m`-*-toolchain-*.sh images

_sdk:
	$(V) $(ECHO) "$(BLUE)Building SDK...$(GRAY)\n"
	$(V)$(call BITBAKE,meta-toolchain-$(DISTRO))

# Used to generate the dtb for the board
.PHONY: dtb
dtb: images/$(CONFIGURED_PLATFORM).dtb

# We need to remove the chosen section of the file, since will be added by uboot
images/$(CONFIGURED_PLATFORM).dtb: $(PLATFORM_DTS_FILE)
	$(V) $(ECHO) " Generating dtb from dts..."
	$(V) cp $< images/$(CONFIGURED_PLATFORM).dts
	$(V) dtc images/$(CONFIGURED_PLATFORM).dts -O dtb -o $@ -p 2048

MKIMAGE=tools/bin/mkimage

$(MKIMAGE): build/tmp/sysroots/$(HOST_ARCH)-linux/usr/bin/uboot-mkimage
	$(V) ln -sf $(BUILD_ROOT)/build/tmp/sysroots/$(HOST_ARCH)-linux/usr/bin/mkimage $@

build/tmp/sysroots/$(HOST_ARCH)-linux/usr/bin/uboot-mkimage:
	$(V)$(ECHO) " Building mkimage..."
	$(V)$(call BITBAKE,u-boot-mkimage-native)

# FIT image for uboot
.PHONY: itb
itb:: images/$(CONFIGURED_PLATFORM).itb
	$(V) $(ECHO) "$(BLUE)Building itb file...$(GRAY)\n"
	$(V)$(MAKE) images/$(CONFIGURED_PLATFORM).itb

images/$(CONFIGURED_PLATFORM).itb:: $(DISTRO_PLATFORM_ITS_FILE) $(MKIMAGE)
	$(V) $(ECHO) " Generating itb from its..."
	$(V)cat $(DISTRO_PLATFORM_ITS_FILE) > images/$(CONFIGURED_PLATFORM).its
	$(V) $(MKIMAGE) -f images/$(CONFIGURED_PLATFORM).its $@

# ONIE installer
.PHONY: onie-installer
onie-installer: header _kernel_links _fs _onie-installer

DISTRO_ONIE_INSTALLER_FILE?= $(BASE_ONIE_INSTALLER_FILE)
_onie-installer::
	$(V) $(ECHO) "$(BLUE)Building ONIE Installer file ($(ONIE_INSTALLER_RECIPE))...$(GRAY)\n"
	$(V)$(call BITBAKE,$(ONIE_INSTALLER_RECIPE))
	$(V)ln -sf $(DISTRO_ONIE_INSTALLER_FILE) images/`basename $(DISTRO_ONIE_INSTALLER_FILE)`

ifneq ($(findstring onie-installer,$(MAKECMDGOALS)),)
 ifeq ($(ONIE_INSTALLER_RECIPE),)
  $(error ====== ONIE_INSTALLER_RECIPE variable is empty, please define it in your platform's Rules.make  =====)
 endif
endif

# Devenv code
GITREVIEWUSER:=$(shell git config --get gitreview.username)
ifneq ($(GITREVIEWUSER),)
 REVIEWUSER?=$(GITREVIEWUSER)
endif
REVIEWUSER?=$(USER)

setup-git-review:
	$(V) $(ECHO) "$(BLUE)Setting up git-review system...$(GRAY)\n"
	$(V)$(MAKE) _setup-git-review

_setup-git-review:: $(addprefix .git/hooks/,$(notdir $(wildcard $(BUILD_ROOT)/tools/bin/hooks/*)))
	$(V) if which git-review > /dev/null ; then \
	  git review -s ; \
	else \
	  $(call WARNING,git-review wasn't found... skipping its configuration) ; \
	fi

.git/hooks/%:
	$(V) cp $(BUILD_ROOT)/tools/bin/hooks/$* $@

.PHONY: devenv_init devenv_clean devenv_add devenv_rm devenv_status devenv_cscope devenv_list_all
.PHONY: devenv_import dev_header devenv_refresh _devenv_refresh

-include src/Rules.make

dev_header: header
	$(V) flock -n $(BUILDDIR)/bitbake.lock echo -n || \
	   { echo "Bitbake is currently running... can't proceed further, aborting" ; \
             exit 255 ; }
	$(V) if ! [ -f .devenv ] ; then \
	  $(call FATAL_ERROR, devenv is not initialized, use 'devenv_init') ; \
	fi

devenv_init: header
	$(V) $(ECHO) "$(BLUE)Configuring development enviroment...$(GRAY)\n"
	$(V) $(call BITBAKE,meta-ide-support)
	$(V) touch .devenv
	$(V) $(MAKE) setup-git-review

# We try to export this symbol only when the target is invoked, since the expansion
# can cause to trigger bitbake before is configured in other cases
ifneq ($(findstring devenv_list_all,$(MAKECMDGOALS)),)
  export YOCTO_LAYERS
endif
devenv_list_all: header
	$(V)$(ECHO) "List of available devenv packages for $(CONFIGURED_PLATFORM) platform:"
	$(V) for layer in $$YOCTO_LAYERS ; do \
	   test -d $$layer || continue ; \
	   DEVCONFS="$$DEVCONFS `find $$layer -name devenv.conf`" ; \
	 done ; \
	 for devconf in $$DEVCONFS ; do \
	   while read recipe ; do \
	     [[ $$recipe == \#* ]] && continue ; \
	     $(ECHO) "  * $$recipe" ; \
	   done < $$devconf ; \
	done

devenv_cscope: header
	$(V) if !  which cscope > /dev/null ; then \
	  $(call FATAL_ERROR,Could not find cscope in your path, please install it.) ; \
	fi
	$(V) $(ECHO) "$(BLUE)Updating cscope indexes for development environment...$(GRAY)\n"
	$(V)find $(STAGING_DIR_TARGET)/usr/include -type f -name "*.[chxsS]" -print > $(BUILD_ROOT)/src/cscope.files
	$(V)find $(BUILD_ROOT)/src -type f -name "*.[chxsS]" -print >> $(BUILD_ROOT)/src/cscope.files
	$(V)cd $(BUILD_ROOT)/src/ ; cscope -b -q -k

devenv_clean: dev_header
	$(V)$(call DEVTOOL, reset -a)
	$(V)rm -Rf src .devenv

DEVENV_BRANCH?=*auto*

define QUERY_RECIPE
	echo "$(2) `query-recipe.py --var $(1) $(2)`";
endef

define DEVENV_ADD
	if ! grep -q '^$(1)$$' .devenv 2>/dev/null ; then \
	  $(call DEVTOOL, modify --extract $(1) $(BUILD_ROOT)/src/$(1)) ; \
	  pushd . > /dev/null ; \
	  cd $(BUILD_ROOT)/src/$(1) ; \
	  if [ -f .gitreview ] ; then \
	    gitdir=$$(git rev-parse --git-dir); cp -f $(BUILD_ROOT)/tools/bin/hooks/* $${gitdir}/hooks/ ; \
	  fi ; \
	  DEVENV_BRANCH=$(DEVENV_BRANCH) ; \
	  if [[ "$$DEVENV_BRANCH" == "*auto*" ]] ; then \
	    DEVENV_BRANCH=`query-recipe.py --gitbranch $(1)` ; \
	    $(ECHO) "$(WHITE)NOTE:$(GRAY) Checking out recipe branch: $(BLUE)$$DEVENV_BRANCH$(GRAY)" ; \
	  fi ; \
	  git checkout $$DEVENV_BRANCH || { $(call FATAL_ERROR, Unable to checkout the request branch '$$DEVENV_BRANCH') ; } ; \
	  popd > /dev/null ; \
	  sed -e "s/##RECIPE##/$(1)/g" $(BUILD_ROOT)/tools/devenv-recipe-template.make >> $(BUILD_ROOT)/src/Rules.make ; \
	  echo $(1) >> $(BUILD_ROOT)/.devenv ; \
	else \
	  $(ECHO) "$(BLUE)$(1)$(GRAY) is already in your devenv" ; \
	fi ;
endef

$(eval $(call PARSE_ARGUMENTS,devenv_add))
ifneq ($(findstring devenv_add,$(MAKECMDGOALS)),)
  PACKAGE?=$(EXTRA_ARGS)
  ifeq ($(PACKAGE),)
   $(error ====== PACKAGE variable is empty, please specify which package you want  =====)
  endif
endif
devenv_add: dev_header
	$(V)$(foreach P, $(PACKAGE), $(call DEVENV_ADD,$(P)))

.PHONY: query_recipe

$(eval $(call PARSE_ARGUMENTS,query_recipe))
ifneq ($(findstring query_recipe,$(MAKECMDGOALS)),)
  ifeq ($(VAR),)
   $(error ====== VAR variable is empty, please specify which variable to query (VAR=SRCREV, VAR=SRC_URI, etc.) =====)
  endif
  PACKAGE?=$(EXTRA_ARGS)
  ifeq ($(PACKAGE),)
   $(error ====== PACKAGE variable is empty, please specify which package(s) you want  =====)
  endif
endif

query_recipe:
	@$(foreach P, $(PACKAGE), $(call QUERY_RECIPE,$(VAR),$(P)))

ifeq (devenv_import,$(firstword $(MAKECMDGOALS)))
  $(eval $(call PARSE_TWO_ARGUMENTS,devenv_import))
  PACKAGE?=$(EXTRA_ARGS_1)
  IMPORTED_SRC?=$(EXTRA_ARGS_2)
  ifeq ($(PACKAGE),)
   $(error ====== PACKAGE variable is empty $(PACKAGE), $(EXTRA_ARGS_1), $(IMPORTED_SRC), $(EXTRA_ARGS_2), please specify which package you want  =====)
  endif
  ifeq ($(IMPORTED_SRC),)
   $(error ====== IMPORTED_SRC variable is empty, please specify the source to import  =====)
  endif
endif
devenv_import:
	$(V) grep  -q $(PACKAGE) .devenv 2>/dev/null || $(call DEVTOOL, modify $(PACKAGE) $(IMPORTED_SRC)) && \
	mkdir -p $(BUILD_ROOT)/src && \
	sed -e "s/##RECIPE##/$(PACKAGE)/g" $(BUILD_ROOT)/tools/devenv-recipe-template.make >> $(BUILD_ROOT)/src/Rules.make && \
	echo $(PACKAGE) >> $(BUILD_ROOT)/.devenv

$(eval $(call PARSE_ARGUMENTS,devenv_rm))
ifneq ($(findstring devenv_rm,$(MAKECMDGOALS)),)
  PACKAGE?=$(EXTRA_ARGS)
  ifeq ($(PACKAGE),)
   $(error ====== PACKAGE variable is empty, please specify which package you want =====)
  endif
endif
devenv_rm: dev_header
	$(V)$(V)sed -i -e "/#$(PACKAGE)$$/,/#END_$(PACKAGE)$$/d" src/Rules.make
	$(V)sed -i -e "/^$(PACKAGE)$$/d" .devenv
	$(V)$(call DEVTOOL,reset $(PACKAGE))
	$(V)rm -Rf src/$(PACKAGE)

devenv_status: dev_header
	$(V) $(call DEVTOOL,status)

$(eval $(call PARSE_ARGUMENTS,devenv_update_recipe))
ifneq ($(findstring devenv_update_recipe,$(MAKECMDGOALS)),)
  PACKAGE?=$(EXTRA_ARGS)
  ifeq ($(PACKAGE),)
   $(error ====== PACKAGE variable is empty, please specify which package you want =====)
  endif
endif
devenv_update_recipe: dev_header
	$(V)$(call DEVTOOL,update-recipe $(PACKAGE))

$(eval $(call PARSE_ARGUMENTS,devenv_patch_recipe))
ifneq ($(findstring devenv_patch_recipe,$(MAKECMDGOALS)),)
  PACKAGE?=$(EXTRA_ARGS)
  ifeq ($(PACKAGE),)
   $(error ====== PACKAGE variable is empty, please specify which package you want =====)
  endif
endif
devenv_patch_recipe: dev_header
	$(V)$(call DEVTOOL,update-recipe -m patch $(PACKAGE))


devenv_refresh: dev_header _devenv_refresh

_devenv_refresh:
	$(V) $(ECHO) "$(BLUE)Updating all the repositories on the developer environment...$(GRAY)"
	$(V) while read repo ; do \
	  echo -e "\nUpdating src/$$repo" ; \
	  pushd . >/dev/null ; \
	  cd src/$$repo ; \
	  git pull --rebase || $(ECHO) "${RED}WARNING: git pull failed, skipping this error$(GRAY)" ; \
	  popd >/dev/null ; \
	done < .devenv
	$(V) $(ECHO) "\n$(PURPLE)Update completed$(GRAY)"

# Test environment

.PHONY: _testenv_header testenv_init testenv_clean testenv_run testenv_rerun
.PHONY: _testenv_rerun _testenv_suite_rerun testenv_suite_run testenv_suite_rerun
.PHONY: testenv_suite_list

_testenv_header: header
	$(V) flock -n $(BUILDDIR)/bitbake.lock echo -n || \
	   { echo "Bitbake is currently running... can't proceed further, aborting" ; \
             exit 255 ; }
	$(V) if ! [ -f .testenv ] ; then \
	  $(call FATAL_ERROR, testenv is not initialized, use 'testenv_init') ; \
	fi

testenv_init: dev_header
	$(V) if ! which tox > /dev/null ; then \
		$(call FATAL_ERROR,Python's tox is not installed. Please use your package manager to install it:\n\n  Hint: on Debian/Ubuntu systems you can install it with: 'sudo apt-get install python-tox') ; \
	 fi ; \
	 if ! which python3-config > /dev/null ; then \
	        $(call FATAL_ERROR,Python's 3 development package is not installed and is required.\n  Hint: on Debian/Ubuntu systems you can install it with: 'sudo apt-get install python3-dev') ; \
	 fi ;
	$(V) if ! [ -f /etc/sudoers.d/topology ] ; then \
	     $(ECHO) "$(BLUE) Setting up sudoer permissions for the topology framework... $(GRAY)\n" ; \
		 echo "$(USER) ALL = (root) NOPASSWD: /sbin/ip, /bin/mkdir -p /var/run/netns, /bin/rm /var/run/netns/*, /bin/ln -s /proc/*/ns/net /var/run/netns/*" | \
		 $(SUDO) tee /etc/sudoers.d/topology ; \
		 $(ECHO) ; \
	 fi
	$(V) $(MAKE) _devenv_ct_init
	$(V) touch .testenv


TOPOLOGY_TEST_IMAGE?=ops_$(USER)$(subst /,_,$(BUILD_ROOT))

# TOPOLOGY_TEST_COV_DIR: directory in the local system that contains the gcov gcno (coverage notes) files.
# The attributes.json expects this directory.
# In the current implementation, this directory is shared between the host and the container using Docker volumes to collect the coverage data (gcda) files.
TOPOLOGY_TEST_COV_DIR?=$(BUILD_ROOT)/src

ifeq (testenv_run,$(firstword $(MAKECMDGOALS)))
  $(eval $(call PARSE_TWO_ARGUMENTS,testenv_run))
  export TESTSUITE?=$(EXTRA_ARGS_1)
  export COMPONENTS?=$(EXTRA_ARGS_2)
  ifeq ($(TESTSUITE),)
   $(error ====== TESTSUITE variable is empty, please specify which test suite you want =====)
  endif
  ifeq ($(COMPONENTS),)
   $(error ====== COMPONENTS variable is empty, please specify which component you want =====)
  endif
endif
testenv_run: _testenv_header
	$(V) $(MAKE) _fs
	$(V) for name in `docker ps -a -q --filter="image=$(TOPOLOGY_TEST_IMAGE)"`; do \
	   echo "Cleaning the docker container ($$name), since is using the old image" ; \
	   docker stop $$name >/dev/null ; \
	   docker rm -f $$name >/dev/null ; \
	 done
	$(V) docker rmi $(TOPOLOGY_TEST_IMAGE) > /dev/null 2>&1 || true
	$(V) $(MAKE) export_docker_image $(TOPOLOGY_TEST_IMAGE)
	$(V) $(SUDO) rm -Rf $(BUILDDIR)/test/$(TESTSUITE)
	$(V) $(MAKE) _testenv_rerun
	$(V) $(SUDO) modprobe bonding

ifeq (testenv_rerun,$(firstword $(MAKECMDGOALS)))
  $(eval $(call PARSE_TWO_ARGUMENTS,testenv_rerun))
  export TESTSUITE?=$(EXTRA_ARGS_1)
  export COMPONENTS?=$(EXTRA_ARGS_2)
  ifeq ($(TESTSUITE),)
   $(error ====== TESTSUITE variable is empty, please specify which test suite you want =====)
  endif
  ifeq ($(COMPONENTS),)
   $(error ====== COMPONENTS variable is empty, please specify which component you want =====)
  endif
endif
testenv_rerun: _testenv_header
	$(V) $(MAKE) _testenv_rerun

define TESTENV_PREPARE
	$(V) # Find if the component is on the devenv
	$(V) \
      test_source_path="ops-tests/$(TESTSUITE)" ; \
	  if [ "$(TESTSUITE)" = "legacy" ] ; then \
	    test_source_path="tests" ; \
	  fi ; \
	  if [ -f .devenv ] && [ -d src/$(1) ] ; then \
	   $(ECHO) "$(1): using tests from devenv..." ; \
	   if ! [ -d src/$(1)/$$test_source_path ] ; then \
		 $(call FATAL_ERROR, No testsuite found at src/$(1)/$$test_source_path); \
	   fi ; \
	   ln -sf $(BUILD_ROOT)/src/$(1)/$$test_source_path $(BUILDDIR)/test/$(TESTSUITE)/code_under_test/$(1) ; \
	 else \
	   $(ECHO) "$(1): fetching tests from git..." ; \
	   $$(query-recipe.py -s -v SRCREV --gitrepo --gitbranch $(1)) ; \
	   if [ -z "$$gitrepo" ] ; then $(call FATAL_ERROR, Unable to find the recipe for $(COMPONENT)) ; fi ; \
	   rm -Rf $(BUILDDIR)/test/$(TESTSUITE)/downloads/$(1)/git ; \
	   if ! git clone -q --single-branch -b $$gitbranch $$gitrepo $(BUILDDIR)/test/$(TESTSUITE)/downloads/$(1)/git ; then \
	    $(call FATAL_ERROR, Unable to clone the required version of the code) ; \
	   fi ; \
	   pushd . >/dev/null ; \
	   if ! cd $(BUILDDIR)/test/$(TESTSUITE)/downloads/$(1)/git ; then \
	     $(call FATAL_ERROR, Unable to find the cloned code) ; \
	   fi ; \
	   git reset $$SRCREV --hard ; \
	   popd > /dev/null ; \
	   if ! [ -d $(BUILDDIR)/test/$(TESTSUITE)/downloads/$(1)/git/$$test_source_path ] ; then \
		 $(call FATAL_ERROR, No testsuite found at '/$$test_source_path' inside the git repo $$gitrepo); \
	   fi ; \
	   ln -sf $(BUILDDIR)/test/$(TESTSUITE)/downloads/$(1)/git/$$test_source_path \
		 $(BUILDDIR)/test/$(TESTSUITE)/code_under_test/$(1) ; \
	 fi ; \
	 if [ "$(TESTSUITE)" = "legacy" ] ; then \
	   cp tools/pytest.ini $(BUILDDIR)/test/$(TESTSUITE)/pytest.ini ; \
	 else \
	   if [ -f $(BUILDDIR)/test/$(TESTSUITE)/code_under_test/$(1)/requirements.txt ] ; then \
		 $(call WARNING,Overriding the global requirements.txt with the one from $(1)) ; \
		 cp $(BUILDDIR)/test/$(TESTSUITE)/code_under_test/$(1)/requirements.txt \
		   $(BUILDDIR)/test/$(TESTSUITE)/ ; \
	   else \
		 cp tools/topology/requirements.txt $(BUILDDIR)/test/$(TESTSUITE)/ ; \
	   fi ; \
	   if [ -f $(BUILDDIR)/test/$(TESTSUITE)/code_under_test/$(1)/tox.ini ] ; then \
	     $(call WARNING,Overriding the global tox.ini with the one from $(1)) ; \
		 cp $(BUILDDIR)/test/$(TESTSUITE)/code_under_test/$(1)/tox.ini \
		   $(BUILDDIR)/test/$(TESTSUITE)/ ; \
	   else \
		 cp tools/topology/tox.ini $(BUILDDIR)/test/$(TESTSUITE)/ ; \
	   fi ; \
	   output_attr_json=$(BUILDDIR)/test/$(TESTSUITE)/attributes.json ; \
	   if [ -f $(BUILDDIR)/test/$(TESTSUITE)/code_under_test/$(1)/attributes.json.in ] ; then \
		 $(call WARNING, Overriding the global attributes.json with the one from $(1)) ; \
		 sed -e 's?@TEST_IMAGE@?$(TOPOLOGY_TEST_IMAGE):latest?' \
		   $(BUILDDIR)/test/$(TESTSUITE)/code_under_test/$(1)/attributes.json.in \
		   > $$output_attr_json ; \
		 sed -i 's?@TEST_COV_DIR@?$(TOPOLOGY_TEST_COV_DIR)?g' $$output_attr_json ; \
	   else \
		 sed -e 's?@TEST_IMAGE@?$(TOPOLOGY_TEST_IMAGE):latest?' \
		   tools/topology/attributes.json.in > $$output_attr_json ; \
		 sed -i 's?@TEST_COV_DIR@?$(TOPOLOGY_TEST_COV_DIR)?g' $$output_attr_json ; \
	   fi ; \
	 fi

endef

ifneq ($(TESTENV_STRESS),)
# If not specified, run at least 3 iterations and max 13
TESTENV_ITERATIONS?=$(shell echo $$(expr $$RANDOM % 10 + 3))
TESTENV_EXTRA_PARAMETERS=$(if $(VERBOSE),-vv,) -k '$(TESTENV_STRESS)'
else
TESTENV_EXTRA_PARAMETERS=$(if $(VERBOSE),-vv,)
endif
TESTENV_ITERATIONS?=1

_testenv_rerun:
	$(V) $(SUDO) rm -Rf $(BUILDDIR)/test/$(TESTSUITE)/code_under_test
	$(V) mkdir -p $(BUILDDIR)/test/$(TESTSUITE)/code_under_test
	$(V) $(ECHO) "$(BLUE)Collecting the requested code to run $(TESTSUITE) tests...$(GRAY)\n"
	$(V)$(foreach COMPONENT, $(COMPONENTS), $(call TESTENV_PREPARE,$(COMPONENT)))
	$(V) $(ECHO) "\n$(BLUE)Starting $(TESTSUITE) tests execution...$(GRAY)\n"
	$(V) \
	  if [ "$(TESTSUITE)" == "legacy" ] ; then \
	    export VSI_IMAGE_NAME=$(TOPOLOGY_TEST_IMAGE) ;\
            $(ECHO) "\nIterating the tests $(TESTENV_ITERATIONS) times\n" ; \
	    export VSI_COV_DATA_DIR=$(TOPOLOGY_TEST_COV_DIR) ;\
	    for iteration in $$(seq 1 $(TESTENV_ITERATIONS)) ; do \
	      $(ECHO) "\nRunning the testsuite on iteration $$iteration" ; \
	      $(MAKE) devenv_ct_test PY_TEST_ARGS="$(TESTENV_EXTRA_PARAMETERS) --exitfirst --junitxml=$(BUILDDIR)/test/$(TESTSUITE)/test-results.xml $(BUILDDIR)/test/$(TESTSUITE)/code_under_test" || exit 1 ; \
	    done ; \
	  else \
	    if [[ tools/topology/requirements.txt -nt $(BUILDDIR)/test/$(TESTSUITE)/.tox ]] ; then \
	      rm -Rf $(BUILDDIR)/test/$(TESTSUITE)/.tox ; \
	    fi ; \
            $(ECHO) "\nIterating the tests $(TESTENV_ITERATIONS) times\n" ; \
	    for iteration in $$(seq 1 $(TESTENV_ITERATIONS)) ; do \
	      $(ECHO) "\nRunning the testsuite on iteration $$iteration" ; \
	      cd $(BUILDDIR)/test/$(TESTSUITE) ; unset CURL_CA_BUNDLE; export TESTENV_EXTRA_PARAMETERS='$(TESTENV_EXTRA_PARAMETERS)' ; tox || exit 1 ; \
            done ; \
	  fi

# We try to export this symbol only when the target is invoked, since the expansion
# can cause to trigger bitbake before is configured in other cases
ifneq ($(findstring testenv,$(MAKECMDGOALS)),)
  export YOCTO_LAYERS
endif

testenv_suite_list: _testenv_header
	$(V) for layer in $$YOCTO_LAYERS ; do \
	   test -d $$layer || continue ; \
	   TESTSUITES="$$TESTSUITES `find $$layer -name testsuites.conf`" ; \
	 done ; \
	 for testsuiteconf in $$TESTSUITES ; do \
	   while read suite ; do \
	     [[ $$suite == \#* ]] && continue ; \
	     $(ECHO) "  * $$suite" ; \
	   done < $$testsuiteconf ; \
	done
	$(V)$(ECHO)

ifeq (testenv_suite_list_components,$(firstword $(MAKECMDGOALS)))
  $(eval $(call PARSE_ARGUMENTS,testenv_suite_list_components))
  export TESTSUITE?=$(EXTRA_ARGS)
  ifeq ($(TESTSUITE),)
   $(error ====== TESTSUITE variable is empty, please specify which test suite you want =====)
  endif
endif
testenv_suite_list_components: _testenv_header
	$(V) for layer in $$YOCTO_LAYERS ; do \
	   test -d $$layer || continue ; \
	   if [ -f $$layer/testsuite_$(TESTSUITE).conf ] ; then \
		 while read component ; do \
		     [[ $$component == \#* ]] && continue ; \
			 $(ECHO) "  * $$component" ; \
	     done < $$layer/testsuite_$(TESTSUITE).conf ; \
	   fi ; \
	 done
	 $(V) $(ECHO)

ifeq (testenv_suite_run,$(firstword $(MAKECMDGOALS)))
  $(eval $(call PARSE_ARGUMENTS,testenv_suite_run))
  export TESTSUITE?=$(EXTRA_ARGS)
  ifeq ($(TESTSUITE),)
   $(error ====== TESTSUITE variable is empty, please specify which test suite you want =====)
  endif
endif
testenv_suite_run: _testenv_header
	$(V) $(MAKE) _fs
	$(V) docker rmi $(TOPOLOGY_TEST_IMAGE) > /dev/null 2>&1 || true
	$(V) $(MAKE) export_docker_image $(TOPOLOGY_TEST_IMAGE)
	$(V) $(SUDO) rm -Rf $(BUILDDIR)/test/$(COMPONENT)/$(TESTSUITE)
	$(V) $(MAKE) _testenv_suite_rerun

$(eval $(call PARSE_ARGUMENTS,testenv_suite_rerun))
ifneq ($(findstring testenv_suite_rerun,$(MAKECMDGOALS)),)
  export TESTSUITE?=$(EXTRA_ARGS)
  ifeq ($(TESTSUITE),)
   $(error ====== TESTSUITE variable is empty, please specify which test suite you want =====)
  endif
endif
testenv_suite_rerun: _testenv_header
	$(V) $(MAKE) _testenv_suite_rerun

_testenv_suite_rerun:
	$(V) for layer in $$YOCTO_LAYERS ; do \
	   test -d $$layer || continue ; \
	   if [ -f $$layer/testsuite_$(TESTSUITE).conf ] ; then \
		 while read component ; do \
		    [[ $$component == \#* ]] && continue ; \
			COMPONENTS="$$COMPONENTS $$component" ; \
	     done < $$layer/testsuite_$(TESTSUITE).conf ; \
	   fi ; \
	 done ; \
	 if [ -z "$$COMPONENTS" ] ; then \
	   $(call FATAL_ERROR, No components where found for the test suite. Do you have testsuite_$(TESTSUITE).conf files?); \
	 else \
	   $(MAKE) _testenv_rerun TESTSUITE=$(TESTSUITE) COMPONENTS="$$COMPONENTS" ; \
	 fi

testenv_clean:
	$(V) rm -Rf .testenv build/test

# Legacy commands used to run the deprecated test framework (VSI)
.PHONY: devenv_ct_init devenv_ct_test _devenv_ct_init

devenv_ct_init: dev_header _devenv_ct_init

_devenv_ct_init:
	$(V)$(call BITBAKE,ops-ft-framework-native)
	$(V) /bin/mkdir -p src
	$(V) /bin/cp tools/pytest.ini src/pytest.ini
	$(V) if [ ! -f .sandbox_uuid ] ; then \
	  echo "$$($(UUIDGEN_NATIVE) -t)" >.sandbox_uuid; \
	fi
	$(V) $(SUDO) modprobe bonding

# Sandbox unique ID is used as a prefix to the name
# while creating docker instances to run tests.
# Docker can't use the entire UUID as container name,
# so using only the fifth field of the UUID.
$(eval $(call PARSE_ARGUMENTS,devenv_ct_test))
PY_TEST_ARGS:=$(EXTRA_ARGS)
ifeq ($(PY_TEST_ARGS),)
PY_TEST_ARGS=src
endif
devenv_ct_test:
	$(V) $(SUDO) PYTHONDONTWRITEBYTECODE=0 PATH=$(STAGING_DIR_NATIVE)/usr/bin:/sbin:$$PATH \
	  SANDBOX_UUID=$$(cat .sandbox_uuid | cut -d '-' -f 5) $(PYTEST_NATIVE) $(PY_TEST_ARGS)

devenv_ct_clean:
	$(V) SBOX_UUID=$$(cat .sandbox_uuid | cut -d '-' -f 5) ; \
	for name in `docker ps -a -q --filter="name=$$SBOX_UUID"`; do \
	  echo "Cleaning the docker container with id $$SBOX_UUID" ; \
	  docker stop --time=5 $$name >/dev/null ; \
	  docker rm -f $$name >/dev/null ; \
	done
	$(V) rm -rf .sandbox_uuid

# Trim Support
.PHONY: trim
trim: header
	$(V) $(ECHO) "$(BLUE)Trimming build directory to reduce size...$(GRAY)\n"
	$(V) $(ECHO) "Removing old images..."
	$(V) find -L $(DEPLOY_DIR_IMAGE_ALL) -xtype l | xargs readlink > $(BUILDDIR)/trim_keep
	$(V) find -L $(DEPLOY_DIR_IMAGE_ALL) -xtype l | xargs readlink | awk -F\- '{$$NF=""; OFS="-"; print}' > $(BUILDDIR)/trim_patterns
	$(V) while read PATTERN ; do \
		find $(DEPLOY_DIR_IMAGE_ALL) -name $${PATTERN}* | grep -vf $(BUILDDIR)/trim_keep >> $(BUILDDIR)/trim_duplicated || true ; \
	 done < $(BUILDDIR)/trim_patterns
	$(V)awk '!a[$$0]++' $(BUILDDIR)/trim_duplicated > $(BUILDDIR)/trim_delete
	$(V)for file in `cat $(BUILDDIR)/trim_delete` ; do echo "  Removing $$file..." ;rm $$file ; done
	$(V)rm -f $(BUILDDIR)/trim_*
	$(V) $(ECHO) "\nCleaning workdir..."
	$(V)cd build ; ../yocto/poky/scripts/cleanup-workdir
	$(V) $(ECHO) "\nCleaning duplicated shared states..."
	$(V)cd build ; ../yocto/poky/scripts/sstate-cache-management.sh -d -L -y --cache-dir=$$(awk -F\" '/SSTATE_DIR =/ { print $$2 } ' $(BUILDDIR)/conf/local.conf)
	$(V) $(ECHO) "\nTrimming completed."

# Git support
.PHONY: changelog_manifest
changelog_manifest: header
	$(V) $(ECHO) "$(BLUE)Generating Change Log Manifest ...$(GRAY)\n"
	$(V) $(BUILD_ROOT)/tools/bin/generate_changelog_manifest.py

# Git support
.PHONY: git_pull
git_pull: header
	$(V)$(ECHO) "Updating the base git repository..."
	$(V)git pull --rebase || $(ECHO) "${RED}WARNING: git pull failed, skipping this error$(GRAY)"
	$(V)$(ECHO) "Finding other .git dirs we need to update (skipping ./build and ./src)..."
	$(V)for gitpath in `find ./ -maxdepth 4 -path ./.git -prune -o -path ./build -prune -o -path ./src -prune -o -name .git -prune -print` ; do \
	   repo=`dirname $$gitpath` ; \
	   $(ECHO) "\nUpdating the $$repo git repository..." ; \
	   pushd . >/dev/null ; \
	   cd $$repo ; \
	   git pull --rebase || $(ECHO) "${RED}WARNING: git pull failed, skipping this error$(GRAY)" ; \
	   popd >/dev/null ; \
	 done
	$(V)if test -f .devenv ; then \
	  echo ; $(MAKE) _devenv_refresh ; \
	 else \
	  $(ECHO) "\n$(PURPLE)Update completed$(GRAY)" ; \
	 fi

## Support commands
## Use with caution!!!!

$(eval $(call PARSE_ARGUMENTS,share_screen_with))
ifneq ($(findstring share_screen_with,$(MAKECMDGOALS)),)
  USERTOSHARE?=$(EXTRA_ARGS)
  ifeq ($(USERTOSHARE),)
   $(error ====== USERTOSHARE variable is empty, please specify an user =====)
  endif
endif
share_screen_with:
	$(V) $(ECHO) "Enabling shared console with user: $(USERTOSHARE)..."
	$(V) $(ECHO) "  Enabling suid in screen binary and fixing permissions, may need root password..."
	$(V) $(SUDO) chmod +s `which screen`
	$(V) $(SUDO) chmod g-w /var/run/screen
	$(V) $(ECHO) "  Starting shared screen session..."
	$(V) screen -d -m -S shared-with-$(USERTOSHARE) ; \
	 sleep 1 ; \
	 screen -x shared-with-$(USERTOSHARE) -X multiuser on ; \
	 screen -x shared-with-$(USERTOSHARE) -X acladd $(USERTOSHARE) ; \
	 screen -x shared-with-$(USERTOSHARE) -r
	$(V) $(ECHO) "  Disabling suid and restoring permissions..."
	$(V) $(SUDO) chmod -s `which screen`
	$(V) $(SUDO) chmod g+w /var/run/screen

$(eval $(call PARSE_ARGUMENTS,attach_screen_with))
ifneq ($(findstring attach_screen_with,$(MAKECMDGOALS)),)
  USERTOSHARE?=$(EXTRA_ARGS)
  ifeq ($(USERTOSHARE),)
    $(error ====== USERTOSHARE variable is empty, please specify an user =====)
  endif
endif
attach_screen_with:
	$(V)$(ECHO) "Attaching to a shared screen by user: $(USERTOSHARE)..."
	$(V)screen -x $(USERTOSHARE)/shared-with-$(USER)
	$(V)$(ECHO) "Leaving shared screen"
