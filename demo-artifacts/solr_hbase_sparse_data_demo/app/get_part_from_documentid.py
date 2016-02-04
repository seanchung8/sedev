#!/usr/bin/python

import urllib2
import json
from starbase import Connection

''' Solr Variables '''
DOCID = "2794413233"
SOLR_BASE = "http://localhost:8983/solr/collection/select"
SOLR_POSTFIX = "&wt=json&indent=true"

''' HBase Variables '''
STARGATE_PORT = 12345

''' Establish a connection to stargate '''
conn = Connection()
c = Connection(port=STARGATE_PORT)

''' Query solr '''
full_url = SOLR_BASE + "?q=documentids:" + DOCID + "&fl=" + SOLR_POSTFIX
response = urllib2.urlopen(full_url)
resp_data = json.loads(response.read())
print "\nFound %s part for document %s" %  (resp_data['response']['numFound'], DOCID)

''' Get Document path '''
TABLE_NAME = "docs"
CF_NAME = "docpaths"
COL = "path"
table = c.table(TABLE_NAME)
print "Document Path: %s" % table.fetch(DOCID)[CF_NAME][COL]

TABLE_NAME = "parts"
CF_NAME = "attrs"
table = c.table(TABLE_NAME)
for doc in resp_data['response']['docs']:
    print "\nPartId: %s" % doc['id']
    print "\tPrice: %s" % doc['price']
    print "\tIs Available?: %s" % doc['isavail']
    print "\tManufacturer: %s" % doc['manufacturer']
    print "\tDocuments: %s" % ",".join(doc['documentids'])
    for attr,val in table.fetch(doc['id'])['attrs'].iteritems():
        print "\t%s: %s" % (attr, val)
