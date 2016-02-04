#!/bin/sh
ssh -i ~/bell/bell root@198.235.66.40 <<'ENDSSH'
storm kill bell-storm
ENDSSH
scp -i ~/bell/bell target/bell-storm-0.1.jar root@198.235.66.40:
scp -i ~/bell/bell src/main/resources/config.properties root@198.235.66.40:
ssh -i ~/bell/bell root@198.235.66.40 <<'ENDSSH'
storm jar bell-storm-0.1.jar com.hortonworks.streaming.impl.topologies.BellStormKafkaTopology config.properties
ENDSSH

