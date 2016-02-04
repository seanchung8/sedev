#!/bin/sh

# Make sure command line parameters are correct
if [ $# -lt 1 ]; then
  echo "Usage: ./solr.sh vm-name.cloud.hortonworks.com"
  exit 1 
elif [ $# -gt 1 ]; then
  echo "Usage: ./solr.sh vm-name.cloud.hortonworks.com"
  exit 1 
fi

#Execute setup commands
ssh -t -o StrictHostKeyChecking=no -i secloud.pem root@$1 <<'ENDSSH'
adduser solr
mkdir /opt/solr
chown solr /opt/solr
cd /opt/solr
wget -q http://apache.mirror.gtcomm.net/lucene/solr/4.7.2/solr-4.7.2.tgz
wget -q http://lucidworks.com/resources/LW-product-binaries/lucidworks-hadoop-lws-job-1.3.0.jar
su solr
tar -xvzf solr-4.7.2.tgz
ln -s solr-4.7.2 solr
cd solr
cp -r example hdp 
rm -rf hdp/examle* hdp/multicore
mv hdp/solr/collection1 hdp/solr/hdp1
rm hdp/solr/hdp1/core.properties
exit
su - hdfs
hadoop fs -mkdir -p /user/solr
hadoop fs -chown solr /user/solr
ENDSSH

# Copy the sample config
scp -o StrictHostKeyChecking=no -i secloud.pem solrconfig.xml root@$1:/opt/solr

# Modify and install the solr config
ssh -t -o StrictHostKeyChecking=no -i secloud.pem root@$1 <<'ENDSSH'
chown solr /opt/solr
cd /opt/solr
su solr
cat solrconfig.xml | sed -e "s/NAMENODEHOSTNAME/`hostname`/g" > solr/hdp/solr/hdp1/conf/solrconfig.xml
ENDSSH

echo "Solr install complete\!"
echo "Now you need to edit /opt/solr/hdp/solr/hdp1/conf/schema.xml to add a schema"
echo "See http://hortonworks.com/hadoop-tutorial/searching-data-solr/ for more information"
echo "Start your solr instance with: "
echo "  cd /opt/solr/solr/hdp"
echo "  su solr"
echo "  java -jar start.jar"
echo "Now point your browser to: http://node_you_installed:8983/solr"
echo "Add your core under core admin"
