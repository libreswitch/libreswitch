
###RECIPE##
.PHONY: ##RECIPE##

##RECIPE##:
	$(V)$(call DEVTOOL, build ##RECIPE##)

##RECIPE##-clean:
	$(V)$(call BITBAKE, -c sstate ##RECIPE##)

ifneq ($(findstring ##RECIPE##-deploy,$(MAKECMDGOALS)),)
  ifeq ($(TARGET),)
    $(error ====== TARGET variable is empty, please specify where to deploy =====)
  endif
endif
##RECIPE##-deploy:
	$(V)$(call DEVTOOL, deploy ##RECIPE## $(TARGET)

ifneq ($(findstring ##RECIPE##-undeploy,$(MAKECMDGOALS)),)
  ifeq ($(TARGET),)
    $(error ====== TARGET variable is empty, please specify where to undeploy from  =====)
  endif
endif
##RECIPE##-undeploy:
	$(V)$(call DEVTOOL, undeploy ##RECIPE## $(TARGET)
#END_##RECIPE##
