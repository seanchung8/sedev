

# Delete
falcon entity -type feed -delete -name hdfs-user-prod-feed
falcon entity -type cluster -delete -name sedemo-prod-backup-cluster
falcon entity -type cluster -delete -name sedemo-prod-backup-cluster

# Clusters
falcon entity -submit -type cluster -file sedemo-prod-primary-cluster.xml
falcon entity -submit -type cluster -file sedemo-prod-backup-cluster.xml

# Feeds
falcon entity -submit -type feed -file hdfs-user-prod-feed.xml

# Processes

# Schedule
falcon entity -type feed -schedule -name hdfs-user-prod-feed
#falcon entity -type process -schedule -name compute-sentiment-process


