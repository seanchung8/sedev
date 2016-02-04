#!/usr/bin/python

import random
import json
import string
import decimal
from optparse import OptionParser
import locale

''' Global vars '''

''' Set local for readability '''
locale.setlocale(locale.LC_ALL, 'en_US')

''' Used for generatoring somewhat random data '''
ID_LEN = 24
PRICE_CEILING = 1000000
COLORS = ['blue', 'black', 'red', 'green', 'purple']
SIZES = ['small', 'medium', 'large', 'tiny', 'jumbo']
MANUFACTURERS = [ "ABA", "Abadal", "Abarth", "Abbott", "ABC", "Abingdon", "Abra", "AC", "Acadian", "Acura", "Adams-Farwell", "Adler", "Advance", "Aeon", "Aero", "Aerocarene", "AGA", "AGR", "Aixam", "AJS", "AKS", "Alanqa", "Alba", "Albar", "Albion", "Alco", "Alesia", "Alfa", "Allard", "Alldays", "Alpine", "Alta", "Alvis", "Ambassador", "AMC", "American", "Amilcar", "Amphicar", "AMV", "Anadol", "Anglian", "Anhui", "Ansaldo", "Apal", "Apollo", "Apperson", "Aptera", "Aquila", "Arab", "Arash", "Arden", "Argyll", "Ariel", "Aries", "Armadale", "Armstrong", "Arnolt", "Aro", "Arrol-Johnston", "Artega", "ASA", "Ascari", "Asia", "ASL", "ASM", "Asquith", "Aston", "Asuna", "Atalanta", "Attica", "Auburn", "Audi", "Aurora", "Austin", "Austin-Healey", "Austin-Morris", "Austral", "Austro-Daimler", "Auto", "Autobacs", "Autobianchi", "Autobleu", "Autocar", "Autocars", "Automobilbau", "AutoVAZ", "Autovia", "Autozam", "Autozodiaco", "Auverland", "AV", "Avanti", "Avia", "Avtodesign", "AWE", "AWZ", "AXR", "B", "Bajaj-Tempo", "Baker", "Ballot", "Bandini", "Baoding", "Baojun", "Baolong", "Barkas", "Barley", "Bayliss-Thomas", "BAZ", "Bean", "Beaumont", "Bebi", "Bedelia", "Bedford", "Beijing", "Bellier", "Belsize", "Bentall", "Bentley", "Benz", "Berkeley", "Berliet", "Berrien", "Beuk", "Biddle", "Bignan", "Birkin", "Biscuter", "Bitter", "Bizzarrini", "Blackjack", "Blixt", "BMC", "BMW", "BNC", "Bond", "Borgward", "Brammo", "Brasier", "Brennabor", "Bricklin", "Bright", "Brilliance", "Bristol", "British", "BRM", "Brooke", "Brush", "Brutsch", "BSA", "Buchmann", "Buckle", "Buckler", "Budd", "Bufori", "Bugatti", "Buick", "Bungartz", "Burney", "Byd", "C.A.C.", "Cadillac", "Calcott", "Callaway", "Caparo", "Cardi", "Casalini", "Castagna", "Caterham", "CD", "Ceirano", "Cemsa", "CG", "Chaigneau-Brasier", "Chaika", "Chamonix", "Champion", "ChangAn", "Changfeng", "Changhe", "Chaparral", "Charles", "Charron", "Chatenet", "Checker", "Chenard", "Chery", "Chevrolet", "Chevron", "Chiribiri", "Chrysler", "Cicero", "Cisitalia", "Citroen", "Cizeta-Moroder", "Clan", "Clement-Bayard", "Clenet", "Clipper", "Cluley", "Clyno", "Colani", "Cole", "Columbia", "Comarth", "Commer", "Commuter", "Connaught", "Continental", "Cooper", "Corbin", "Cord", "Corre-La", "Cottin", "Crane", "Crosley", "Crosslander", "Crossley", "Cunningham", "CWS", "Dacia", "Dadi", "Daewoo", "Daf", "Daihatsu", "Daimler", "Dare", "Darl", "Darracq", "Datsun", "Dauer", "Davis", "Davrian", "Dawson", "DAX" ]
ATTRS = [ "Abrasion", "Absorption", "Additives", "Applications", "Behavior", "Biological", "Break", "Brittleness", "Capacity", "Chemical", "Coefficient", "Compression", "Conductivity", "Content", "Crystallization", "Decomposition", "Deflection", "Density", "Dielectric", "Dissipation", "Elastic", "Electric", "Environmental", "Expansion", "Failure", "Flammability", "Flexural", "Flow", "Formulae", "Fracture", "Friction", "Gas", "Gases", "Glass", "Hardness", "Heat", "Hydrolytic", "Impact", "Index", "Intrinsic", "Latent", "Liquids", "Magnetic", "Mechanical", "Melt", "Melting", "Melts", "Modulus", "Molar", "Morphology", "Optical", "Permeability", "Permittivity", "Poisson", "Polymer", "Power", "Properties", "Ratio", "References", "Refraction", "Refractive", "Resistance", "Selected", "Solubility", "Specific", "Stability", "Strength", "Stress", "Structural", "Surface", "Temperature", "Tensile", "Thermal", "Transition", "Transport", "Upper-Use", "Viscoelastic", "Viscosity", "Water", "Yield" ]

''' Max number of docs per part '''
DOC_MAX_COUNT = 12

''' Max number of variable attributes per part'''
DYN_ATTR_MAX_COUNT = 10

''' Output '''
OUTFILE_SOLR = "/tmp/solr_json_data.json"
OUTFILE_HBASE = "/tmp/hbase_json_data.json"
OUTFILE_HBASE_DOCS = "/tmp/hbase_docs_json_data.json"
CHECKPOINT_NUM = 10000

''' HBase data '''
CF_NAME = "ATTRS"
DOC_BASE = "/data/documents"
DOC_CF_NAME = "DOCPATHS"
DOC_COL = "PATH"

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
        data[cnt]['color'] = random.choice(COLORS)

        ''' Generate size '''
        data[cnt]['size'] = random.choice(SIZES)

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
            key = random.choice(ATTRS).upper()
            value = random.choice(ATTRS)
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
