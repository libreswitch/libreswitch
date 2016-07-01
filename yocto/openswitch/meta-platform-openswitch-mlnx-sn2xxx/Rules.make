# Copyright Mellanox Technologies, Ltd. 2001-2016.

DISTRO_KERNEL_FILE = $(BASE_BZIMAGE_FILE)
DISTRO_FS_FILE = $(BASE_CPIO_FS_FILE)
DISTRO_FS_TARGET = openswitch-disk-image
ONIE_INSTALLER_RECIPE = openswitch-onie-installer
ONIE_INSTALLER_FILE = onie-installer-x86_64-mlnx-sn2xxx

# For this platform we create an onie-installer
all:: onie-installer local_conf_vars

.PHONY: local_conf_vars

header:: local_conf_vars

local_conf_vars: build/conf/local.conf _local_conf_vars

define BB_GLOBAL_VAR_ADD
$(call WARNING,Writing bitbake global variable $(1) to local.conf); \
if [[ `cat $(BUILDDIR)/conf/local.conf | grep $(1)` == "" ]]; then \
  echo $(1) = \"$(2)\" >> $(BUILDDIR)/conf/local.conf; \
else \
  sed -i 's/$(1) =.*/$(1) = "$(2)"/' $(BUILDDIR)/conf/local.conf; \
fi;
endef

.PHONY: _local_conf_vars

_local_conf_vars:
	@$(foreach var,$(subst ;, ,$(BB_EXTRA_GLOBALS)),$(call BB_GLOBAL_VAR_ADD,$(word 1,$(subst =, ,$(var))),$(word 2,$(subst =, ,$(var)))))
