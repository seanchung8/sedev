#!/usr/bin/perl

use Date::Parse;
use File::Basename;
use strict;

my $directory = $ARGV[0];

my @files = glob("$directory/*");
$/ = undef;
my ($f);
print "query total_time predicted_mr_jobs actual_mr_jobs launch_time max_hash_mem total_map " .
      "total_reduce hdfs_read hdfs_write total_cpu\n";
foreach $f (@files) {
	open(TEMP, $f) || next;
	my $r = <TEMP>;
	my $fn = basename($f);

	my $time = getTime($r) || "fail";
	my ($predicted, $actual) = getTotalJobs($r);
	$predicted ||= "fail";
	$actual ||= "fail";
	my $query = getQuery($r) || "not_set";
	my $launchTime = getLaunchTime($r) || "fail";
	my $maxHashMem = getMaxHashMem($r) || "NA";
	my ($totalMap, $totalReduce) = getTotalMapReduce($r);
	my ($totalRead, $totalWrite) = getTotalReadWrite($r);
	my $totalCpu = getTotalCpu($r);
	print "$fn $query $time $predicted $actual $launchTime $maxHashMem $totalRead " .
	      "$totalWrite $totalMap $totalReduce $totalCpu\n";
}

sub getQuery {
	my $r = shift;
	if ($r =~ m|queries/(query\d+.sql)|) {
		return $1;
	}
}

sub getTime {
	my $r = shift;
	if ($r =~ m|Time taken: ([0-9\.]+?) seconds,|gs) {
		return $1;
	}
}

sub getTotalJobs {
	my $r = shift;
	my $r2 = $r;
	my $total;

	if ($r =~ m|Total MapReduce jobs = (\d+)|gs) {
		$total = $1;
	}
	my @matches = ($r2 =~ m|filtered out by condition resolver|gs);
	my $net = $total - ($#matches+1);
	return ($total, $net);
}

sub getLaunchTime {
	my $r = shift;

	# Calculate the launch time.
	my $start = "none";
	my $launch = "none";
	if ($r =~ m|(\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2})|gs) {
		$start = $1;
	}
	if ($r =~ m|(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})|gs) {
		$launch = $1;
	}
	if ($start eq "none" || $launch eq "none") {
		return "NA";
	}
	my $time1 = str2time($start);
	my $time2 = str2time($launch);
	my $diff = $time2 - $time1;
	if ($diff > 12 * 60 * 60) {
		$diff -= (12 * 60 * 60)
	}
	return $diff
}

sub getMaxHashMem {
	my $r = shift;
	my @sizes = ($r =~ m|Memory usage:\s+(\d+)|gs);
	@sizes = sort { $b <=> $a } @sizes;
	my $max = $sizes[0];
	$max = commaify($max);
	return $max;
}

sub commaify {
	my $max = shift;
	$max = reverse $max;
	$max =~ s/(\d\d\d)(?=\d)(?!\d*\.)/$1,/g;
	$max = reverse $max;
}

sub getTotalMapReduce {
	my $r = shift;
	my @mr = ($r =~ m|Job \d+: Map:\s+(\d+)\s+(Reduce: \d+)?|gs);
	my $totalMap = 0;
	my $totalReduce = 0;
	#print join(", ", @mr), "\n";
	foreach (@mr) {
		if ($_ =~ m|Reduce: (\d+)|) {
			$totalReduce += $1;
		} else {
			$totalMap += $_;
		}
	}
	#print "$totalMap, $totalReduce\n";
	return ($totalMap, $totalReduce);
}

sub getTotalReadWrite {
	my $r = shift;
	my @rr = ($r =~ m|HDFS Read: (\d+) HDFS Write: (\d+)|gs);
	my $totalRead = 0;
	my $totalWrite = 0;
	my $i;
	for ($i = 0; $i <= $#rr; $i += 2) {
		$totalRead += $rr[$i];
		$totalWrite += $rr[$i+1];
	}
	$totalRead = commaify($totalRead);
	$totalWrite = commaify($totalWrite);
	return ($totalRead, $totalWrite);
}

sub getTotalCpu {
	my $r = shift;
	my @cpu = ($r =~ m|Job \d+: Map:\s+\d+.+?\Cumulative CPU: ([\d\.]+)|gs);
	my $cpu = 0;
	foreach (@cpu) {
		$cpu += $_;
	}
	return $cpu;
}
