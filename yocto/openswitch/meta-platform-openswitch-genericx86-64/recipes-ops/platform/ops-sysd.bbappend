# Copyright (C) 2016 Hewlett Packard Enterprise Development LP

EXTRA_OECMAKE+="${@bb.utils.contains('MACHINE_FEATURES', 'ops-container', '-DUSE_SW_FRU=ON', '',d)}"
