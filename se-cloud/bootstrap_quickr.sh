#!/bin/sh

#
# Parse cmdline
#
if [ $# -ne 1 ]; then
  echo "Usage: ./bootstrap.sh vm-name.cloud.hortonworks.com"
  exit 1 
fi

#
# Sanity checks
#

# Test for secloud.pem file
if [ ! -e "secloud.pem" ]; then
  echo "You need the SE Cloud pem file in this directory before boostrapping.  Call the file secloud.pem"
  exit 1
fi

# Test for rc.local.append file
if [ ! -e "rc.local.append" ]; then
  echo "You need the rc.local.append file in this directory before boostrapping."
  exit 1
fi

# Test for selinux_config file
if [ ! -e "selinux_config" ]; then
  echo "You need the selinux_config file in this directory before boostrapping."
  exit 1
fi


#
# Main
#

echo -e "\n##"
echo -e "## Starting bootstrap on $1"
echo -e "##"

# Disable SE Linux
echo -e "\n#### Disabling SELinux"
scp -o StrictHostKeyChecking=no -i secloud.pem selinux_config root@$1:/etc/selinux/config

# Disable Transparent Huge Pages (thanks Paul!) 
echo -e "\n#### Disabling Transparent Huge Pages"
scp -o StrictHostKeyChecking=no -i secloud.pem rc.local.append root@$1:
ssh -o StrictHostKeyChecking=no -i secloud.pem root@$1 <<'ENDSSH'
cat /root/rc.local.append >> /etc/rc.local
echo never > /sys/kernel/mm/transparent_hugepage/enabled
echo never > /sys/kernel/mm/transparent_hugepage/defrag
ENDSSH

# Disable IPv6
echo -e "\n#### Disabling IPv6"
ssh -o StrictHostKeyChecking=no -i secloud.pem root@$1 <<'ENDSSH'
echo 1 > /proc/sys/net/ipv6/conf/all/disable_ipv6
echo 1 > /proc/sys/net/ipv6/conf/default/disable_ipv6
echo "# Added by HDP Bootstrap Script - disable IPv6" >> /etc/sysctl.conf
echo "net.ipv6.conf.all.disable_ipv6 = 1" >> /etc/sysctl.conf
echo "net.ipv6.conf.default.disable_ipv6 = 1" >> /etc/sysctl.conf
ENDSSH

echo -e "\n#### Configuring YUM Repos"
ssh -o StrictHostKeyChecking=no -i secloud.pem root@$1 <<'ENDSSH'
yum -y install wget
cd /etc/yum.repos.d
wget http://public-repo-1.hortonworks.com/ambari/centos6/2.x/updates/2.0.0/ambari.repo
ENDSSH

echo -e "\n#### Configuring NTP"
ssh -o StrictHostKeyChecking=no -i secloud.pem root@$1 <<'ENDSSH'
yum -y install ntp
ntpdate pool.ntp.org
chkconfig ntpd on
/etc/init.d/ntpd stop
/etc/init.d/ntpd start
ENDSSH

echo -e "\n#### Disabling IPTables"
ssh -o StrictHostKeyChecking=no -i secloud.pem root@$1 <<'ENDSSH'
chkconfig iptables off
/etc/init.d/iptables stop
chkconfig ip6tables off
/etc/init.d/ip6tables stop
ENDSSH

echo -e "\n##"
echo -e "## Finished bootstrap on $1"
echo -e "##"

exit 0
