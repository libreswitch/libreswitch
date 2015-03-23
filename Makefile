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

.PHONY: all clean configure distclean header switch-platform help

# Useful variables
SHELL=/bin/bash
V=$(if $(VERBOSE),,@)
ECHO=echo -e
MAKE=make $(if $(VERBOSE),,-s)

# Remove known troublesome directories from PATH.
# More directories can be added to RDIRS separated by '|'.
RDIRS:=^.
PATH:=$(shell echo $(PATH) | tr -s ":" "\n" | grep -vwE "($(RDIRS))" | tr -s "\n" ":" | sed 's/:$$//')

ifneq ($(VERBOSE),)
 export VERBOSE
endif

# Console color escape sequences for bold/bright colors
RED=\033[1;31m
GREEN=\033[1;32m
YELLOW=\033[1;33m
BLUE=\033[1;34m
PURPLE=\033[1;35m
CYAN=\033[1;36m
WHITE=\033[1;37m
#This is actually "reset"
GRAY=\033[0m

CONFIGURED_PLATFORM=$(shell if [ -f .platform ] ; then cat .platform ; else echo undefined ; fi)

PLATFORMS = $(shell find yocto/* -name meta-platform* -printf "%P " | sed -e 's/meta-platform-//g')

# Parameters: first argument is the fatal error message
define FATAL_ERROR
	$(ECHO) ; \
	 $(ECHO) "$(RED)ERROR:$(GRAY) $(1)" ; \
	 $(ECHO) ; \
	 exit 1
endef

export HALON_ROOT=$(PWD)

all:: header

help:: header
	@$(ECHO) "$$HALON_HELP_TEXT"

define HALON_HELP_TEXT
The following targets are available:

    $(YELLOW)configure$(GRAY)       : configs the project for specific platform hardware, and bootstraps the build system
    $(YELLOW)switch-platform$(GRAY) : change the project to a different platform 
    $(YELLOW)distclean$(GRAY)       : removes generated files, and returns to a configurable state
    $(YELLOW)help$(GRAY)            : this, see also $(BLUE)https://wiki.openhalon.io/wiki/index.php/Getting_started_with_Halon$(GRAY)

After configuring the project, these additional targets are available:

    $(YELLOW)all$(GRAY)    : build the default targets for the platform
    $(YELLOW)fs$(GRAY)     : build the File System (if the FS is decoupled from the kernel)
    $(YELLOW)kernel$(GRAY) : build the Linux kernel
    $(YELLOW)sdk$(GRAY)    : build the SDK installer for the platform

Maintenance/development targets:

    $(YELLOW)bake RECIPE= $(GRAY)       : bitbake the specified RECIPE, e.g. RECIPE="-s" shows all package names
    $(YELLOW)clean$(GRAY)               : cleans the build but not the shared state
    $(YELLOW)cleansstate RECIPE= $(GRAY): clean RECIPE including shared state, keeps any download
    $(YELLOW)devshell RECIPE= $(GRAY)   : start a devshell for RECIPE

endef
export HALON_HELP_TEXT

header:: .platform
	@$(ECHO) "$(RED)Halon Build System$(GRAY)\n"
	@$(ECHO) "Platform: $(GREEN)$(CONFIGURED_PLATFORM)$(GRAY)"
	@$(ECHO)

.platform:
ifneq ($(MAKECMDGOALS),help)
	@$(call FATAL_ERROR,Halon is not configured; run make configure)
endif

configure:
	@$(ECHO) "$(RED)Halon Build System$(GRAY)"
	$(V) if [ -f .platform ] ; then \
	    $(call FATAL_ERROR,Halon is already configured; you need to run distclean to change the configuration) ; \
	  fi
	$(V)\
	 if [ "$(PLATFORM)" == "" ] ; then \
	    $(call FATAL_ERROR,Set the environment variable PLATFORM to select a platform) ; \
	 fi ;\
	 if ! [ -d yocto/*/meta-platform-$(PLATFORM) ] ; then \
	    $(call FATAL_ERROR,Unknown platform \"$(PLATFORM)\"; choose from {$(GREEN)$(PLATFORMS)$(GRAY)}) ; \
	 fi ;
	@$(ECHO) "Configuring for platform $(PLATFORM)...\n"
	@$(ECHO) "\n$(GREEN)Configuring yocto...$(GRAY)"
	$(V) \
	 mkdir -p build/conf ; \
	 sed -e "s|##HALON_YOCTO_ROOT##|$(HALON_ROOT)/yocto/poky|" tools/config/bblayers.conf.in > build/conf/bblayers.conf ; \
	 for repo in yocto/*/meta* ; do \
            [[ "$$repo" =~ "yocto/poky" ]] && continue ; \
            repo_name=`basename $$repo` ; \
            if [[ "$$repo_name" =~ meta-platform.* ]] ; then \
                continue ; \
	    else \
		echo "  $(HALON_ROOT)/$$repo \\" >> build/conf/bblayers.conf ; \
	    fi ; \
	 done ; \
	 for repo in yocto/*/meta-platform* ; do \
            [[ "$$repo" =~ "^yocto/poky" ]] && continue ; \
	    cp build/conf/bblayers.conf build/conf/bblayers.conf-$${repo##*platform-} ; \
	    echo -e "  $(HALON_ROOT)/$$repo \\ \n\"" >> build/conf/bblayers.conf-$${repo##*platform-} ; \
	 done ; \
	 ln -sf bblayers.conf-$(PLATFORM) build/conf/bblayers.conf
	$(V) mkdir -p images
	$(V) tools/bin/bootstrap.sh
	$(V) echo $(PLATFORM) > .platform
	$(V) $(ECHO) "\n$(GREEN)Configuration completed successfully!\n$(GRAY)"

switch-platform: header _switch-platform

_switch-platform:
	$(V)\
	 if [ "$(PLATFORM)" == "" ] ; then \
            $(call FATAL_ERROR,Set the environment variable PLATFORM to select the new platform) ; \
         fi ;\
	 if ! [ -d yocto/*/meta-platform-$(PLATFORM) ] ; then \
            $(call FATAL_ERROR,Unknown platform \"$(PLATFORM)\"; choose from {$(GREEN)$(PLATFORMS)$(GRAY)}) ; \
         fi ; \
	 $(ECHO) -n Switching to platform $(PLATFORM)... ; \
	 ln -sf bblayers.conf-$(PLATFORM) build/conf/bblayers.conf ; \
	 echo $(PLATFORM) > .platform ; \
	 echo -e " done\n"

clean:: header
	$(V)$(ECHO) "Cleaning..."
	$(V)rm -Rf build/{tmp,sstate-cache,cache,bitbake.lock}
	$(V)$(ECHO) "Cleaning completed.\n"

distclean::
	$(V)$(ECHO) "$(GREEN)Distcleaning...$(GRAY)"
	$(V) rm -Rf .platform images build tools/bin/{corkscrew,python}
	$(V)$(ECHO) "Distcleaning completed. You need to reconfigure to build again\n"

include tools/Rules.make
