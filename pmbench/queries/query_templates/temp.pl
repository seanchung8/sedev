#!/usr/bin/perl

@files = glob("*.tpl");
foreach (@files) {
	open(TEMP, $_);
	while ($line = <TEMP>) {
		next if $line =~ m/^--/;
		$line =~ tr/A-Z/a-z/;
		$line =~ s/\(/ /g;
		$line =~ s/\)/ /g;
		$line =~ s/,/ /g;
		$line =~ s/;/ /g;
		@words = split(/\s+/, $line);
		foreach $word (@words) {
			$seen{$word}++;
		}
	}
}

@seen = keys(%seen);
@seen = sort { $seen{$b} <=> $seen{$a} } @seen;
foreach (@seen) {
	print $_, "\t\t", $seen{$_}, "\n";
}
