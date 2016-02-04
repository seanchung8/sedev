#!/usr/bin/python

import glob
import json
import sys
from starbase import Connection

''' Variables '''
STARGATE_PORT = 12345
TABLE_NAME = "PARTS"
CF_NAME = "ATTRS"
INPUT_BASE = "/tmp/hbase_json_data.json.*"

''' Establish a connection to stargate '''
conn = Connection()
c = Connection(port=STARGATE_PORT)

''' Create the table with single CF '''
table = c.table(TABLE_NAME)
if not table.exists():
    table.create(CF_NAME)

''' Loop through files and load into HBase '''
files = glob.glob(INPUT_BASE)
for fname in files:
    print "Processing input file %s" % fname
    try:
        with open(fname) as f:
            json_data = json.loads(f.read())
            batch = table.batch()
            if batch:
                for entry in json_data:
                    key = entry.keys()[0]
                    batch.insert(key, entry[key])
                batch.commit(finalize=True)
    except IOError:
        raise
