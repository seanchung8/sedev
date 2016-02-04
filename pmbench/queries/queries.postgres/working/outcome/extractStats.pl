#!/usr/bin/perl

print "query time blks_read blks_hit tup_returned_net tup_fetched_net\n";
open(FILES, "files");
while ($f = <FILES>) {
	chomp($f);
	$f =~ m|([^/]+$)|;
	$file = $1;
	$time = getTime($file);
	$time =~ m|(\d+)m(.+?)s|;
	$time = $1 * 60 + $2;
	$stats = getStats($file, $oldStats);
	print join(" ", $file, $time,
		$stats->{"blks_read"}, $stats->{"blks_hit"},
		$stats->{"tup_returned_net"}, $stats->{"tup_fetched_net"}), "\n";
	$oldStats = $stats;
}

sub getStats {
	my ($f, $oldStats, %ret);
	$f = shift;
	$oldStats = shift;

	if (!$oldStats) {
		$oldStats = getStatsInner("orig.stats");
	}
	my $newStats = getStatsInner("$f.stats");
	$ret{"blks_read"} = $newStats->{"blks_read"};
	$ret{"blks_hit"} = $newStats->{"blks_hit"};
	$ret{"tup_returned_net"} = $newStats->{"tup_returned"} - $oldStats->{"tup_returned"};
	$ret{"tup_fetched_net"} = $newStats->{"tup_fetched"} - $oldStats->{"tup_fetched"};
	$ret{"tup_returned"} = $newStats->{"tup_returned"};
	$ret{"tup_fetched"} = $newStats->{"tup_fetched"};
	return \%ret;
}

sub getStatsInner {
	my ($fname, %ret);
	$fname = shift;
	open(TEMP, $fname);
	while (<TEMP>) {
		chomp;
		@stuff = split('\s*\|\s*', $_);
		if ($stuff[1] eq "tpc") {
			$ret{"blks_read"} = $stuff[5];
			$ret{"blks_hit"} = $stuff[6];
			$ret{"tup_returned"} = $stuff[7];
			$ret{"tup_fetched"} = $stuff[8];
			return \%ret;
		}
	}
}

sub getTime {
	my $f = shift;

	open(TEMP, "$f.time");
	while (<TEMP>) {
		if ($_ =~ m|real\t(.+)|) {
			return $1;
		}
	}
}
