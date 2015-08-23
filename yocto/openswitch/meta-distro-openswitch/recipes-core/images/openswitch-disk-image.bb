DESCRIPTION = "OpenSwitch Disk Rootfs Image"

IMAGE_LINGUAS = " "

LICENSE = "Apache-2.0"

# For x86 machines we need to add grub and kernel source
CORE_IMAGE_EXTRA_INSTALL_append_x86-64 = "grub kernel-image"

inherit openswitch-image
