
###RECIPE##
.PHONY: ##RECIPE##-build ##RECIPE##-clean ##RECIPE##-deploy ##RECIPE##-undeploy

##RECIPE##-build:
	$(V)$(call DEVTOOL, build ##RECIPE##)

##RECIPE##-clean:
	$(V)$(call BITBAKE, -c cleansstate ##RECIPE##)

##RECIPE##-reconfigure:
	$(V)$(call BITBAKE, -f -c configure ##RECIPE##)

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

##RECIPE##-ct-test:
	$(V) if [ -d src/##RECIPE##/ct-test ] && [ -f  src/##RECIPE##/ct-test/Makefile ]; then \
		$(MAKE) -C src/##RECIPE##/ct-test; \
    else \
        $(ECHO) "$(RED)No component tests found for ##RECIPE##. $(GRAY)"; \
	fi

##RECIPE##-ct-test-clean:
	$(V) if [ -d src/##RECIPE##/ct-test ] && [ -f  src/##RECIPE##/ct-test/Makefile ]; then \
		$(MAKE) -C src/##RECIPE##/ct-test clean; \
	fi
#END_##RECIPE##
