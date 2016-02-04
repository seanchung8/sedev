#!/usr/bin/python
# This will read the input from stdin from the radsniff program
# and output the IP, Name, Info, Date 

import sys
import time

uname = ''
NASIP = ''
FramedIP = ''
NASId = ''
StatusType = ''
NASPortID = ''
ETime = ''

for line in sys.stdin:
   if "User-Name =" in line:
      junk,uname = line.split(" = ")
   if "NAS-IP-Address =" in line:
      junk,NASIP = line.split(" = ")
   if "Framed-IP-Address =" in line:
      junk,FramedIP = line.split(" = ")
   if "NAS-Identifier =" in line:
      junk,NASId = line.split(" = ")
   if "Acct-Status-Type =" in line:
      junk,StatusType = line.split(" = ")
   if "NAS-Port-Id =" in line:
      junk,NASPortId = line.split(" = ")
   if line.isspace():
      if "Stop" not in StatusType:
         if not FramedIP:
             RealIP = NASIP.rstrip()
         else:
             RealIP = FramedIP.rstrip()
         Oip = RealIP
         Oname = uname.rstrip()
         Oinfo = '"' + NASId.replace('"','').rstrip()+ ' - ' + NASPortId.replace('"','').rstrip()+ '"'
         ETime = str(time.time()) 
         print Oip + ',' + Oname + ',' + Oinfo + ',' + ETime
         uname = ''
         NASIP = ''
         FramedIP = ''
         NASId = ''
         StatusType = ''
         NASPortID = ''
         ETime = ''

