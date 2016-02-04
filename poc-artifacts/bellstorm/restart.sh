#!/bin/sh
scp -i ~/bell/bell src/main/resources/config.properties root@198.235.66.40:
ssh -i ~/bell/bell root@198.235.66.40 <<'ENDSSH'
storm kill bell-storm
sleep 5
storm jar bell-storm-0.1.jar com.hortonworks.streaming.impl.topologies.BellStormKafkaTopology config.properties
ENDSSH