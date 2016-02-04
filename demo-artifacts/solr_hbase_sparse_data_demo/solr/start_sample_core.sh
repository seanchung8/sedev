#!/bin/bash

SCRIPT_DIR=$(cd `dirname $0` && pwd)

SOLR_DL="http://mirror.reverse.net/pub/apache/lucene/solr/4.7.2/solr-4.7.2.tgz"
SOLR_HOME="/opt/solr"
SOLR_JVM_OPTS="-Xmx2g -Xms2g -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:NewRatio=3 -XX:MaxTenuringThreshold=8 -XX:+CMSParallelRemarkEnabled -XX:+ParallelRefProcEnabled -XX:+UseLargePages -XX:+AggressiveOpts"

# Create the solr user
adduser solr 

# Get the SOLR_DL
cd /tmp && wget -N "$SOLR_DL" 

# symlink to SOLR_HOME
solr_parent_dir=`dirname $SOLR_HOME`
solr_dl_filename=`basename $SOLR_DL`
solr_source_dir=`echo $solr_dl_filename | sed -r 's|(\.[^\.]+){1}$||g'`

if [ ! -d $solr_parent_dir ]; then
   mkdir -p $solr_parent_dir
fi
cd $solr_parent_dir && tar -xzvf /tmp/$solr_dl_filename
if [ ! -h $SOLR_HOME ]; then
   ln -s $solr_parent_dir/$solr_source_dir $SOLR_HOME
fi
ls -d $SOLR_HOME

# Copy the sample collection
cp -Rp $SCRIPT_DIR/sample $SOLR_HOME/

# Start solr
cd $SOLR_HOME/sample && java $SOLR_JVM_OPTS -jar start.jar
