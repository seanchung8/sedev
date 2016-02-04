#!/bin/sh

queries=`ls *.sql`
for q in $queries; do
	echo $q

	# Clear OS buffers.
	sudo sh -c 'echo 3 > /proc/sys/vm/drop_caches'

	# Reset postgres stats.
	sudo -u postgres psql tpc -c 'select pg_stat_reset();';

	# Run the query.
	{ time psql tpc -f $q; } > outcome/$q.time 2>&1

	# Get the query stats.
	psql tpc -c 'select * from pg_stat_database;' > outcome/$q.stats
done
