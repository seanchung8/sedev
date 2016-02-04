#!/usr/bin/python

import urllib2
import json
import sys
from starbase import Connection

''' Solr Variables '''
MANU = "KINETICA"
SOLR_BASE = "http://localhost:8983/solr/collection/select"
SOLR_POSTFIX = "&rows=10000000&wt=json&indent=true"

''' HBase Variables '''
STARGATE_PORT = 12345

''' Establish a connection to stargate '''
conn = Connection()
c = Connection(port=STARGATE_PORT)

''' Query solr '''
full_url = SOLR_BASE + "?q=manufacturer:" + MANU + "&fl=" + SOLR_POSTFIX
response = urllib2.urlopen(full_url)
resp_data = json.loads(response.read())
print "\nFound %s part(s) for manufacturer %s" %  (resp_data['response']['numFound'], MANU)

TABLE_NAME = "parts"
CF_NAME = "attrs"
table = c.table(TABLE_NAME)
for doc in resp_data['response']['docs']:
    print "\nPartId: %s" % doc['id']
    print "\tPrice: %s" % doc['price']
    print "\tIs Available?: %s" % doc['isavail']
    print "\tManufacturer: %s" % doc['manufacturer']

    print "\tDocuments:"
    doc_table = c.table("docs")
    for docid in doc['documentids']:
        print "\t\t%s: %s" % (docid, doc_table.fetch(docid)['docpaths']['path'])

    for attr,val in table.fetch(doc['id'])['attrs'].iteritems():
        print "\t%s: %s" % (attr, val)
