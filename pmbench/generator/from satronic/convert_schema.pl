#!/usr/bin/perl

$dialect = $ARGV[0];
open(STREAM, "tpcds.sql");
if ($dialect eq "hive") {
	print "create database if not exists text;\n";
	convert_hive()
} elsif ($dialect eq "hive-orc") {
	print "create database if not exists orc;\n";
	convert_hive_orc("orc")
} elsif ($dialect eq "hive-rcfile") {
	print "create database if not exists rcfile;\n";
	convert_hive_orc("rcfile")
} elsif ($dialect eq "postgres") {
	convert_postgres()
} else {
	print "Unknown dialect\n";
}

sub convert_hive {
	$in = 0;
	while (<STREAM>) {
		$_ =~ s|^--.+||;
		next if ($_ =~ m|^\s*$|);
		$_ =~ s/varchar\(\d+\)/string/;
		$_ =~ s/char\(\d+\)/string/;
		$_ =~ s/decimal\(\d+,\d+\)/float/;
		$_ =~ s/not null//;
		$_ =~ s/primary key .+//;
		$_ =~ s/integer/int/;
		$_ =~ s/\Wdate\W/string/;
		$_ =~ s/\Wtime\W/string/;
		$_ =~ s/,$//;
		if ($_ =~ m|^\s*$|) {
			$in = 0;
		}
		if ($_ =~ m|\);|) {
			$in = 0;
		}
		if ($in >= 2) {
			print ", ";
		}
		if ($_ =~ m|create table (.+)|) {
			print "drop table text.$1;\n";
			print "create table text.$1\n";
		} else {
			print;
		}
		if ($in > 0) {
			$in++;
		}
		if ($_ =~ m|\(|) {
			$in = 1;
		}
	}
}

sub convert_hive_orc {
	$format = shift;
	$in = 0;
	while (<STREAM>) {
		$_ =~ s|^--.+||;
		next if ($_ =~ m|^\s*$|);
		$_ =~ s/varchar\(\d+\)/string/;
		$_ =~ s/char\(\d+\)/string/;
		$_ =~ s/decimal\(\d+,\d+\)/float/;
		$_ =~ s/not null//;
		$_ =~ s/primary key .+//;
		$_ =~ s/integer/int/;
		$_ =~ s/\Wdate\W/string/;
		$_ =~ s/\Wtime\W/string/;
		$_ =~ s/,$//;
		if ($_ =~ m|^\s*$|) {
			$in = 0;
		}
		if ($_ =~ m|\);|) {
			$_ = ") stored as $format;\n";
			$in = 0;
			$insert = 1;
		}
		if ($in >= 2) {
			print ", ";
		}
		if ($_ =~ m|create table (.+)|) {
			$table = $1;
			print "drop table $format.$1;\n";
			print "create table $format.$1\n";
		} else {
			print;
		}
		if ($in > 0) {
			$in++;
		}
		if ($_ =~ m|\(|) {
			$in = 1;
		}
		if ($insert == 1) {
			print "insert into table $format.$table select * from text.$table;\n";
			$insert = 0;
		}
	}
}

sub convert_postgres {
	while (<STREAM>) {
		$_ =~ s|^--.+||;
		next if ($_ =~ m|^\s*$|);
		if ($_ =~ m|create table (.+)|) {
			print "drop table $1;\n";
			print "create table $1\n";
		} else {
			print;
		}
	}
}


