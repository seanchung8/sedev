#!/usr/bin/perl

$hash1 = tokenCount($ARGV[0]);
$hash2 = tokenCount($ARGV[1]);
foreach (keys(%$hash1)) {
	$tokenDifference += abs ($hash1->{$_} - $hash2->{$_});
}
print $tokenDifference, "\n";

sub tokenCount {
	my $file = shift;
	my $token;
	my %counts;

	$/ = undef;
	open(TEMP, $file);
	my $text = <TEMP>;
	$text =~ tr/A-Z/a-z/;
	@tokens = split(/\s+/, $text);
	foreach $token (@tokens) {
		$token =~ tr|0-9a-z_\.\-||cd;
		next if !$token;
		$counts{$token}++;
	}
	return \%counts;
}
