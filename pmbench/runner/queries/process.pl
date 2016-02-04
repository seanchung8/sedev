#!/usr/bin/perl

$directory = $ARGV[0];

@files = glob("*.sql");
$/ = undef;
foreach $f (@files) {
	open(TEMP, "$directory/$f.results" . $backup) || next;
	$r = <TEMP>;
	$r1 = $r;
	$r2 = $r;
	$time = "fail";
	$total = "fail";
	if ($r =~ m|Time taken: ([0-9\.]+?) seconds,|gs) {
		$time = $1;
	}
	if ($r1 =~ m|Total MapReduce jobs = (\d+)|gs) {
		$total = $1;
	}
	@matches = ($r2 =~ m|filtered out by condition resolver|gs);
	$net = $total - ($#matches+1);
	print "$f $time $total $net\n";
}
