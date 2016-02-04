import time, sys 

# NOTE: This script only creates the expected values from a LR run.  It does NOT yet automatically check for the correct number of output rows and the expected values in those rows. 
# USAGE: python validate.py <cardatafile> <historicaltolls> <outfile>
# validate.py: since the validation code is so convoluted I am creating a simple python script to run the validation.
## The only issues will be:
### speed
### memory size -- will it be able to hold all the data structures in simple python maps/dicts?
# what's the largest number of xways this script can process?  With 112GB?
#### Keep in mind that there's no guarantee that the vendor output will match the order of this validator output 

pos_reports = {}  # key: type+time+carid; values: [speed, xway, lane, dir]
cars = {}  # key: carid; values: [time[0], speed[1], xway[2], lane[3], dir[4], seg[5], pos[6], xpos[7], ltoll[8], bal[9], bal1[10], bal2[11]]  # All the car values represent the _last_ or _previous_ car stats
segs = {}  # key: xway+seg+dir+min: values: [lav[0], numv[1], numv4lav[2]]  # seg data is only relevant on this key
stopped = {}  # key: xway+lane+dir+seg+pos; values [carid...]  # a list of carids
#accidents = {}  # key:xway+lane+dir+seg; values: [carid1, carid2, ..., time]  # time being the initial time of the accident, and can there be more than one stopped car in a location?  I guess we'll find out.
accidents = {}  # key:xway+lane+dir+seg; values: [carid1, carid2, time]  # moving forward with only two cars can be part of an accident (we'll see if the mitsim data holds)
tolls = {} # key: carid; values: [toll1, ...]
outfile = open(sys.argv[3],"w")

num_t0out = 0
num_t1out = 0
num_t2out = 0
num_t3out = 0

# 0 - type
# 1 - time
# 2 - carid
# 3 - speed 
# 4 - xway
# 5 - lane
# 6 - dir
# 7 - seg 
# 8 - pos 
# 9 - qid

###########################################################
# Calculate the toll
###########################################################
def calctoll(numv):
	return 2 * (50-numv)**2	

###########################################################
# Update a field in a list in a map
###########################################################
def update(d, key, index, nv):  # (dict, key, index, newvalue) -- index assuming the value of this dict is a list, which it is in our case
	d[key][index] = nv

###########################################################
# Print current stopped cars 
###########################################################
def print_stopped():
	print "###########################################"
	print "STOPPED"
	print stopped 
	print "###########################################"
###########################################################
# Print current accidents 
###########################################################
def print_accidents():
	print "###########################################"
	print "ACCIDENTS"
	print accidents
	print "###########################################"

#############################################################	
# Process type 0 reports 
#############################################################	
def t0(t):
	new_seg_flag = False 
	starttime = time.time()
	# Create these two keys for potential use
	stopped_key = t[4]+"-"+t[5]+"-"+t[6]+"-"+t[7]+"-"+t[8]  # xway[4], lane[5], dir[6], seg[7], pos[8] # Keep this, and other multi-field keys as strings
	accident_key = t[4]+"-"+t[6]+"-"+t[7]  # xway[4], dir[6], seg[7]  # The lane is not important
	ptime = int(t[1])  # Change the other fields to ints to save space ... and maybe allow faster processing
	carid = int(t[2])
	speed = int(t[3])
	xway  = int(t[4])
	lane  = int(t[5])
	dir   = int(t[6])
	seg   = int(t[7])
	pos   = int(t[8])

	min = ptime/60+1
	seg_key = str(xway)+"-"+str(seg)+"-"+str(dir)+"-"+str(min)  # 180 minutes in a 3 hour sim.  180 * 2  * numxways * 100  = 3600 ... not much for 1 xway, or even large numbers of xways # (dirs and segs)
	# Check if this is the first time this xway segment has seen as car in this dir, then create the first seg to work on (of course tolls will be 0)
	if seg_key not in segs:  # If we don't have any stats yet for this segment, simply enter them 
		# Enter default values, and this should only happen xways * 100 * 2 (dir) times!  And the first time will just be the speed of the first vehicle!
		segs[seg_key] = [float(speed), 1, 1]
		# DO NOTHING and let the segment be added at the end
		print "New seg_key: " + seg_key	
		#print segs
	# Assume a zero toll initially
	toll = 0
	#######################################################################################################
	# DOES CAR ALREADY EXIST IN DATA SET?  if not, enter with deafult values, if so get the car
	#######################################################################################################
	if carid not in cars:
		# Enter the car information with default values 
		cars[carid] = [ptime, speed, xway, lane, dir, seg, pos, 1, 0, 0, 0, 0] 
		print "Currently working with NEW car: " + str(carid) + ": " +  ','.join(map(str,cars[carid]))  #D
		# SO, do we or do we NOT send a toll notification for a new car on its FIRST appearance?
		# 1. NO, the p_previous is undefined so forget it (keep it simple)
		# 2. YES, the p_previous is undefined and this new seg IS new, so get the toll notification
	else:
		car = cars[carid]
		print "Currently working with car: " + str(carid) + ": " +  ','.join(map(str,car))  #D
		########################################################################################
		# IS THIS CAR IN THE SAME POSITION AS IT WAS IN THE PREVIOUS REPORT?
		########################################################################################
		if car[6] == pos:	
			print "Same pos for :" + str(carid) + " - pos: " + str(pos)  #D
			# Check if this is a stopped car, i.e. this is the fourth consecutive report in the same pos
			if car[7] >= 3:  # Meaning, _this_ is at least the fourth time, and 3 or greater was the last time
				print "stopped_key: " + stopped_key	
				print "At least fourth time at this pos: " + str(car[7]) + " times"  #D
				# Check if this car already exists here, if so skip.  If another car has stopped here, _it_ will take care of adding the accident
				if stopped_key in stopped and carid in stopped[stopped_key]:
					print "Already in stopped list, nothing to do"  #D
					print_stopped()
				###################################################################
				# CREATE AN ACCIDENT
				###################################################################
				elif stopped_key in stopped:  # There is a stopped car at this pos but it's not the current car
					print "Adding an accident..." #D	
					stopped[stopped_key].append(carid)
					print_stopped()
					if accident_key not in accidents:
						stopped_cars = stopped[stopped_key]
						accidents[accident_key] = [stopped_cars[0], stopped_cars[1], ptime]
					print_accidents()
					# This is where we'd ideally set up the accident
				###################################################################
				# CREATE A STOPPED CAR 
				###################################################################
				else:  #  This is the first car to stop at this pos, because if another one shows up, it's an accident
					print "Adding a car to stopped..."  #D
					stopped[stopped_key] = [carid]
					print_stopped()
			# Increment xpos
			print "Updating xpos for car " + str(carid) + " from " + str(car[7]) + " to " + str(car[7]+1)
			update(cars, carid, 7, car[7]+1) 
				# Check if the number of stopped cars at this position > 1 and if there is no accident at this xway+lane+dir+seg+pos, create an accident; else there already is an accident so pass 
				#if len(stopped[stopped_key]) > 1 and accident_key not in accidents:
				#	for carid in stopped[stopped_key]:
				#		accidents[accident_key].append(carid) 
				#	accidents[accident_key].append(ptime)
				#else:
				#	pass
		# If this isn't the same pos as previously check if this car was part of an accident and clear the accident if it was.  
		## Again, this assumes no more than two cars can be involvd in an accident.  If more than two cars can be simply add the logic to account for that. 
		##########################################################################
		# NEW POS FOR CAR, NOT NECESSARILY NEW SEGMENT
		##########################################################################
		else:
			print "New pos for car: " + str(carid)  #D
			###########################################################
			# REMOVE STOPPED CAR from stopped if it's there
			###########################################################
			removed_stopped_flag = False #D 
			for key in stopped:
				if carid in stopped[key]:
					print "Removing " + str(carid) + " from stopped"
					stopped[key].remove(carid)
					if len(stopped[key]) == 0:
						del stopped[key]
						removed_stopped_flag = True
						break;
			if removed_stopped_flag == True:
				print_stopped()
			#if stopped_key in stopped and carid in stopped[stopped_key]:  # the car might have changed lanes!
			#	print "Removing carid " + str(carid) + " from stopped"	
			#	stopped[stopped_key].remove(carid)
			#	print "stopped: " + ",".join(map(str,stopped[stopped_key]))  #D
				# And reset the xpos counter
			update(cars, carid, 7, 1) 
			# Clear the car from accident if it's there
			## This leaves open the possibility that a carid will overlap with a time ... sooooo, either assume ONLY two possible cars or ...
			###########################################################
			# CLEAR ACCIDENT IF ANY INVOLVING THIS CAR
			###########################################################
			# Same as stopped, the car may have crossed segments and the accident_key will now be different!
			#if accident_key in accidents and carid in accidents[accident_key] and carid != accidents[accident_key][0] and carid != accidents[accident_key][1]: 
			#if carid in accidents[accident_key] and carid != accidents[accident_key][len(accidents[accident_key])-1]: # This leaves open the possibility that a carid will overlap with a time ... sooooo, either assume ONLY two possible cars or ...
			# Not the most efficient but it'll have to do for now
			removed_accident_flag = False  #D
			for key in accidents:
				if carid in accidents[key]:
					print "Clearing accident: " + ','.join(map(str, accidents[key]))  #D
					del accidents[key]  # NOTE: accidents.pop(key) won't work here because of the dict mutation while iterating
					removed_accident_flag = True  #D
					break;
			if removed_accident_flag == True:  #D
				print_accidents()
				#if len(accidents[accident_key]) == 3:  # This is a grand assumption, [carid1, carid2, time] and not [carid, carid..., time]
					# Clear the accident
					#accidents.pop[accident_key]
			# Since this isn't the same pos, check if it's in the same segment
			if car[5] == seg:
				# Do we do anything here?  No notifications ... do check for upstream accidents I believe ... and I was wrong, only at new segments
				# Update seg stats?  I think so ... but you do that at the bottom
				# At least check if the car has moved to lane = 4, if it has reset the toll so it isn't charged when the come back on 
				if lane == 4:
					toll = 0 
					 
			#######################################################################################################################
			#######################################################################################################################
			# NEW SEGMENT: check for upstream accidents, calculate and send toll notification, update seg stats, assess toll from previous seg if any 
			#######################################################################################################################
			#######################################################################################################################
			else: 	
				print "NEW SEGMENT for " + str(carid)
				new_seg_flag = True
				##############################################################
				# TOLL CALCULATION 
				##############################################################
				numv = 1  # initial assumed number of vehicles and speeds in the segment
				total_speed = 0.0  # The sum of all speeds 
				total_speed_readings = 0 
				lav_keys = []
				lav = 0
				######################################################
				# GET NUMV  (remember, from last 1 minute)		
				######################################################
				last_min_key = str(xway)+"-"+str(seg)+"-"+str(dir)+"-"+str(min - 1)  # Gather data from the last full minute if it exists
				if last_min_key in segs:  # If we're just starting this key won't exist since it will be min = 0 
					numv = segs[last_min_key][1]
					print "NUMV for seg " + last_min_key + " in the last minute is " + str(numv)
					if numv > 50: 
						toll = calctoll(numv)	
						print "TOLL FROM NUMV: " + str(toll)
				######################################################
				# GET LAV (remember, from last 5 minutes)  We only do this portion if numv was high enough to assign a toll.  This is an opp to see if lav speed is high enough to remove the toll
				######################################################
				#if toll > 0:  # Calc every time, because we need it for the ouput  ##-if numv assigned a toll, see if speed can erase it
				for i in xrange(1,6):
					lav_keys.append(str(xway)+"-"+str(seg)+"-"+str(dir)+"-"+str(min-i))
				# Get all speeds and counts for the last 5 minutes and calc an LAV; remember, using number of vehicle speed ratings, not the number of vehicles
				for akey in lav_keys:
					if akey in segs: 
						total_speed += round(segs[akey][0] * segs[akey][2])  # may need round to int to avoid creeping errors		
						total_speed_readings += segs[akey][2]
						print "TOTAL_SPEED: " + str(total_speed) + " and TOTAL_SPEED_READINGS " + str(total_speed_readings)
				if total_speed_readings > 0:
					lav = total_speed / total_speed_readings
					print "LAV for segs " + ','.join(lav_keys) + " in the last minute is " + str(lav) 
					if int(round(lav)) >= 40:  # If this doesn't hold true, we let the toll stand; don't really need the cast to int, but maybe it's safer
						toll = 0  # look for any bugs with lav >= 40 having a toll -- so, the errors (toll even though lav>=40) occur from time 960 - 1703.  ???
							# AND, the bug was because of float vs. int.  AND, it's NOT a bug.  It's actually a good discovery!  Why?  The speed of the car IS NOT
							## what's supposed to go into the output.  It's the LAV, right?

				##################################################################
				# CHECK FOR UPSTREAM ACCIDENTS 
				##################################################################
				# Form the keys for five segments beyond the current seg, based on xway, seg, and dir.  If there is an accident in any of these segments an accident report will be generated
				accident_keys = []
				if dir == 0:
					for i in xrange(1,6):
						accident_keys.append(str(xway)+"-"+str(dir)+"-"+str(seg+i))
				else:
					for i in xrange(1,6):
						accident_keys.append(str(xway)+"-"+str(dir)+"-"+str(seg-i))
				# Check for accidents
				for akey in accident_keys:
					if akey in accidents:
						toll = 0
						#############################################################	
						#############################################################	
						# WRITE OUT THE ACCIDENT NOTIFICATION! 
						#############################################################	
						#############################################################	
						outfile.write("1,"+str(ptime)+","+str(ptime+(time.time()-starttime))+","+str(xway)+","+str(seg)+","+str(dir)+","+str(carid)+"\n")
						print "OUTPUT TYPE 1: 1,"+str(ptime)+","+str(ptime+(time.time()-starttime))+","+str(xway)+","+str(seg)+","+str(dir)+","+str(carid)
						global num_t1out
						num_t1out += 1
						break

				############################################################
				# ASSESS PREVIOUS TOLL if any (before or after notification?)
				############################################################
				if cars[carid][8] > 0: # Remember, this is the toll from a previous segment
					# If you want to keep the last three balances, for the sake of "result time," move them down
					cars[carid][11] = cars[carid][10] # balance at time - 60
					cars[carid][10] = cars[carid][9]  # balance at time - 30 
					# Add the PREVIOUS 'toll' to the balance of the car, [9].  
					cars[carid][9] += cars[carid][8] # Remember, we're assessing the _last_ toll, not the current toll!	
					if carid in tolls:
						tolls[carid].append(cars[carid][8])
					else:
						tolls[carid] = [cars[carid][8]]
					print "Tolls so far CHARGED for car " + str(carid) + ": " + ','.join(map(str, tolls[carid]))
					#print "Current toll and new balance and balance history of current car: " + str(cars[carid][8])  + ", " + str(cars[carid][9]) + ", " + str(cars[carid][10]) + ", " + str(cars[carid][11])  # 
					print "Current car " + str(carid) + ": " + ','.join(map(str,cars[carid]))

				##############################################
				# LASTLY, if EXIT LANE, then NO notification is sent!  (BUT, lane changes don't only happen at new segs, they happen at new pos reports!)
				##############################################
				# Does a car ever enter lane 4 and then jump out again?  Doesn't _appear_ to be the case
				if lane != 4:
					#############################################################	
					#############################################################	
					# WRITE OUT THE TOLL NOTIFICATION! 
					#############################################################	
					#############################################################	
					outfile.write("0,"+str(carid)+","+str(ptime)+","+str(ptime+(time.time()-starttime))+","+str(int(round(lav)))+","+str(toll)+"\n")  # little bugs
					print "OUTPUT TYPE 0: 0,"+str(carid)+","+str(ptime)+","+str(ptime+(time.time()-starttime))+","+str(int(round(lav)))+","+str(toll)
					global num_t0out
					num_t0out += 1


	########################################################
	# UPDATE CAR STATS, BY THE MINUTE
	########################################################
	car = cars[carid]
	car[0] = ptime
	car[1] = speed
	car[2] = xway
	car[3] = lane
	car[4] = dir
	car[5] = seg
	car[6] = pos
	#car[7] # Taken care of above if applicable 
	car[8] = toll 
	#car[9] # Taken care of above if applicable
	#car[10] # Taken care of above if applicable
	#car[11] # Taken care of above if applicable
	print "IS THE CAR UPDATED? " + str(carid) + ": " + ','.join(map(str,(cars[carid]))) 

	########################################################
	# UPDATE SEG STATS
	########################################################
	# Update previous segment stats
	# Update current segment stats
	# Check if seg stats exists for this xway,seg,dir,min, with values [lav, numv, numv4lav]
	if seg_key in segs:  # Pull and update values
		curr_seg = segs[seg_key]
		# Update lav
		prev_total = round(curr_seg[0] * curr_seg[2])
		# Update numv, ONLY IF THE CAR HAS JUST MOVED INTO THIS SEGMENT
		# BUT, still need 'number of vehicles in avg' type of field 
		if new_seg_flag == True:
			curr_seg[1] += 1
		# Incr the num vehicles to be used for the average
		curr_seg[2] += 1
		curr_seg[0] = (prev_total + speed) / curr_seg[2]
		# Send it back to the segs dict
		segs[seg_key] = curr_seg

	if seg_key in segs:  # D
		print "UPDATED SEGMENT: " + seg_key + ": " + ','.join(map(str, segs[seg_key]))
		#print segs[seg_key]  # I guess we don't have to remove cars because once that minutes done, ..., no, you do, ...
	print "DONE PROCESSING pos report for " + str(carid) + " at time " + str(ptime)

#############################################################	
# Process type 2 reports 
#############################################################	
# The way this validator calculates daily balance queries MAY NOT be the way solutions should calculate them.
# Solutions should be tracking the charged tolls per vehicle and summing
def t2(t): 
	starttime = time.time()
	print t	
	ptime = int(t[1])  # Change the other fields to ints to save space ... and maybe allow faster processing
	carid = int(t[2])
	speed = int(t[3])
	xway  = int(t[4])
	lane  = int(t[5])
	dir   = int(t[6])
	seg   = int(t[7])
	pos   = int(t[8])
	qid   = int(t[9])

	answer = 0
	# For the sake of the validator, the daily balance query can be answered by keeping a balance field[s] (fields for holding up to balances, or
	## it can just track the tolls in a list, as is currently done, or
	#if carid in cars:
	#	answer = cars[carid][9] 
	#else:
	#	answer = -1
	if carid in tolls:
		charges = tolls[carid]
		for charge in charges:
			answer += charge

	# Result time is just the time from time to time - 60 that the result comes from
	# In this case it's always just time ... or is it?  It's not, the pos report can come before, or after, the t2 query
	# To that point, it's processed at the same 'time' BUT it can be processed before, after, or (if threaded) concurrently as the pos report
	# If the pos report comes before: 	cars[carid][9] 
	# If the pos report comes after: 	cars[carid][9] # it's still [9], it only becomes relevant when deciding if the value is valid 
	
	# Check if the pos report for this time has already been seen
	resulttime = ptime	
	# Check if pos report for this time has not been seen (or is concurrent) resulttime = time - 30 (because the toll will not have been assessed yet)
	if carid in cars: # Remember, whatever is pulled will be the last seen car, whether current or from 30 secs ago
		if cars[carid][0] == ptime: 	# This car had its position report processed for this time
			pass
		elif cars[carid][0] == ptime - 30:   # This simply accounts for the fact that the current query is answered before the current pos report is processed
			resulttime = ptime - 30
		# The documents says t2 and t3 queries will accompany pos reports. The document seems to say the t2 query can process and return balance results up to 60 seconds back.
		#elif cars[carid][0] == ptime - 60:   # This isn't really used here.  Maybe more relevant when checking results, not creating them. 
		#	resulttime = ptime - 60
	# If the pos report has already been seen resulttime = time
		# Do nothing here
			
	#############################################################	
	#############################################################	
	# WRITE OUT THE TYPE 2 NOTIFICATION! 
	#############################################################	
	#############################################################	
	outfile.write("2,"+str(ptime)+","+str(ptime+(time.time()-starttime))+","+str(resulttime)+","+str(qid)+","+str(answer)+"\n")
	print "OUTPUT TYPE 2: 2,"+str(ptime)+","+str(ptime+(time.time()-starttime))+","+str(resulttime)+","+str(qid)+","+str(answer)
	global num_t2out
	num_t2out += 1


#############################################################	
# Process type 3 reports 
#############################################################	
# This dict will hold the historical tolls information
# [carid, day, xway, amt]
# key: carid, day, xway; value: amt
historical = {}
history = open(sys.argv[2])
for h in history:
	t = h.strip().split(',')
	key = t[0]+t[1]+t[2]
	historical[key] = int(t[3]) 

def t3(t):
	starttime = time.time()
	print t	
	ptime = int(t[1])  # Change the other fields to ints to save space ... and maybe allow faster processing
	carid = int(t[2])
	speed = int(t[3])
	xway  = int(t[4])
	lane  = int(t[5])
	dir   = int(t[6])
	seg   = int(t[7])
	pos   = int(t[8])
	qid   = int(t[9])
	day   = int(t[14])

	key = str(carid)+str(day)+str(xway)
	print "HISTORICAL KEY: " + key
	answer = 0
	if key in historical: 	
		answer = historical[key]
	else: # shouldn't happen but ...
		answer = -1	
	#############################################################	
	#############################################################	
	# WRITE OUT THE TYPE 3 NOTIFICATION! 
	#############################################################	
	#############################################################	
	outfile.write("3,"+str(ptime)+","+str(ptime+(time.time()-starttime))+","+str(qid)+","+str(answer)+"\n")
	print "OUTPUT TYPE 3: 3,"+str(ptime)+","+str(ptime+(time.time()-starttime))+","+str(qid)+","+str(answer)
	global num_t3out
	num_t3out += 1


# Main
positions = open(sys.argv[1])

# Keep counts of how many of each type we see
num_t0 = 0
num_t2 = 0
num_t3 = 0
num_t4 = 0

for report in positions:
	t = report.strip().split(",")
	# we don't really need to keep the positions
	# AND, these Azure Samll boxes only have 1.75 GB of memory ... FYI
	#pos_reports[t[0]+t[1]+t[2]]=[t[3],t[4],t[5],t[6],t[7],t[8]]
	#print t[0]+','+t[1]+','+t[2] +','+ ','.join(pos_reports[t[0]+t[1]+t[2]])
	ptype = 	t[0]
	ptime = 	t[1]
	pcarid = 	t[2] 
	pspeed =	t[3]
	pxway = 	t[4]
	plane = 	t[5]
	pdir = 		t[6]
	pseg = 		t[7]
	ppos = 		t[8]
	pqid = 		t[9]

	print "Time: " + ptime + ", Type: " + ptype

	if ptype == '0':
		t0(t)
		# update the car stats
		# update the seg stats	
		num_t0 += 1
	elif ptype == '2':
		t2(t)
		num_t2 += 1
	elif ptype == '3':
		t3(t)
		num_t3 += 1
	elif ptype == '4': 
		num_t4 += 1
	

print "Number of type 0 processed: " + str(num_t0)	
print "Number of type 2 processed: " + str(num_t2)	
print "Number of type 3 processed: " + str(num_t3)	
print "Number of type 4 seen: " + str(num_t4)	

######################################################
# Report how many of each report was created
######################################################
print "Number of type 0 toll notifications sent: " + str(num_t0out)
print "Number of type 1 accident reports: " + str(num_t1out) 
print "Number of type 2 balance queries: " + str(num_t2out) 
print "Number of type 3 historical balance queries: " + str(num_t3out)
