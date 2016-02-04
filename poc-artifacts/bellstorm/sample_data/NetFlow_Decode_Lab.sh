#!/bin/sh
# This script will listen for Netflow Packets on port 9996
# and every 5 seconds run a script to decode them
# the origanal packets will be stored in /data/rawnetflow/lab
# The output will go to stdout

nfcapd -p 9996 -l /data/rawnetflow/lab/ -T all -t 5 -x "nfdump -r %d%f -o csv -q -N" 2> /dev/null

