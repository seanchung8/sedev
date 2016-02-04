import sys

# validate_type0.py: upload all type0 queries for validation output and solution output and compare them
# Usage: python validate_type0.py <validation_file> <solution_file>
# Or, this will simply be incorporated into the main validation script
if len(sys.argv) != 3:
	print "Usage: python validate_type0.py <validation_file> <solution_file>"

# Load validation file of type0 
val_dict = {} 
val_file = open(sys.argv[1])
for line in val_file:
	tokens = line.strip().split(',')	
	if tokens[0] == "0":  # Only pull type 0 toll notifications
		key = tokens[1]+"-"+tokens[2]  # The key(string): carid[1]-time[2].   The value(list:[int,float,int]): emit[3],LAV[4],toll[5]
		val_dict[key] = [float(tokens[3]),int(tokens[4]),int(tokens[5])]

# Verify count
print "Number of validation records: " + str(len(val_dict))

# Load solution file of type0
sol_dict = {} 
sol_file = open(sys.argv[2])
for line in sol_file:
	tokens = line.strip().split(',')	
	if tokens[0] == "0":  #  Same as above, although this should be moot if the outfile only contains type 0 toll notifications
		key = tokens[1]+"-"+tokens[2]  
		sol_dict[key] = [float(tokens[3]),int(tokens[4]),int(tokens[5])]

# Verfiy count
print "Number of solution records: " + str(len(sol_dict))

# Compare counts
if len(val_dict) == len(sol_dict):
	print "Counts check: PASSED"
else:
	print "Counts check: FAILED"

# Compare tolls amount of each results (val to sol).  Look for the key from val in sol.  Then, look for and compare values.
## If the values match, store the keys in the the validated dict with expected vs. received and a "passed" flag 
validated_present = {}  # Validate the presence of a value.  key, Value: "PASSED"|"FAILED" 
validated_emits = {}  # Same key as above.  Value: [emit - time],"PASSED"|"FAILED"  # Needs to be < 5  # Need to parse time from key!
validated_lavs = {}  #  Value: [val lav, sol lav, "PASSED"|"FAILED"
validated_tolls = {}  # Value: [val toll, sol toll, "PASSED"|"FAILED" 
for key in val_dict:
	if key in sol_dict:
		# Validate the presence of the key
		validated_present[key] = "PASSED"

		# Get the Values lists
		val_vals = val_dict[key]
		sol_vals = sol_dict[key]

		# Parse key for carid and time
		carid,time=key.split('-')	

		# Check emit time, but get it first
		process_time = float(sol_vals[0]) - float(time) 
		if process_time <= 5:  # Within spec
			validated_emits[key] = "PASSED"
		else:
			validated_emits[key] = "FAILED"

		# Check lav value
		if val_vals[1] == sol_vals[1]:
			validated_lavs[key] = "PASSED"	
		else:
			validated_lavs[key] = "FAILED"	
			
		# Check toll value
		if val_vals[2] == sol_vals[2]:
			validated_tolls[key] = "PASSED"	
		else:
			validated_tolls[key] = "FAILED"	
	else:
		validated_present[key] = "FAILED"

# Now, print the results
for key in validated_present: 
	if validated_present[key] == "FAILED":
		print key + " not present in solution set"

for key in validated_emits: 
	if validated_emits[key][1] == "FAILED":
		print key + " did not finish request <= 5 seconds"

for key in validated_lavs: 
	if validated_lavs[key][2] == "FAILED":
		print key + " did not match lav from validator"

for key in validated_tolls: 
	if validated_lavs[key][2] == "FAILED":
		print key + " did not match toll from validator"



