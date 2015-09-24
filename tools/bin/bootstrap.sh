#!/bin/bash

# Definitions
SERVER_URL=https://archive.openswitch.net/
CORKSCREW=corkscrew-2.0
CORKSCREW_PACKAGE=$CORKSCREW.tar.gz
BOOTSTRAP_TMP=/tmp/$DISTRO-tmp-$USER/

error () {
    echo
    echo -e "\033[31mERROR\033[0m: $1"
    echo
    exit 255
}

if [[ $VERBOSE ]] ; then
    REDIRECT=\&1
else
    REDIRECT=/dev/null
fi

setup_corkscrew (){
    echo "Setting up Corkscrew"
    pushd . > /dev/null
    cd $BOOTSTRAP_TMP
    if [ ! -f $CORKSCREW_PACKAGE ] ; then
        echo curl $SERVER_URL/$CORKSCREW_PACKAGE 
        curl $SERVER_URL/$CORKSCREW_PACKAGE > $CORKSCREW_PACKAGE || { rm -f $CORKSCREW_PACKAGE ; exit 255 ; }
    fi
    if [ ! -f $CORKSCREW/Makefile ] ; then
        tar xzf $CORKSCREW_PACKAGE || exit 255
    fi
    echo -n "  Building corkscrew..."
    cd $CORKSCREW
    ./configure --prefix=$BUILD_ROOT/tools/ >$REDIRECT 2>&1
    make >$REDIRECT
    make install >$REDIRECT
    echo " done"
    popd > /dev/null
    echo "Corkscrew ready"
}

check_binary () {
    if ! which $1 >/dev/null 2>&1 ; then
        error "please install $2 first"
    fi
}

check_file () {
    if test ! -f $1 ; then
        error "please install $2 first"
    fi
}

check_python_version() {
   if [ -f /usr/bin/python2.6 ] ; then
      ln -sf /usr/bin/python26 tools/bin/python
   elif [ -f /usr/bin/python2.7 ] ; then
     ln -sf /usr/bin/python2.7 tools/bin/python
   else
     error "Please install Python 2.6 or higher"
   fi
}

sanity_checks (){
    echo "Running sanity check of installed packages"
    # Required by our configuration system with screen
    check_binary screen screen
    # Required to get packages
    check_binary curl curl
    # Required by yocto
    check_python_version
    check_binary chrpath chrpath
    # Required to build itb files
    check_binary dtc device-tree-compiler
    if ! lsb_release -d | grep -q "[Red Hat|CentOS]" ; then
        if test `uname -m` = x86_64 ; then
            check_file /usr/include/bits/predefs.h  libc6-dev-i386
        fi
    fi
}


mkdir -p $BOOTSTRAP_TMP
# Check we have everything we need
sanity_checks

# Setup crokscrew if not native version is found
if ! which corkscrew >/dev/null 2>&1 ; then
    setup_corkscrew
fi

