# The installer deploys certain binaries without write permissions for the
# owner, causing recent versions of patchelf to fail during set_scene stage with:
# open: Permission denied
# Fixing the permissions solve the issue

do_install_append() {
    for f in ${D}/${bindir}/* ; do
        chmod u+w "$f"
    done
}
