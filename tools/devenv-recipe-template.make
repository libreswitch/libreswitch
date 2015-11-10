
###RECIPE##
.PHONY: ##RECIPE##-build ##RECIPE##-clean ##RECIPE##-deploy ##RECIPE##-undeploy

##RECIPE##-build:
	$(V)$(call DEVTOOL, build ##RECIPE##)

##RECIPE##-clean:
	$(V)$(call BITBAKE, -c cleansstate ##RECIPE##)

##RECIPE##-reconfigure:
	$(V)$(call BITBAKE, -f -c configure ##RECIPE##)

##RECIPE##-sca-analysis:
	$(V)if ! which $(SCA_TOOL) > /dev/null ; then \
		$(call FATAL_ERROR,unable to find the tool $(SCA_TOOL) used by the static analysis toolchain ($(SCA_TOOLCHAIN))) ; \
	 fi
	$(V)$(ECHO) "$(BLUE)Running static analysis ($(SCA_TOOLCHAIN))...$(GRAY)\n"
	$(V) cd src/##RECIPE## ; $(SCA_TOOL) $(SCA_TOOL_SCAN_CMD)

$(eval $(call PARSE_ARGUMENTS,##RECIPE##-deploy))
TARGET?=$(EXTRA_ARGS)
ifneq ($(findstring ##RECIPE##-deploy,$(MAKECMDGOALS)),)
  ifeq ($(TARGET),)
    $(error ====== TARGET variable is empty, please specify where to deploy =====)
  endif
endif
##RECIPE##-deploy:
	$(V)$(call DEVTOOL, deploy-target -s ##RECIPE## $(TARGET))

$(eval $(call PARSE_ARGUMENTS,##RECIPE##-undeploy))
TARGET?=$(EXTRA_ARGS)
ifneq ($(findstring ##RECIPE##-undeploy,$(MAKECMDGOALS)),)
  ifeq ($(TARGET),)
    $(error ====== TARGET variable is empty, please specify where to undeploy from  =====)
  endif
endif
##RECIPE##-undeploy:
	$(V)$(call DEVTOOL, undeploy-target -s ##RECIPE## $(TARGET))

##RECIPE##-nfs-deploy:
	$(V)$(call DEVTOOL, deploy-target -s ##RECIPE## localhost:$(NFSROOTPATH))

##RECIPE##-nfs-undeploy:
	$(V)$(call DEVTOOL, undeploy-target -s ##RECIPE## localhost:$(NFSROOTPATH))

-include src/##RECIPE##/Rules-ops-build.make
#END_##RECIPE##
