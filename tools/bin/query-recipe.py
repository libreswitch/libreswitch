#!/usr/bin/env python

# Copyright (C) 2016 Hewlett-Packard Development Company, L.P.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at:
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""
Parse the appropriate bitbake files for a recipe and return the value for the
requested variables
"""

from __future__ import print_function

import os
import sys
import argparse
import re

# tell python where to find poky devtool python libraries
this_path = os.path.dirname(os.path.realpath(__file__))
poky_path = os.path.realpath(os.path.join(this_path, '..', '..',
                                          'yocto', 'poky'))
sys.path.append(os.path.join(poky_path, 'scripts', 'lib'))



def _parse_recipe(workspace_path, tinfoil, pn, appends=True):
    """Parse package recipe - adapted from poky devtool scripts"""
    import oe.recipeutils

    # Find top recipe file
    recipefile = oe.recipeutils.pn_to_recipe(tinfoil.cooker, pn)
    if not recipefile:
        skipreasons = oe.recipeutils.get_unavailable_reasons(tinfoil.cooker, pn)
        if skipreasons:
            sys.exit('\n'.join(skipreasons))
        else:
            sys.exit("Unable to find any recipe file matching %s" % pn)

    # gather append files
    if appends:
        append_files = tinfoil.cooker.collection.get_file_appends(recipefile)
        # Filter out appends from the workspace
        append_files = [path for path in append_files if
                        not path.startswith(workspace_path)]
    else:
        append_files = []

    return oe.recipeutils.parse_recipe(recipefile, append_files,
                                       tinfoil.config_data)


def _get_git_uri(rd):
    """Extract git url & extras from SCI_URL"""
    for piece in rd.getVar('SRC_URI', expand=True).split():
        if piece.startswith('git://'):
            parts = re.split(r';', piece)
            base = parts.pop(0)
            extras = {}
            for part in parts:
                key, value = re.split(r'=', part)
                extras[key] = value
            if 'protocol' in extras:
                base = re.sub(r'^git', extras['protocol'], base)
            return base, extras
    return None, None


def _getvar(rd, var):
    if var == 'gitrepo':
        uri, _ = _get_git_uri(rd)
        return uri
    elif var == 'gitbranch':
        uri , extras = _get_git_uri(rd)
        if extras and 'branch' in extras:
            return extras['branch']

        if uri:
            return 'master'
        else:
            return None
    else:
        return rd.getVar(var, expand=True)



def main():

    # get and validate commandline args
    parser = argparse.ArgumentParser(description=
      'Query one or more variable values from a yocto package recipe.')
    parser.add_argument('-v', '--var',
                        action='append',
                        dest='var',
                        help='Variable to fetch.')
    parser.add_argument('--gitrepo',
                        action='append_const',
                        dest='var',
                        const='gitrepo',
                        help='Extract the git repo from SRC_URI.')
    parser.add_argument('--gitbranch',
                        action='append_const',
                        dest='var',
                        const='gitbranch',
                        help='Extract the git branch from SRC_URI. (default: "master" if SRC_URI is git, but no branch specified)')
    parser.add_argument('packages', nargs='+',
                        help='Name of package to query')
    parser.add_argument('-s', '--shellsyntax', action='store_true',
                        help='Print the output in shell syntax')

    global args
    args = parser.parse_args()

    if not args.var:
        sys.exit("You must specify at least one var to get")


    # validate ruinning environment
    basepath = os.environ.get('BUILDDIR')
    if not basepath:
        sys.exit("BUILDDIR must be set in the environment")
    os.chdir(basepath)
    workspace_path = os.path.join(basepath, 'workspace')


    # setting up tinfoil is very chatty to console
    old_stdout = sys.stdout
    old_stderr = sys.stderr
    sys.stdout = open(os.devnull, 'w')
    sys.stderr = sys.stdout

    import devtool
    tinfoil = devtool.setup_tinfoil()

    # restore console streams
    sys.stdout = old_stdout
    sys.stderr = old_stderr

    # parse the recipe file and print the requested variables
    for package in args.packages:
        rd = _parse_recipe(workspace_path, tinfoil, package)
        if len(args.var) == 1:
            val = _getvar(rd, args.var[0])
            if not val is None:
                print("{}".format(val))
        else:
            for var in args.var:
                val = _getvar(rd, var)
                if args.shellsyntax :
                    print("export {}={}".format(var, '' if val is None else val))
                else:
                    print("{}={}".format(var, '' if val is None else val))


if __name__ == "__main__":
    main()
