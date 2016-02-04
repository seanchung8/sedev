#!/usr/bin/python

import random
import json
import string
import decimal
from optparse import OptionParser
import locale

''' Global vars '''
locale.setlocale(locale.LC_ALL, 'en_US')
ID_LEN = 24
PRICE_CEILING = 1000000
MANUFACTURERS = [ "SPRINGBEE", "MENBRAIN", "PEARLESSA", "KINETICA", "PUSHCART", "HORTONWORKS" ]
DOC_MAX_COUNT = 12
DYN_ATTR_MAX_COUNT = 25
DYN_KEY_LEN = 6
DYN_VAL_LEN = 25
OUTFILE_SOLR = "/tmp/solr_json_data.json"
OUTFILE_HBASE = "/tmp/hbase_json_data.json"
OUTFILE_HBASE_DOCS = "/tmp/hbase_docs_json_data.json"
CHECKPOINT_NUM = 10000
CF_NAME = "attrs"
DOC_BASE = "/data/documents"
DOC_CF_NAME = "docpaths"
DOC_COL = "path"

''' Parse the command line '''
parser = OptionParser()
parser.add_option("-c", "--count", help="Number of documents to generate")
(opts, args) = parser.parse_args()
if not opts.count:
    parser.print_help()
    exit(-1)

''' Main '''
data = []
dyn_data = []
doc_data = []

if int(opts.count) < CHECKPOINT_NUM:
    CHECKPOINT_NUM = int(opts.count)

current_count = 0
while int(current_count) < int(opts.count):
    for cnt in xrange(0, int(CHECKPOINT_NUM)):
        data.append({})
        dyn_data.append({})

        ''' Generate ID '''
        data[cnt]['id'] = ''.join(random.choice(string.ascii_lowercase) for i in range(int(ID_LEN)))
        dyn_data[cnt][data[cnt]['id']] = {}

        ''' Generate isavail bool '''
        data[cnt]['isavail'] = random.choice([True, False])

        ''' Generate color '''
        data[cnt]['color'] = random.choice(['blue', 'black', 'red', 'green', 'purple'])

        ''' Generate size '''
        data[cnt]['size'] = random.choice(['small', 'medium', 'large', 'tiny', 'jumbo'])

        ''' Generate random price with prec of 2 '''
        data[cnt]['price'] = "%.2f" % (float(random.randrange(int(PRICE_CEILING)))/100.00)

        ''' Generate the manufacturer '''
        data[cnt]['manufacturer'] = random.choice(MANUFACTURERS)

        ''' Generate the documentids '''
        docids = []
        for docnum in xrange(0,random.randint(1,int(DOC_MAX_COUNT))):
            docids.append(random.randint(1000000000,9999999999))
            ''' duplicate some documents for the many to many relationship '''
            if random.randint(1,1000) <= 4:
                docids.append(random.randint(5000000000, 5000001000))
        data[cnt]['documentids'] = docids

        ''' Generate a random number of attributes '''
        for attrnum in xrange(0,random.randint(1,int(DYN_ATTR_MAX_COUNT))):
            key = ''.join(random.choice(string.ascii_lowercase) for i in range(int(DYN_KEY_LEN)))
            value = ''.join(random.choice(string.ascii_lowercase) for i in range(int(DYN_VAL_LEN)))
            dyn_data[cnt][data[cnt]['id']][CF_NAME + ":" + key] = value

        ''' Generate document table in hbase '''
        for doc in docids:
            doc_data.append({ doc : { DOC_CF_NAME + ":" + DOC_COL : DOC_BASE + "/" + str(doc) }})

        current_count += 1

    print "SOLR: Generated %s total docs, writing batch of %s docs to %s" % (locale.format("%d", current_count, grouping=True), locale.format("%d", CHECKPOINT_NUM, grouping=True), OUTFILE_SOLR + ".%s" % current_count)
    ''' Output to file '''
    f = open(OUTFILE_SOLR + ".%s" % current_count, 'a')
    f.write(json.dumps(data))
    data = []

    print "HBASE: Generated %s total docs, writing batch of %s docs to %s" % (locale.format("%d", current_count, grouping=True), locale.format("%d", CHECKPOINT_NUM, grouping=True), OUTFILE_HBASE + ".%s" % current_count)
    ''' Output to file '''
    f = open(OUTFILE_HBASE + ".%s" % current_count, 'a')
    f.write(json.dumps(dyn_data))
    dyn_data = []

    print "HBASE DOCS: Writing docs generated in last %s parts to %s" % (current_count, OUTFILE_HBASE + ".%s" % current_count)
    ''' Output to file '''
    f = open(OUTFILE_HBASE_DOCS + ".%s" % current_count, 'a')
    f.write(json.dumps(doc_data))
    doc_data = []
