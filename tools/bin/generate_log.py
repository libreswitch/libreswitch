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
Parse the manifest.conf and generate the log in order to know what changed in differents versions of the code
"""

from __future__ import unicode_literals
from __future__ import print_function
from __future__ import absolute_import

import tempfile
import logging
import git
import shutil
import sys
import os
from os import system
from re import search
from os import walk
from os.path import join, abspath, dirname
from git import Repo

FORMAT = '** %(levelname)s :: %(message)s'
log = logging.getLogger(__name__)


def clone_project(project, url, folder_path):
    """
    Get the code from the repository

    :param str url: Path of the code.
    :rtype: str
    :return: 
    We need to install pip install GitPython (it is a python library used to interact with Git repositories)
    https://github.com/gitpython-developers/GitPython
    """

    path = abspath(folder_path + 'tmp/' + project)
    Repo.clone_from(url,path)

def set_environment_flag(filename):
    """
    This method is in charge of change the value of the enviroment variable for git changelog
    :param str project: Name of the variable.
    :rtype: str
    :return: 
    """
    pathLogChange = abspath('../gitchangelog/'+filename)
    os.environ["GITCHANGELOG_CONFIG_FILENAME"] = pathLogChange

def write_changelog_file(path, pathChangeLog,file_path):
    """
    Get the log for each project 
    :param str path: Path where the script has to run the program
               pathChangeLog: the path where is the gitchangelog
               file_path: the path to write the result of the operation
    :rtype: str
    :return: 
    """
    command = 'cd '+path
    command += '&& '+pathChangeLog +' >> ' + file_path 
    system (command)

def get_log(project, log_number,folder_path):
    """
    Get the log for each project 
    :param str project: Name of the project.
    :rtype: str
    :return: 
    """
    path = abspath(folder_path + 'tmp/' + project)
    command = 'cd '+path
    #git checkout a la primera version
    command += ' && git checkout '+log_number
    #system('git tag startPoint')
    command +=  ' && git tag current_version'
    system (command)
    
    pathChangeLog = abspath('../gitchangelog/gitchangelog.py')

    #Getting the changelog for new features    
    set_environment_flag('gitchangelognew.rc')
    write_changelog_file(path,pathChangeLog, folder_path +'/new.changelog')

    #Getting the changelog for Changes 
    set_environment_flag('gitchangelogchanges.rc')
    write_changelog_file(path,pathChangeLog, folder_path +'/changes.changelog')

    #Getting the changelog for Fix 
    set_environment_flag('gitchangelogfix.rc')
    write_changelog_file(path,pathChangeLog, folder_path +'/fix.changelog')

    #Getting the changelog for Fix 
    set_environment_flag('gitchangelogother.rc')
    write_changelog_file(path,pathChangeLog, folder_path +'/others.changelog')

def get_range_log(project, from_log, to_log, folder_path):
    """
    Get the log for each project 
    :param str project: Name of the project.
    :rtype: str
    :return: 
    """
    path = abspath(folder_path + 'tmp/' + project)

    command = 'cd '+path
    #git checkout a la primera version
    command += ' && git checkout '+from_log+'~1'
    #system('git tag startPoint')
    command +=  ' && git tag previous_version'
    system (command)
    
    command = 'cd '+path
    #git checkout a la primera version
    command += ' && git checkout '+to_log
    # add final tag
    command +=  ' && git tag current_version'
    system (command)

    pathChangeLog = abspath('../gitchangelog/gitchangelog.py')

    #Getting the changelog for new features    
    set_environment_flag('gitchangelognew.rc')
    write_changelog_file(path,pathChangeLog, folder_path +'/new.changelog')

    #Getting the changelog for Changes 
    set_environment_flag('gitchangelogchanges.rc')
    write_changelog_file(path,pathChangeLog, folder_path +'/changes.changelog')

    #Getting the changelog for Fix 
    set_environment_flag('gitchangelogfix.rc')
    write_changelog_file(path,pathChangeLog, folder_path +'/fix.changelog')

    #Getting the changelog for Fix 
    set_environment_flag('gitchangelogother.rc')
    write_changelog_file(path,pathChangeLog, folder_path +'/others.changelog')
    
def merge_files (folder_path):
    """
    Get the log for each project 
    :param str project: Name of the project.
    :rtype: str
    :return: 
    """
    #we are doing this just to have the rigth order 
    filenames = []
    filenames.append(folder_path+ '/new.changelog')
    filenames.append(folder_path+ '/changes.changelog')
    filenames.append(folder_path+ '/fix.changelog')
    filenames.append(folder_path+ '/others.changelog')

    with open('ChangeLogResult', 'w') as outfile:
        for fname in filenames:
            with open(fname) as infile:
                for line in infile:
                    outfile.write(line)

def create_files(file_name,dirTempPath):
    """
    Creating files and headers
    :param str file_name: Name of the file.
               dirTempPath: Temp path 
    :rtype: str
    :return: 
    """
    name = file_name.capitalize()
    if not os.path.exists(dirTempPath +'/'+file_name+'.changelog'):
        system('echo \''+ name +'\n'+ '='*len(name) +'\' > '+ dirTempPath +'/'+ file_name +'.changelog')

def create_file_html():
    """
    Create file in a nice format.
    """
    os.system('export LC_ALL=en_US.UTF-8')
    os.system('export LANG=en_US.UTF-8')
    os.system('rst2html.py ChangeLogResult > changelogresults.html')

def main():
    """
    Main function.
    """

    pathLogChange = abspath("../gitchangelog/gitchangelog.rc")
    os.environ["GITCHANGELOG_CONFIG_FILENAME"] = pathLogChange
    log.debug('Path Log Change: {}  '+ pathLogChange)

    # Configure logging
    logging.basicConfig(format=FORMAT, level=logging.DEBUG)

    dirTempPath = tempfile.mkdtemp()
    
    log.debug('Path: {}' + dirTempPath)

    if len(sys.argv) < 2:
        log.debug('Missing file to compare')
        sys.exit(1)

    #Getting the name of the files
    first_file = str(sys.argv[1]).strip()
    second_file = None
    if len(sys.argv) >= 3:
        second_file = str(sys.argv[2]).strip()
    
    # Creating files and headers
    create_files('new',dirTempPath)
    create_files('changes',dirTempPath)
    create_files('fix',dirTempPath)
    create_files('others',dirTempPath)

    #remove_old_data()
    if second_file is None:
        with open(first_file, 'r') as fd:
            for line in fd:
                line = line.strip()
                data = line.split(';')
                log.debug('Line to process: {}'.format(data))
                clone_project(data[0],data[2],dirTempPath)
                get_log(data[0],data[1],dirTempPath)
        fd.closed
    else:
        if second_file is not None:
            #read the first file in order to get projects
            file1 = open(first_file, 'r')
            file2 = open(second_file, 'r')

            for line in file1:
                line = line.strip()
                data = line.split(';')
                log.debug('Line to process: {}'.format(data))
                project_name = data[0]
                git_log_version1= data[1]
                clone_project(project_name,data[2],dirTempPath)
                #checking if the project is in the second manifest
                for line2 in file2:
                    data2 = line2.strip().split(';')
                    if data2[0] != project_name:
                        continue
                    
                    git_log_version2 = data2[1]
                    log.debug('Path: {}' + dirTempPath)
                    get_range_log(project_name,git_log_version1,git_log_version2, dirTempPath)
            file1.closed
            file2.closed
    merge_files (dirTempPath)
    log.debug('Creating Final File' )
    create_file_html() 


if __name__ == '__main__':
    main()

