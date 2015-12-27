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

.PHONY: all clean configure distclean header switch-platform show-platform help

# This allows an overlay layer to basically override the whole environment
-include yocto/*/.distro_override

DISTRO ?= openswitch
export DISTRO
# Pull distribution specific rules
include yocto/$(DISTRO)/Rules.make

# Useful variables
SHELL=/bin/bash
V=$(if $(VERBOSE),,@)
ECHO=echo -e
MAKE=make $(if $(VERBOSE),,-s)
SUDO=/usr/bin/sudo -E

# Remove known troublesome directories from PATH.
# More directories can be added to RDIRS separated by '|'.
RDIRS:=^.
PATH:=$(shell echo $(PATH) | tr -s ":" "\n" | grep -vwE "($(RDIRS))" | tr -s "\n" ":" | sed 's/:$$//')

ifneq ($(VERBOSE),)
 export VERBOSE
endif

# Console color escape sequences for bold/bright colors
ifeq ($(MAKE_TERMOUT),)
RED=\033[1;31m
BLUE=\033[1;34m
PURPLE=\033[1;35m
CYAN=\033[1;36m
#This is actually "reset"
GRAY=\033[0m
else
RED=
BLUE=
PURPLE=
CYAN=
GRAY=
endif

CONFIGURED_PLATFORM=$(shell if [ -f .platform ] ; then cat .platform ; else echo undefined ; fi)

PLATFORMS = $(shell find yocto/$(DISTRO) -name meta-platform* -printf "%P " | sed -e 's/meta-platform-$(DISTRO)-//g')
PLATFORMS:=$(sort $(PLATFORMS))

# Parameters: first argument is the fatal error message
define FATAL_ERROR
	$(ECHO) ; \
	 $(ECHO) "$(RED)ERROR:$(GRAY) $(1)" ; \
	 $(ECHO) ; \
	 exit 1
endef

define PARSE_ARGUMENTS
ifeq ($(1),$(firstword $(MAKECMDGOALS)))
  # use the rest as arguments for "$(1)"
  EXTRA_ARGS := $(wordlist 2,$(words $(MAKECMDGOALS)),$(MAKECMDGOALS))
  # ...and turn them into do-nothing targets
  $(eval $(EXTRA_ARGS)::;@:)
endif
endef

export BUILD_ROOT=$(CURDIR)

all:: header

help::
	@$(ECHO) "$(CYAN)Build System for $(DISTRO)$(GRAY)\n"
	@$(ECHO) "$$HELP_TEXT"

ifeq ($(CONFIGURED_PLATFORM),undefined)
define HELP_TEXT
The platform type is not configured.  To configure it, please run:

    $(BLUE)make configure $(GRAY)<platform>

where <platform> is one of: $(PURPLE)$(PLATFORMS)$(GRAY)

endef
else
define HELP_TEXT
Platform: $(PURPLE)$(CONFIGURED_PLATFORM)$(GRAY)

The following primary targets are available:

    $(BLUE)all$(GRAY)                 : build the default targets for the platform
    $(BLUE)fs$(GRAY)                  : build the File System (if the FS is decoupled from the kernel)
    $(BLUE)kernel$(GRAY)              : build the Linux kernel
    $(BLUE)sdk$(GRAY)                 : build the SDK installer for the platform

Additional development/maintenance targets:

    $(BLUE)bake RECIPE= $(GRAY)       : bitbake the specified RECIPE, e.g. RECIPE="-s" shows all package names
    $(BLUE)clean$(GRAY)               : cleans the build but not the shared state
    $(BLUE)cleansstate RECIPE= $(GRAY): clean RECIPE including shared state, keeps any download
    $(BLUE)distclean$(GRAY)           : removes generated files, and returns to a configurable state
    $(BLUE)devshell RECIPE= $(GRAY)   : start a devshell for RECIPE
    $(BLUE)help$(GRAY)                : this, see also $(BLUE)$(DISTRO_HELP_LINK)$(GRAY)
    $(BLUE)switch-platform$(GRAY)     : change to a different platform
    $(BLUE)show-platform$(GRAY)       : show current platform

endef
endif
export HELP_TEXT

header:: .platform
	@$(ECHO) "$(CYAN)Build System for $(DISTRO)$(GRAY)\n"
	@$(ECHO) "Platform: $(PURPLE)$(CONFIGURED_PLATFORM)$(GRAY)"
	@$(ECHO)

.platform:
ifneq ($(MAKECMDGOALS),help)
	@$(ECHO) "$$HELP_TEXT"
	@$(call FATAL_ERROR,$(DISTRO) is not configured)
endif

$(eval $(call PARSE_ARGUMENTS,configure))
PLATFORM?=$(EXTRA_ARGS)
configure::
	@$(ECHO) "$(CYAN)Build System for $(DISTRO)$(GRAY)"
	$(V)$(MAKE) _configure PLATFORM=$(PLATFORM)

.PHONY: _configure
_configure:
	$(V) if [ -f .platform ] ; then \
	    $(call FATAL_ERROR,$(DISTRO) is already configured; you need to run distclean to change the configuration) ; \
	  fi
	$(V)\
	 if ! [ -d yocto/*/meta-platform-$(DISTRO)-$(PLATFORM) ] || [ "$(PLATFORM)" == "" ] ; then \
	    $(call FATAL_ERROR,Unknown platform \"$(PLATFORM)\"; choose from {$(PURPLE)$(PLATFORMS)$(GRAY)}) ; \
	 fi ;
	@$(ECHO) "Configuring for platform $(PLATFORM)...\n"
	@$(ECHO) "\n$(PURPLE)Configuring yocto...$(GRAY)"
	$(V) \
	 mkdir -p build/conf ; rm -f build/conf/*.conf ; \
	 sed -e "s|##YOCTO_ROOT##|$(BUILD_ROOT)/yocto/poky|" tools/config/bblayers.conf.in > build/conf/bblayers.conf ; \
	 for repo in yocto/*/meta* ; do \
	    [[ "$$repo" =~ "yocto/poky" ]] && continue ; \
	    repo_name=`basename $$repo` ; \
	    if [[ "$$repo_name" =~ meta-distro.* ]] ; then \
	        layer_dep=`cat $(BUILD_ROOT)/$$repo/.layer_dep 2>/dev/null` ; \
	        test -z "$$layer_dep" || echo "  $(BUILD_ROOT)/$$layer_dep \\" >> build/conf/bblayers.conf ; \
	        [ "$$repo_name" == "meta-distro-$(DISTRO)" ] && echo "  $(BUILD_ROOT)/$$repo \\" >> build/conf/bblayers.conf ; \
	    elif [[ "$$repo_name" =~ meta-platform-.* ]] ; then \
	        continue ; \
	    else \
	        echo "  $(BUILD_ROOT)/$$repo \\" >> build/conf/bblayers.conf ; \
	    fi ; \
	 done ; \
	 for repo in yocto/*/meta-platform-$(DISTRO)-* ; do \
	    [[ "$$repo" =~ "^yocto/poky" ]] && continue ; \
	    cp build/conf/bblayers.conf build/conf/bblayers.conf-$${repo##*platform-} ; \
	    layer_dep=`cat $(BUILD_ROOT)/$$repo/.layer_dep 2>/dev/null` ; \
	    test -z "$$layer_dep" || echo "  $(BUILD_ROOT)/$$layer_dep \\" >> build/conf/bblayers.conf-$${repo##*platform-} ; \
	    echo -e "  $(BUILD_ROOT)/$$repo \\ \n\"" >> build/conf/bblayers.conf-$${repo##*platform-} ; \
	 done ; \
	 ln -sf bblayers.conf-$(DISTRO)-$(PLATFORM) build/conf/bblayers.conf
	$(V) mkdir -p images
	$(V) tools/bin/bootstrap.sh
	$(V) echo $(PLATFORM) > .platform
	$(V) $(ECHO) "\n$(PURPLE)Configuration completed successfully!\n$(GRAY)"

$(eval $(call PARSE_ARGUMENTS,switch-platform))
PLATFORM?=$(EXTRA_ARGS)
switch-platform: header _switch-platform

_switch-platform:
	$(V)\
	 if [ "$(PLATFORM)" == "" ] ; then \
            $(call FATAL_ERROR,Set the environment variable PLATFORM to select the new platform) ; \
     fi ;\
	 if ! [ -d yocto/*/meta-platform-$(DISTRO)-$(PLATFORM) ] ; then \
            $(call FATAL_ERROR,Unknown platform \"$(PLATFORM)\"; choose from {$(PURPLE)$(PLATFORMS)$(GRAY)}) ; \
     fi ; \
	 $(ECHO) -n Switching to platform $(PLATFORM)... ; \
	 ln -sf bblayers.conf-$(DISTRO)-$(PLATFORM) build/conf/bblayers.conf ; \
	 if [ -f .devenv ] ; then \
		if ! grep -q "$(BUILD_ROOT)/build/workspace" build/conf/bblayers.conf ; then \
			sed --follow-symlinks -i 's|\(.*$(BUILD_ROOT)/yocto/.*/meta-platform-$(DISTRO)-$(PLATFORM) \\\)|\1\n  $(BUILD_ROOT)/build/workspace \\|' build/conf/bblayers.conf ; \
		fi ;\
	 fi ; \
	 echo $(PLATFORM) > .platform ; \
	 echo -e " done\n"

show-platform: header

clean:: header
	$(V)$(ECHO) "Cleaning..."
	$(V)rm -Rf build/{tmp,cache,bitbake.lock}
	$(V)$(ECHO) "Cleaning completed.\n"

distclean::
	$(V)$(ECHO) "$(PURPLE)Distcleaning...$(GRAY)"
	$(V)rm -Rf .platform .devenv images src build nfsroot* tools/bin/{corkscrew,python}
	$(V)find -type l -lname 'images/*' -print0 | xargs -r0 rm -f
	$(V)$(ECHO) "Distcleaning completed. You need to reconfigure to build again\n"

include tools/Rules.make
