#!/usr/bin/python
# This will read the input from stdin from the radsniff program
# and output the IP, Name, Info, Date 
# into a mysql database

import sys
import time
import MySQLdb as mdb 
import warnings

warnings.filterwarnings('ignore')

dbuser = 'ipuser'
dbpass = 'lookup'
db     = 'ipinfodb'
dbhost = 'localhost'
Otag   = 'DSL'

try:
   con = mdb.connect(dbhost,dbuser,dbpass,db)
   cur = con.cursor()
   cur.execute("SELECT VERSION()")
   ver = cur.fetchone()
   # print "Connected to MySQL database version: %s " % ver
except mdb.Error, e:
   print "Error %d: %s" % (e.args[0], e.args[1])
   sys.exit(1)

sql = "CREATE TABLE IF NOT EXISTS ipinfo (     \
          ip VARCHAR(40) NOT NULL PRIMARY KEY, \
          tag VARCHAR(10),                     \
          client VARCHAR(30),                  \
          info VARCHAR(100),                   \
          updatetime VARCHAR(20) ); "
cur.execute(sql)

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
         #Oname = uname.replace('"','')
         Oinfo = '"' + NASId.replace('"','').rstrip()+ ' - ' + NASPortId.replace('"','').rstrip()+ '"'
         #Oinfo = Oinfo.replace('\\','_')
         Oinfo = Oinfo.replace('/','_')
         Oinfo = Oinfo.replace('GigabitEthernet','GE')
         ETime = str(time.time()) 

         sql = "REPLACE INTO ipinfo(ip,tag,client,info,updatetime) VALUES('%s','%s','%s','%s','%s') " %  (Oip, Otag, Oname, Oinfo, ETime)
         try:
           cur.execute(sql)
           con.commit()
         except:
           print "db Commint failed on %s - %s" % (Oip, Iname)
           con.rollback()

         uname = ''
         NASIP = ''
         FramedIP = ''
         NASId = ''
         StatusType = ''
         NASPortID = ''
         ETime = ''

con.close()
