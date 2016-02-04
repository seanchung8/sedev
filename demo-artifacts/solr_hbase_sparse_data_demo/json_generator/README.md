JSON generator for Solr
============
Generate JSON docs with randomized data to test mulitvalue and dynamic fields in Solr.

Prerequisites
-------------
1. Python 2.6


Setup
-----
1. Clone the repo
2. Modify the global variables as desired
3. Run the script with the number of documents to generate


Running
-------
* Run the script with the desired number of documents
```
python generate_json.py -c 1000000
```

* Define the appropriate Solr schema in schema.xml
```
<fields>
   <!-- internal fields, keep --> 
   <field name="_version_" type="long" indexed="true" stored="true"/>
   <field name="_root_" type="string" indexed="true" stored="false"/>
   <field name="text" type="text_general" indexed="true" stored="false" multiValued="true"/>

   <!-- needed for storing index segments on HDFS -->
   <field name="data_source" stored="false" type="text_general" indexed="true"/>

   <!-- user defined fields, modify as needed -->
   <field name="id" type="string" indexed="true" stored="true" required="true" multiValued="false" />
   <field name="isavail" type="boolean" indexed="true" stored="true" />
   <field name="manufacturer" type="text_general" indexed="true" stored="true"/>
   <field name="price"  type="float" indexed="true" stored="true"/>
   <field name="documentids" type="text_general" indexed="true" stored="true" multiValued="true"/>

   <!-- dynamic field, handles strings, add additional as needed -->
   <dynamicField name="*_s"  type="string"  indexed="true"  stored="true" />
</fields>
```

* Load the data into Solr
```
curl 'http://localhost:8983/solr/<collection name>/update/json?commit=true' --data-binary @<path_to_output>.json -H 'Content-type:application/json'
```
