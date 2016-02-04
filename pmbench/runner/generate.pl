#!/usr/bin/perl

use strict;
use Getopt::Long qw(:config bundling);
use Proc::ProcessTable;

# Parse and set up options.
my (%config, @INCLUDES);
GetOptions("d:s" => \$config{"DATABASE"},
	   "h:s" => \$config{"HIVE"},
	   "n:s" => \$config{"NUMBER_RUNS"},
	   "q:s" => \$config{"QUERY"},
	   "r:s" => \$config{"RUNID"},
	   "s:s" => \$config{"SIMULTANEOUS"},
	   "x:s" => \$config{"DEBUG"},
	   "i:s" => \@INCLUDES);
$config{"HIVE"} ||= "hive";
$config{"RUNID"} ||= $$;
$config{"AVG_WAIT_BETWEEN_LAUNCH"} ||= 10;
if (!$config{"DATABASE"}) {
	print <<END;
Specify a database with -d

Usage:
generate -d database [-h hive] [-n #runs] [-q query] [-s simultaneous]
		     [-i include] [-r runid]

Options:
-d Database to use. Mandatory.
-h If you don't want to use hive from the path, specify an alternate here
-n Number of queries to run
-q The query to run if you only want to run one query multiple times
-s Number of simultaneous executions
-i Hive include file. Can be used more than once.
-r Runid directory. By default will use $$.

Samples:
# Run all queries, one at a time.
generate -d orc_scale_200

# Run 30 instances of query 27, up to 5 simultaneously.
generate -d orc_scale_200 -q query27.sql -n 30 -s 5

# Run 1 instance of query 27.
generate -d orc_scale_200 -q query27.sql -n 1 -s 1

# Run a mix of 50 queries, up to 10 at a time.
generate -d orc_scale_200 -n 50 -s 10
END
	exit(1);
}
foreach (@INCLUDES) {
	$config{"INIT"} .= "-i settings/$_ ";
}

# Build the list of queries.
my @files = glob("queries/*.sql");

# Make the output directory.
if (!$config{"DEBUG"}) {
	mkdir("runs/" . $config{"RUNID"});
}

# Run selected test set.
if ($config{"NUMBER_RUNS"}) {
	if ($config{"QUERY"}) {
		runQueries(\%config);
	} else {
		runQueries(\%config, \@files);
	}
} else {
	runSequential(\%config, \@files);
}

# Run all queries once, sequentially.
sub runSequential {
	my ($config, $fileAry) = @_;
	my ($file);

	print "Running all queries sequentially\n";
	foreach $file (@$fileAry) {
		runCommand($config, $file);
	}
}

# Run queries multiple overlapping times.
sub runQueries {
	my ($config, $fileAry) = @_;
	my $simultaneous = $config->{"SIMULTANEOUS"};
	my $totalRuns = $config->{"NUMBER_RUNS"};
	my $query = $config->{"QUERY"};

	if ($simultaneous == 0 || $totalRuns == 0) {
		die "Specify -s and -n\n";
	}
	if (!$fileAry) {
		if (!$query) {
			die "Specify -q\n";
		}
		if (! -f $query) {
			$query = "queries/$query";
		}
		if (! -f $query) {
			die "$query does not exist\n";
		}
	}

	my $i = 1;
	my $count = 0;
	while ($totalRuns > 0) {
		while ($count < $simultaneous && $totalRuns > 0) {
			my $n = sprintf("%05d", $i);
			if ($fileAry) {
				$query = @$fileAry[int(rand($#$fileAry+1))];
			}
            # START bad bad bad
            my $background = 1;
            if ( $simultaneous == 1 ) { $background = 0; }
			runCommand($config, $query, "session$n", $background);
			# END bad bad bad
            sleep(1);
			$i++;
			$count++;
			$totalRuns--;
		}

		# Get an updated child process count.
		if ($totalRuns > 0) {
			sleep(3);
			$count = countChildProcesses();
			print "Child count now = $count, Sessions remain = $totalRuns\n";
		}
	}
}

sub countChildProcesses {
	my $n = 0;
	for my $p (@{new Proc::ProcessTable->table}) {
		#print "name = ", $p->cmndline, "ppid = ", $p->ppid, " me = ", $$, "\n";
		if ($p->ppid == 1 && $p->cmndline =~ m|getdate.settings|) {
			$n++;
		}
	}
	return $n;
}

# Run an individual query.
sub runCommand {
	my ($config, $query, $outname, $background) = @_;
	my $H = $config->{"HIVE"};
	my $I = $config->{"INIT"};
	my $D = $config->{"DATABASE"};
	my $R = $config->{"RUNID"};
	$outname ||= $query;
	open(TEMP, ">runs/$R/$outname.results");
	print TEMP "$query\n";
	close(TEMP);
	my $command = "$H $I -i settings/init.settings -i settings/getdate.settings " .
		"--database $D -f $query >> runs/$R/$outname.results 2>&1";
	print $command, "\n";
	if ($config->{"DEBUG"}) {
		return;
	}
	if ($background) {
		system("$command &");
	} else {
		system($command);
	}
}


