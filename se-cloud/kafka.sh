#!/bin/sh

# Make sure command line parameters are correct
if [ $# -lt 1 ]; then
  echo "Usage: ./kafka.sh vm-name.cloud.hortonworks.com"
  exit 1 
elif [ $# -gt 1 ]; then
  echo "Usage: ./kafka.sh vm-name.cloud.hortonworks.com"
  exit 1 
fi

#Execute setup commands
ssh -t -o StrictHostKeyChecking=no -i secloud.pem root@$1 <<'ENDSSH'
adduser kafka
mkdir /opt/kafka
chown kafka /opt/kafka
cd /opt/kafka
wget -q http://apache.mirror.nexicom.net/kafka/0.8.1.1/kafka_2.9.2-0.8.1.1.tgz
su kafka
tar -xvzf kafka_2.9.2-0.8.1.1.tgz
ln -s kafka_2.9.2-0.8.1.1 kafka
ENDSSH

echo "Kafka setup complete\!"
echo "See http://kafka.apache.org/documentation.html#introduction for more information about creating and working with topic data."