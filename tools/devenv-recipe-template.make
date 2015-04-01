
###RECIPE##
.PHONY: ##RECIPE##-build ##RECIPE##-clean ##RECIPE##-deploy ##RECIPE##-undeploy

##RECIPE##-build:
	$(V)$(call DEVTOOL, build ##RECIPE##)

##RECIPE##-clean:
	$(V)$(call BITBAKE, -c sstate ##RECIPE##)

$(eval $(call PARSE_ARGUMENTS,##RECIPE##-deploy))
TARGET?=$(EXTRA_ARGS)
ifneq ($(findstring ##RECIPE##-deploy,$(MAKECMDGOALS)),)
  ifeq ($(TARGET),)
    $(error ====== TARGET variable is empty, please specify where to deploy =====)
  endif
endif
##RECIPE##-deploy:
	$(V)$(call DEVTOOL, deploy-target ##RECIPE## $(TARGET))

$(eval $(call PARSE_ARGUMENTS,##RECIPE##-deploy))
TARGET?=$(EXTRA_ARGS)
ifneq ($(findstring ##RECIPE##-undeploy,$(MAKECMDGOALS)),)
  ifeq ($(TARGET),)
    $(error ====== TARGET variable is empty, please specify where to undeploy from  =====)
  endif
endif
##RECIPE##-undeploy:
	$(V)$(call DEVTOOL, undeploy-target ##RECIPE## $(TARGET))
#END_##RECIPE##
