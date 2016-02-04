#!/bin/sh

ssh -i ~/bell/bell root@198.235.66.40 rm /usr/lib/storm/lib/http*
ssh -i ~/bell/bell root@198.235.66.41 rm /usr/lib/storm/lib/http*
ssh -i ~/bell/bell root@198.235.66.42 rm /usr/lib/storm/lib/http*
ssh -i ~/bell/bell root@198.235.66.43 rm /usr/lib/storm/lib/http*
ssh -i ~/bell/bell root@198.235.66.44 rm /usr/lib/storm/lib/http*
ssh -i ~/bell/bell root@198.235.66.45 rm /usr/lib/storm/lib/http*
ssh -i ~/bell/bell root@198.235.66.46 rm /usr/lib/storm/lib/http*
ssh -i ~/bell/bell root@198.235.66.47 rm /usr/lib/storm/lib/http*
ssh -i ~/bell/bell root@198.235.66.48 rm /usr/lib/storm/lib/http*
ssh -i ~/bell/bell root@198.235.66.49 rm /usr/lib/storm/lib/http*

scp -i ~/bell/bell *.jar root@198.235.66.40:/usr/lib/storm/lib
scp -i ~/bell/bell *.jar root@198.235.66.41:/usr/lib/storm/lib
scp -i ~/bell/bell *.jar root@198.235.66.42:/usr/lib/storm/lib
scp -i ~/bell/bell *.jar root@198.235.66.43:/usr/lib/storm/lib
scp -i ~/bell/bell *.jar root@198.235.66.44:/usr/lib/storm/lib
scp -i ~/bell/bell *.jar root@198.235.66.45:/usr/lib/storm/lib
scp -i ~/bell/bell *.jar root@198.235.66.46:/usr/lib/storm/lib
scp -i ~/bell/bell *.jar root@198.235.66.47:/usr/lib/storm/lib
scp -i ~/bell/bell *.jar root@198.235.66.48:/usr/lib/storm/lib
scp -i ~/bell/bell *.jar root@198.235.66.49:/usr/lib/storm/lib

