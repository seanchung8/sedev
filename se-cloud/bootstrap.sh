#!/bin/sh

# Make sure command line parameters are correct
if [ $# -lt 1 ]; then
  echo "Usage: ./bootstrap.sh vm-name.cloud.hortonworks.com"
  exit 1 
elif [ $# -gt 1 ]; then
  echo "Usage: ./bootstrap.sh vm-name.cloud.hortonworks.com"
  exit 1 
fi

# Test for secloud.pem file
if [ ! -e "secloud.pem" ]
then
  echo "You need the SE Cloud pem file in this directory before boostrapping.  Call the file secloud.pem"
  exit 1
fi

# Disable SE Linux
scp -o StrictHostKeyChecking=no -i secloud.pem selinux_config root@$1:/etc/selinux/config

# Disable Transparent Huge Pages (thanks Paul!) 
scp -o StrictHostKeyChecking=no -i secloud.pem rc.local.append root@$1:

# Execute setup commands
ssh -o StrictHostKeyChecking=no -i secloud.pem root@$1 <<'ENDSSH'
cat /root/rc.local.append >> /etc/rc.local
echo 1 > /proc/sys/net/ipv6/conf/all/disable_ipv6
echo 1 > /proc/sys/net/ipv6/conf/default/disable_ipv6
echo never > /sys/kernel/mm/transparent_hugepage/enabled
echo never > /sys/kernel/mm/transparent_hugepage/defrag
yum -y install wget
cd /etc/yum.repos.d
wget http://public-repo-1.hortonworks.com/ambari/centos6/2.x/updates/2.1.0/ambari.repo
yum -y update
yum -y install ntp
ntpdate pool.ntp.org
chkconfig iptables off
/etc/init.d/iptables stop
chkconfig ntpd on
/etc/init.d/ntpd stop
/etc/init.d/ntpd start
echo "net.ipv6.conf.all.disable_ipv6 = 1" >> /etc/sysctl.conf
echo "net.ipv6.conf.default.disable_ipv6 = 1" >> /etc/sysctl.conf
ENDSSH
