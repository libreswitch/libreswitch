#!/usr/bin/env python
# -*- coding:utf-8 -*-
#
# Copyright (C) 2015 Hewlett Packard Enterprise Development LP
# All Rights Reserved.
#
# The contents of this software are proprietary and confidential
# to the Hewlett Packard Enterprise Development LP. No part of
# this program  may be photocopied, reproduced, or translated
# into another programming language without prior written consent
# of the Hewlett Packard Enterprise Development LP.

"""
Find and parse changelog.conf files and generate a changelog manifest.
"""

from __future__ import unicode_literals
from __future__ import print_function
from __future__ import absolute_import

import logging
import shutil
import sys
import os
import re
from os import system
from re import search
from os import walk
from os.path import join, abspath, dirname


FORMAT = '** %(levelname)s :: %(message)s'
log = logging.getLogger(__name__)


def find_changelogs(root):
    """
    Find in root path parameter all files named 'changelog.conf'.

    :param str root: Root path to find files.
    :rtype: list
    :return: List of paths to changelog.conf files found in root.
    """
    changelogs = []
    for root, dirs, files in walk(root):
        if 'changelog.conf' in files:
            changelogs.append(
                abspath(join(root, 'changelog.conf'))
            )
    return changelogs


def parse_changelog(changelog):
    """
    Parse a changelog file with the following format:

    ::

        recipe1
        recipe2
        recipe3

    :param str changelog: Path to the changelog.conf file.
    :rtype: list
    :return: List of recipes found in changelog file.
    """
    with open(changelog, 'r') as fd:
        lines = []
        for line in fd:
            line = line.strip()
            if line:
                lines.append(line)
        return lines


def find_recipe(root, name):
    """
    Find recipe with given name in given root directory.

    :param str root: Root path to find recipe.
    :param str name: Name of the BitBake recipe.
    :rtype: str o None
    :return: Path to recipe file, or None if not found.
    """
    recipe = '{}.bb'.format(name)

    for root, dirs, files in walk(root):
        if recipe in files:
            return abspath(join(root, recipe))
    return None


def parse_recipe(recipe):
    """
    Parse a BitBake recipe to identify source repository.

    :param str recipe: Path to recipe file.
    :rtype: str
    :return: Source code repository from which the recipe sources the code.
    """
    with open(recipe, 'r') as fd:
        for line in fd:
            line = line.strip()

            if not line or line.startswith('#'):
                continue

            match = search('(git(s)?://)', line)
            if not match:
                continue

            protocol = line[line.find('protocol=') + 9:len(line)]
            if protocol.find('\\'):
                protocol = protocol[0:protocol.find('\\')].strip()
            final = len(line)
            if line.find('\\'):
                final = line.find('\\')
            line = line[(line.find('git')):final].strip()
            line = line.replace('git', protocol, 1)
            line = line[0:line.find('protocol=') - 1]

            return line

    return None

def generate_manifest_data(repositories):
    """
    We need to execute BitBake -s in order to know the latest version for each proyect
    :param str repositories: Path of the recipe.
    :rtype: str
    """
    #git checkout a la primera version
    system ('make bake RECIPE=\'-s\' | grep \'gitAUTOINC*\' > build/changelog_tmp.txt')
    listprojects = repositories.keys()

    #it is going to store the name of the project and the git reference number
    m = open('build/changelog.manifest','w')

    with open('build/changelog_tmp.txt') as f:
        for line in f:
            #Get the name of the project
            str1 = line.strip().find(" ")
            project_name = line[0:str1]
            if project_name in listprojects:
                #gitN = line [(line.find("gitAUTOINC+") + 11):len(line)].strip()
                #str3 = gitN.find("-")
                #git Reference number
                #gitNumber = gitN[0:str3]
                #im.write(project_name + ';' + gitNumber+ ';' + repositories[project_name] +'\n')
                matched = re.search('.*\+([0-9a-z]{10})-.*', line)
                if matched:
                    gitNumber = matched.group(1)
                    m.write(project_name + ';' + gitNumber + ';' + 'http://git.openswitch.net/openswitch/' + project_name +'\n')
        m.close()

def main():
    """
    Main function.
    """
    # Configure logging
    logging.basicConfig(format=FORMAT, level=logging.DEBUG)

    # Get root
    #root = abspath(dirname(__file__))
    root = abspath("yocto")
    #log.debug('Root is: {}'.format(root))

    # Find all changelog configurations
    changelogs = find_changelogs(root)
    #log.debug('Changelogs found: {}'.format(changelogs))

    repositories = {}

    # Parse all changelogs
    for chgl in changelogs:
        recipes = parse_changelog(chgl)
        #log.debug('Recipes found in {}: {}'.format(chgl, recipes))

        # Find all recipes
        for name in recipes:
            recipe = find_recipe(root, name)

            if recipe is None:
                log.error(
                    'Cannot find recipe {} in {}'.format(name, root)
                )
                continue
            #log.debug('Recipe {} found in: {}'.format(name, recipe))

            # Parse recipe
            repository = parse_recipe(recipe)
            #log.debug('Recipe {} repository is: {}'.format(name))
            repositories[name] = repository

    generate_manifest_data(repositories)

if __name__ == '__main__':
    main()
