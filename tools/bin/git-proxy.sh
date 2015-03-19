#!/bin/sh

exec corkscrew `echo $http_proxy | sed -e 's?.*//\(.*\):\([0-9]*\).*?\1 \2?'` $*
