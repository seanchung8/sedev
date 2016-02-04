#!/bin/sh
scp -i ~/bell/bell banana/*.war root@198.235.66.40:/opt/solr/solr/hdp/webapps
scp -i ~/bell/bell banana/*.xml root@198.235.66.40:/opt/solr/solr/hdp/contexts