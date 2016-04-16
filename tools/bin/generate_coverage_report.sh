#!/usr/bin/env bash
#
# Copyright (C) 2015 Hewlett Packard Enterprise Development LP
# All Rights Reserved.
#
# The contents of this software are proprietary and confidential
# to the Hewlett Packard Enterprise Development LP. No part of
# this program  may be photocopied, reproduced, or translated
# into another programming language without prior written consent
# of the Hewlett Packard Enterprise Development LP.
# Code Coverage Generation Tool
#
#  This tool uses lcov to produce HTML coverage reports for components
#  that can be compiled with gcov coverage files.
#
# Usage:
#  $ ./generate_coverage_report.sh ops-component
#  - ops-component: The name of the OPS component/module under test.
#
# What the tool does:
#  1. Enables gcov coverage flags on modules added to the devenv
#  2. Compiles the moduler under test to produce the gcno files
#  3. Creates a baseline with the gcno files prior to running tests
#  4. Run tests using the testenv_run target
#  5. Reads a code coverage configuration file and applies the settings
#  6. Analyzes the coverage results and generates an HTML report

COV_MODULE=$1
COV_CONF_FILE="src/$COV_MODULE/ft_ct_coverage"        # Configuration file. If it exists then CT/FT coverage will be performed.
                                                      # example: http://git.openswitch.net/cgit/openswitch/ops-lacpd/tree/ft_ct_coverage
COV_MINIMUM=-1                                        # Minimum default of coverage. -1 as we want all runs to pass
COV_REPORT_DIR="coverage"                             # Location where the coverage report data will be placed
COV_DATA_DIR="src/"                                   # Repo directory where coverage data will be stored (gcda && gcno)

# If the COV_CONF_FILE does not exist, do not continue, i.e no coverage enabled at the repo.
if [ -f $COV_CONF_FILE ]
then
  # Makes the build system compile repos added to the devenv with gcov flags
  touch build/devenv-coverage-enabled
  # Compile the moduler under test, which causes the gcno files to be generated.
  # The gcno files are needed prior to running the tests so that a baseline can be created
  make $COV_MODULE-build
  mkdir -p $COV_REPORT_DIR
  # Clear any previous coverage data
  rm -rf $COV_REPORT_DIR/*
  # Clear all coverage counters before running the tests
  lcov --zerocounters --directory $COV_DATA_DIR
  # Create coverage data baseline, check that gcno files exist before proceeding
  COV_BASE_DATA=`lcov --initial --capture --directory $COV_DATA_DIR --output-file $COV_REPORT_DIR/app_base.info 2>&1`
  if grep -q "ERROR: no \.gcno files found" <<< $COV_BASE_DATA; then
    echo "WARNING: No coverage notes file was generated during module compilation. Exiting now with no coverage report"
    exit 0
  fi
  # Run the Feature Tests
  make testenv_run feature $COV_MODULE
  # Capture the coverage raw data and check that coverage data was generated
  COV_RAW_DATA=`lcov --capture --directory $COV_DATA_DIR --output-file $COV_REPORT_DIR/app_test.info 2>&1`
  if grep -q "ERROR: no \.gcda files found" <<< $COV_RAW_DATA; then
    echo "WARNING: No coverage data was generated during test run. Exiting now with no coverage report"
    exit 0
  fi
  # The file where the coverage data will be placed
  LCOV_COV_FILE="app_total.info"
  # Read the configured minimum coverage percentage and exclude patterns from the repo conf file
  # Configure exclude patterns using wildcars and separated by spaces, e.g to remove gtest and openvswitch files use:
  # Exclude_pattern=*gtest* *openvswitch*
  while read -r line
  do
    tmp=`echo "$line" | sed -rn 's/^Minimum_coverage=([0-9]+)$/\1/p'`
    if [ ! -z "$tmp" ]
    then
      COV_MINIMUM=$tmp
    fi
    tmp=`echo "$line" | sed -rn 's/^Exclude_pattern=(.*)$/\1/p'`
    if [ ! -z "$tmp" ]
    then
      COV_EXCLUDE_PATTERN=$tmp
    fi
  done < "$COV_CONF_FILE"
  # Check if exclusion pattern is configured and apply it.
  if [ ! -z "$COV_EXCLUDE_PATTERN" ]
  then
    echo "The configured FT/CT code coverage exclude pattern is: $COV_EXCLUDE_PATTERN"
    echo "Going to exclude files from report:"
    lcov --remove $COV_REPORT_DIR/app_base.info $COV_EXCLUDE_PATTERN --output-file $COV_REPORT_DIR/data_base.info
    lcov --remove $COV_REPORT_DIR/app_test.info $COV_EXCLUDE_PATTERN --output-file $COV_REPORT_DIR/data_test.info
    COV_RESULT=`lcov --add-tracefile $COV_REPORT_DIR/data_base.info --add-tracefile $COV_REPORT_DIR/data_test.info --output-file $COV_REPORT_DIR/$LCOV_COV_FILE`
  else
    COV_RESULT=`lcov --add-tracefile $COV_REPORT_DIR/app_base.info --add-tracefile $COV_REPORT_DIR/app_test.info --output-file $COV_REPORT_DIR/$LCOV_COV_FILE`
  fi
  echo "*** Going to merge baseline data with raw coverage data. ***"
  echo "Coverage result after merging with baseline data:"
  echo $COV_RESULT
  # Get actual coverage from command output
  COV_REPORTED=`echo "$COV_RESULT" | sed -rn 's/lines\.\.\.\.\.\.:[[:space:]]([[:digit:]]+).*/\1/p'`
  if [ -z "$COV_REPORTED" ]
  then
    COV_REPORTED=0
  fi
  # Generate the HTML report
  mkdir -p $COV_REPORT_DIR/html
  cd $COV_REPORT_DIR/html
  # TODO: Implement Branch Analysis
  echo "Creating HTML report"
  genhtml --no-branch-coverage ../$LCOV_COV_FILE > /dev/null 2>&1
  cd ../..
  echo ""
  echo "**** Configuration and result info: ****"
  echo ""
  echo "The configured coverage threshold is: $COV_MINIMUM"
  echo "The configured FT/CT code coverage exclude pattern is: $COV_EXCLUDE_PATTERN"
  echo "The actual reported coverage is: $COV_REPORTED"
  echo "The location of the FT/CT code coverage report is: $COV_REPORT_DIR/html"
  if [ "$COV_MINIMUM" -gt "$COV_REPORTED" ]
  then
    echo "The code does not have enough coverage"
  else
    exit 0
  fi
else
  echo "Code coverage not enabled, skipping..."
fi
