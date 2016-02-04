#!/bin/bash

base="/tmp/solr_json_data.json"

for i in $(ls ${base}.*); do 
    echo $i
    curl 'http://localhost:8983/solr/collection/update/json' --data-binary @$i -H 'Content-type:application/json'
done
