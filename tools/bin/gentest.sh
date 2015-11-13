# Copyright (C) 2015 Hewlett Packard Enterprise Development LP
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

#!/usr/bin/env bash

# Check the file was provided as a parameter
if [ $# -eq 0 ]
then
    echo "Error: No file was specified. Please specify the path name for the test to create."
    exit -1
fi

ROOTDIR=$(git rev-parse --show-toplevel)

# Check template class to use
case "$2" in
    ""|"uttest")
        TPL="uttest.cpp.in"
        ;;
    *)
        echo "Error: Unknown template class $2"
        exit -1
esac

# Check if file already exists
if [ -f "$1" ]
then
    echo "Error: file already exists"
    exit -1
fi

# Gather variables
basedir=$(git rev-parse --show-toplevel)
filename=$(basename "$1")
extension="${filename##*.}"
testname="${filename%.*}"
name="$(git config --global user.name)"
year=$(date +%Y)

# Check extension
if [ $extension != cpp ]
then
    echo "Error: the extension is not cpp"
    exit -1
fi

sed -e "s?@TESTNAME@?$testname?g;s?@AUTHOR@?$name?g;s?@YEAR@?$year?g" "$basedir/tools/templates/${TPL}" > $1
