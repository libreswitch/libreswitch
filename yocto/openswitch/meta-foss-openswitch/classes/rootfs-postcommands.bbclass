
# Zap the root password if debug-tweaks feature is not enabled
ROOTFS_POSTPROCESS_COMMAND += '${@bb.utils.contains_any("IMAGE_FEATURES", [ 'debug-tweaks', 'empty-root-password' ], "", "zap_empty_root_password ; ",d)}'

# Allow dropbear/openssh to accept logins from accounts with an empty password string if debug-tweaks is enabled
ROOTFS_POSTPROCESS_COMMAND += '${@bb.utils.contains_any("IMAGE_FEATURES", [ 'debug-tweaks', 'allow-empty-password' ], "ssh_allow_empty_password; ", "",d)}'

# Enable postinst logging if debug-tweaks is enabled
ROOTFS_POSTPROCESS_COMMAND += '${@bb.utils.contains_any("IMAGE_FEATURES", [ 'debug-tweaks', 'post-install-logging' ], "postinst_enable_logging; ", "",d)}'

# Add vagrant user and tweaks
ROOTFS_POSTPROCESS_COMMAND += '${@bb.utils.contains_any("IMAGE_FEATURES", [ 'vagrant-tweaks' ], "vagrant_support; ", "",d)}'

# For vagrant disable SSH DNS lookup
ROOTFS_POSTPROCESS_COMMAND += '${@bb.utils.contains_any("IMAGE_FEATURES", [ 'vagrant-tweaks' ], "ssh_disable_dns_lookup; ", "",d)}'

# Create /etc/timestamp during image construction to give a reasonably sane default time setting
ROOTFS_POSTPROCESS_COMMAND += "rootfs_update_timestamp ; "

# Tweak the mount options for rootfs in /etc/fstab if read-only-rootfs is enabled
ROOTFS_POSTPROCESS_COMMAND += '${@bb.utils.contains("IMAGE_FEATURES", "read-only-rootfs", "read_only_rootfs_hook; ", "",d)}'

# Write manifest
IMAGE_MANIFEST = "${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.manifest"
ROOTFS_POSTPROCESS_COMMAND =+ "write_image_manifest ; "

#Create the "version_detail" file at the same place where manifest is created
VERSION_DETAIL = "${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.version_detail"
ROOTFS_POSTPROCESS_COMMAND += 'write_version_detail;'

# Set default postinst log file
POSTINST_LOGFILE ?= "${localstatedir}/log/postinstall.log"
# Set default target for systemd images
SYSTEMD_DEFAULT_TARGET ?= '${@bb.utils.contains("IMAGE_FEATURES", "x11-base", "graphical.target", "multi-user.target", d)}'
ROOTFS_POSTPROCESS_COMMAND += '${@bb.utils.contains("DISTRO_FEATURES", "systemd", "set_systemd_default_target; ", "", d)}'

ROOTFS_POSTPROCESS_COMMAND += 'empty_var_volatile;'

# Disable DNS lookups, the SSH_DISABLE_DNS_LOOKUP can be overridden to allow
# distros to choose not to take this change
SSH_DISABLE_DNS_LOOKUP ?= " ssh_disable_dns_lookup ; "
ROOTFS_POSTPROCESS_COMMAND_append_qemuall = "${SSH_DISABLE_DNS_LOOKUP}"



#
# A hook function to support read-only-rootfs IMAGE_FEATURES
#
read_only_rootfs_hook () {
	# Tweak the mount option and fs_passno for rootfs in fstab
	sed -i -e '/^[#[:space:]]*\/dev\/root/{s/defaults/ro/;s/\([[:space:]]*[[:digit:]]\)\([[:space:]]*\)[[:digit:]]$/\1\20/}' ${IMAGE_ROOTFS}/etc/fstab

	# If we're using openssh and the /etc/ssh directory has no pre-generated keys,
	# we should configure openssh to use the configuration file /etc/ssh/sshd_config_readonly
	# and the keys under /var/run/ssh.
	if [ -d ${IMAGE_ROOTFS}/etc/ssh ]; then
		if [ -e ${IMAGE_ROOTFS}/etc/ssh/ssh_host_rsa_key ]; then
			echo "SYSCONFDIR=/etc/ssh" >> ${IMAGE_ROOTFS}/etc/default/ssh
			echo "SSHD_OPTS=" >> ${IMAGE_ROOTFS}/etc/default/ssh
		else
			echo "SYSCONFDIR=/var/run/ssh" >> ${IMAGE_ROOTFS}/etc/default/ssh
			echo "SSHD_OPTS='-f /etc/ssh/sshd_config_readonly'" >> ${IMAGE_ROOTFS}/etc/default/ssh
		fi
	fi

	# Also tweak the key location for dropbear in the same way.
	if [ -d ${IMAGE_ROOTFS}/etc/dropbear ]; then
		if [ -e ${IMAGE_ROOTFS}/etc/dropbear/dropbear_rsa_host_key ]; then
			echo "DROPBEAR_RSAKEY_DIR=/etc/dropbear" >> ${IMAGE_ROOTFS}/etc/default/dropbear
		else
			echo "DROPBEAR_RSAKEY_DIR=/var/lib/dropbear" >> ${IMAGE_ROOTFS}/etc/default/dropbear
		fi
	fi


	if ${@bb.utils.contains("DISTRO_FEATURES", "sysvinit", "true", "false", d)}; then
		# Change the value of ROOTFS_READ_ONLY in /etc/default/rcS to yes
		if [ -e ${IMAGE_ROOTFS}/etc/default/rcS ]; then
			sed -i 's/ROOTFS_READ_ONLY=no/ROOTFS_READ_ONLY=yes/' ${IMAGE_ROOTFS}/etc/default/rcS
		fi
		# Run populate-volatile.sh at rootfs time to set up basic files
		# and directories to support read-only rootfs.
		if [ -x ${IMAGE_ROOTFS}/etc/init.d/populate-volatile.sh ]; then
			${IMAGE_ROOTFS}/etc/init.d/populate-volatile.sh
		fi
	fi

	if ${@bb.utils.contains("DISTRO_FEATURES", "systemd", "true", "false", d)}; then
	    # Update user database files so that services don't fail for a read-only systemd system
	    for conffile in ${IMAGE_ROOTFS}/usr/lib/sysusers.d/systemd.conf ${IMAGE_ROOTFS}/usr/lib/sysusers.d/systemd-remote.conf; do
		[ -e $conffile ] || continue
		grep -v "^#" $conffile | sed -e '/^$/d' | while read type name id comment; do
		    if [ "$type" = "u" ]; then
			useradd_params=""
			[ "$id" != "-" ] && useradd_params="$useradd_params --uid $id"
			[ "$comment" != "-" ] && useradd_params="$useradd_params --comment $comment"
			useradd_params="$useradd_params --system $name"
			eval useradd --root ${IMAGE_ROOTFS} $useradd_params || true
		    elif [ "$type" = "g" ]; then
			groupadd_params=""
			[ "$id" != "-" ] && groupadd_params="$groupadd_params --gid $id"
			groupadd_params="$groupadd_params --system $name"
			eval groupadd --root ${IMAGE_ROOTFS} $groupadd_params || true
		    fi
		done
	    done
	fi
}

#
# This function is intended to disallow empty root password if 'debug-tweaks' is not in IMAGE_FEATURES.
#
zap_empty_root_password () {
	if [ -e ${IMAGE_ROOTFS}/etc/shadow ]; then
		sed -i 's%^root::%root:*:%' ${IMAGE_ROOTFS}/etc/shadow
        fi
	if [ -e ${IMAGE_ROOTFS}/etc/passwd ]; then
		sed -i 's%^root::%root:*:%' ${IMAGE_ROOTFS}/etc/passwd
	fi
} 

#
# allow dropbear/openssh to accept root logins and logins from accounts with an empty password string
#
ssh_allow_empty_password () {
	for config in sshd_config sshd_config_readonly; do
		if [ -e ${IMAGE_ROOTFS}${sysconfdir}/ssh/$config ]; then
			sed -i 's/^[#[:space:]]*PermitRootLogin.*/PermitRootLogin yes/' ${IMAGE_ROOTFS}${sysconfdir}/ssh/$config
			sed -i 's/^[#[:space:]]*PermitEmptyPasswords.*/PermitEmptyPasswords yes/' ${IMAGE_ROOTFS}${sysconfdir}/ssh/$config
		fi
	done

	if [ -e ${IMAGE_ROOTFS}${sbindir}/dropbear ] ; then
		if grep -q DROPBEAR_EXTRA_ARGS ${IMAGE_ROOTFS}${sysconfdir}/default/dropbear 2>/dev/null ; then
			if ! grep -q "DROPBEAR_EXTRA_ARGS=.*-B" ${IMAGE_ROOTFS}${sysconfdir}/default/dropbear ; then
				sed -i 's/^DROPBEAR_EXTRA_ARGS="*\([^"]*\)"*/DROPBEAR_EXTRA_ARGS="\1 -B"/' ${IMAGE_ROOTFS}${sysconfdir}/default/dropbear
			fi
		else
			printf '\nDROPBEAR_EXTRA_ARGS="-B"\n' >> ${IMAGE_ROOTFS}${sysconfdir}/default/dropbear
		fi
	fi

	if [ -d ${IMAGE_ROOTFS}${sysconfdir}/pam.d ] ; then
		sed -i 's/nullok_secure/nullok/' ${IMAGE_ROOTFS}${sysconfdir}/pam.d/*
	fi
}

# Add vagrant tweaks
vagrant_support () {
	# Add vagrant user with vagrant password
	echo 'vagrant:x:1001:' >> ${IMAGE_ROOTFS}/etc/group
	if [ -e ${IMAGE_ROOTFS}/etc/shadow ]; then
		echo 'vagrant:$6$l7VkVWhcbM/Po72v$RuxGVpbr2/CaXq4tbdRrU83tODijdu4oi635UgPpWXCt8jZfz.QgGuYfGafvZ2N0dwn/1xlbjuXFtw08rJEFv/:16380:0:99999:7:::' >> ${IMAGE_ROOTFS}/etc/shadow
		echo 'vagrant:x:1001:1001::/home/vagrant:/bin/sh' >> ${IMAGE_ROOTFS}/etc/passwd
	elif [ -e ${IMAGE_ROOTFS}/etc/passwd ]; then
		echo 'vagrant:$6$l7VkVWhcbM/Po72v$RuxGVpbr2/CaXq4tbdRrU83tODijdu4oi635UgPpWXCt8jZfz.QgGuYfGafvZ2N0dwn/1xlbjuXFtw08rJEFv/:1001:1001::/home/vagrant:/bin/sh' >> ${IMAGE_ROOTFS}/etc/passwd
	fi

	# Add vagrand home and insecure keys
	mkdir -p ${IMAGE_ROOTFS}/home/vagrant/.ssh
	echo 'ssh-rsa AAAAB3NzaC1yc2EAAAABIwAAAQEA6NF8iallvQVp22WDkTkyrtvp9eWW6A8YVr+kz4TjGYe7gHzIw+niNltGEFHzD8+v1I2YJ6oXevct1YeS0o9HZyN1Q9qgCgzUFtdOKLv6IedplqoPkcmF0aYet2PkEDo3MlTBckFXPITAMzF8dJSIFo9D8HfdOV0IAdx4O7PtixWKn5y2hMNG0zQPyUecp4pzC6kivAIhyfHilFR61RGL+GPXQ2MWZWFYbAGjyiYJnAmCP3NOTd0jMZEnDkbUvxhMmBYSdETk1rRgm+R4LOzFUGaHqHDLKLX+FIPKcF96hrucXzcWyLbIbEgE98OHlnVYCzRdK8jlqm8tehUc9c9WhQ== vagrant insecure public key' > ${IMAGE_ROOTFS}/home/vagrant/.ssh/authorized_keys
	chown 1001:1001 -R ${IMAGE_ROOTFS}/home/vagrant
	chmod 0600 ${IMAGE_ROOTFS}/home/vagrant/.ssh/authorized_keys
	chmod 0700 ${IMAGE_ROOTFS}/home/vagrant/.ssh
	chmod 0755 ${IMAGE_ROOTFS}/home/vagrant

	# Add password-less sudo
	echo 'vagrant ALL=(ALL) NOPASSWD: ALL' >> ${IMAGE_ROOTFS}/etc/sudoers
}

ssh_disable_dns_lookup () {
	if [ -e ${IMAGE_ROOTFS}${sysconfdir}/ssh/sshd_config ]; then
		sed -i -e 's:#UseDNS yes:UseDNS no:' ${IMAGE_ROOTFS}${sysconfdir}/ssh/sshd_config
	fi
}

#
# Enable postinst logging if debug-tweaks is enabled
#
postinst_enable_logging () {
	mkdir -p ${IMAGE_ROOTFS}${sysconfdir}/default
	echo "POSTINST_LOGGING=1" >> ${IMAGE_ROOTFS}${sysconfdir}/default/postinst
	echo "LOGFILE=${POSTINST_LOGFILE}" >> ${IMAGE_ROOTFS}${sysconfdir}/default/postinst
}

#
# Modify systemd default target
#
set_systemd_default_target () {
	if [ -d ${IMAGE_ROOTFS}${sysconfdir}/systemd/system -a -e ${IMAGE_ROOTFS}${systemd_unitdir}/system/${SYSTEMD_DEFAULT_TARGET} ]; then
		ln -sf ${systemd_unitdir}/system/${SYSTEMD_DEFAULT_TARGET} ${IMAGE_ROOTFS}${sysconfdir}/systemd/system/default.target
	fi
}

# If /var/volatile is not empty, we have seen problems where programs such as the
# journal make assumptions based on the contents of /var/volatile. The journal
# would then write to /var/volatile before it was mounted, thus hiding the
# items previously written.
#
# This change is to attempt to fix those types of issues in a way that doesn't
# affect users that may not be using /var/volatile.
empty_var_volatile () {
	if [ -e ${IMAGE_ROOTFS}/etc/fstab ]; then
		match=`awk '$1 !~ "#" && $2 ~ /\/var\/volatile/{print $2}' ${IMAGE_ROOTFS}/etc/fstab 2> /dev/null`
		if [ -n "$match" ]; then
			find ${IMAGE_ROOTFS}/var/volatile -mindepth 1 -delete
		fi
	fi
}

# Turn any symbolic /sbin/init link into a file
remove_init_link () {
	if [ -h ${IMAGE_ROOTFS}/sbin/init ]; then
		LINKFILE=${IMAGE_ROOTFS}`readlink ${IMAGE_ROOTFS}/sbin/init`
		rm ${IMAGE_ROOTFS}/sbin/init
		cp $LINKFILE ${IMAGE_ROOTFS}/sbin/init
	fi
}

make_zimage_symlink_relative () {
	if [ -L ${IMAGE_ROOTFS}/boot/zImage ]; then
		(cd ${IMAGE_ROOTFS}/boot/ && for i in `ls zImage-* | sort`; do ln -sf $i zImage; done)
	fi
}

insert_feed_uris () {
	
	echo "Building feeds for [${DISTRO}].."

	for line in ${FEED_URIS}
	do
		# strip leading and trailing spaces/tabs, then split into name and uri
		line_clean="`echo "$line"|sed 's/^[ \t]*//;s/[ \t]*$//'`"
		feed_name="`echo "$line_clean" | sed -n 's/\(.*\)##\(.*\)/\1/p'`"
		feed_uri="`echo "$line_clean" | sed -n 's/\(.*\)##\(.*\)/\2/p'`"
		
		echo "Added $feed_name feed with URL $feed_uri"
		
		# insert new feed-sources
		echo "src/gz $feed_name $feed_uri" >> ${IMAGE_ROOTFS}/etc/opkg/${feed_name}-feed.conf
	done
}

python write_image_manifest () {
    from oe.rootfs import image_list_installed_packages
    from oe.utils import format_pkg_list

    deploy_dir = d.getVar('DEPLOY_DIR_IMAGE', True)
    link_name = d.getVar('IMAGE_LINK_NAME', True)
    manifest_name = d.getVar('IMAGE_MANIFEST', True)

    if not manifest_name:
        return

    pkgs = image_list_installed_packages(d)
    with open(manifest_name, 'w+') as image_manifest:
        image_manifest.write(format_pkg_list(pkgs, "ver"))
        image_manifest.write("\n")

    if os.path.exists(manifest_name):
        manifest_link = deploy_dir + "/" + link_name + ".manifest"
        if os.path.lexists(manifest_link):
            if d.getVar('RM_OLD_IMAGE', True) == "1" and \
                    os.path.exists(os.path.realpath(manifest_link)):
                os.remove(os.path.realpath(manifest_link))
            os.remove(manifest_link)
        os.symlink(os.path.basename(manifest_name), manifest_link)
}

# Create the version_detail file.
# Copy the manifest & the version_detail file to the image at "/var/lib/"
python write_version_detail () {
    import oe.packagedata
    from oe.rootfs import image_list_installed_packages
    from shutil import copyfile
    import string
    import re
    from urlparse import urlparse
    import yaml

    srctype_nomodify_list = ["https", "http", "ftp", "file"]

    installed_pkg_list = image_list_installed_packages(d)

    pkgs_info = []

    for pkg in installed_pkg_list:
        pkg_info = {}

        pkg_info["PKG"] = pkg

        sdata = oe.packagedata.read_subpkgdata(pkg, d)

        if 'SRCREV' in sdata.keys():
            pkg_info["SRCREV"] = sdata['SRCREV']
            if (("PV" in sdata.keys()) and ((sdata['PV']).endswith('999'))):
                pkg_info["SRCREV"] = sdata['SRCREV']+"~DIRTY"
        else:
            pkg_info["SRCREV"] = "INVALID"

        if 'PV' in sdata.keys():
            pkg_info["PV"] = sdata['PV']
        else:
            pkg_info["PV"] = "NA"

        if 'SRC_URI' in sdata.keys():
            full_uri_str = ''.join(sdata['SRC_URI'])
            primary_uri_str = (full_uri_str.split())[0]

            parsed_url = urlparse(primary_uri_str)
            pkg_info["TYPE"] = parsed_url.scheme

            if parsed_url.scheme not in srctype_nomodify_list:
                primary_uri_str = primary_uri_str.replace(parsed_url.scheme,
                'https', 1)

            #Remove the "protocol=" clause from the URL
            primary_uri_str = re.sub(r';protocol=(.*);?', '', primary_uri_str)

            pkg_info["SRC_URL"] = str(primary_uri_str)
        else:
            pkg_info["TYPE"] = "other"
            pkg_info["SRC_URL"] = "NA"

        pkgs_info.append(pkg_info)

    with open(d.getVar('VERSION_DETAIL', True), 'w+') as version_detail:
        yaml.dump_all(pkgs_info, version_detail, default_flow_style=False)

    #copy the image manifest & version_detail files to the image at "/var/lib/"
    copyfile((d.getVar('IMAGE_MANIFEST', True)), (d.getVar('IMAGE_ROOTFS', True) + "/var/lib/image.manifest"))
    copyfile((d.getVar('VERSION_DETAIL', True)), (d.getVar('IMAGE_ROOTFS', True) + "/var/lib/version_detail.yaml"))
}

# Can be use to create /etc/timestamp during image construction to give a reasonably 
# sane default time setting
rootfs_update_timestamp () {
	date -u +%4Y%2m%2d%2H%2M%2S >${IMAGE_ROOTFS}/etc/timestamp
}

# Prevent X from being started
rootfs_no_x_startup () {
	if [ -f ${IMAGE_ROOTFS}/etc/init.d/xserver-nodm ]; then
		chmod a-x ${IMAGE_ROOTFS}/etc/init.d/xserver-nodm
	fi
}

rootfs_trim_schemas () {
	for schema in ${IMAGE_ROOTFS}/etc/gconf/schemas/*.schemas
	do
		# Need this in case no files exist
		if [ -e $schema ]; then
			oe-trim-schemas $schema > $schema.new
			mv $schema.new $schema
		fi
	done
}

rootfs_check_host_user_contaminated () {
	contaminated="${WORKDIR}/host-user-contaminated.txt"
	HOST_USER_UID="$(PSEUDO_UNLOAD=1 id -u)"
	HOST_USER_GID="$(PSEUDO_UNLOAD=1 id -g)"

	find "${IMAGE_ROOTFS}" -wholename "${IMAGE_ROOTFS}/home" -prune \
	    -user "$HOST_USER_UID" -o -group "$HOST_USER_GID" >"$contaminated"

	if [ -s "$contaminated" ]; then
		echo "WARNING: Paths in the rootfs are owned by the same user or group as the user running bitbake. See the logfile for the specific paths."
		cat "$contaminated" | sed "s,^,  ,"
	fi
}

# Make any absolute links in a sysroot relative
rootfs_sysroot_relativelinks () {
	sysroot-relativelinks.py ${SDK_OUTPUT}/${SDKTARGETSYSROOT}
}
