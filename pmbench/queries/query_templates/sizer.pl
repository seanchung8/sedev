#!/usr/bin/perl

%tables = (
call_center => '0.0',
catalog_page => '1.56',
catalog_returns => '20.39',
catalog_sales => '282.2',
customer_address => '5.25',
customer => '12.6',
customer_demographics => '76.92',
date_dim => '9.84',
dbgen_version => '0',
household_demographics => '0.14',
income_band => '0',
inventory => '225.47',
item => '4.82',
promotion => '0.04',
reason => '0',
ship_mode => '0',
store => '0',
store_returns => '31.19',
store_sales => '370.45',
time_dim => '4.87',
warehouse => '0',
web_page => '0.01',
web_returns => '9.35',
web_sales => '140.07',
web_site => '0.01',
);

$/ = undef;
@files = glob("query*.tpl");
foreach (@files) {
	open(TEMP, $_);
	$_ =~ m|(\S+?).tpl|;
	print "$1 ";
	$total = 0;
	$text = <TEMP>;
	@facts = ();
	foreach $table (keys %tables) {
		if ($text =~ m|$table|) {
			$total += $tables{$table};
			if ($table eq "catalog_sales" || $table eq "store_sales" || $table eq "web_sales") {
				push(@facts, $table);
			}
		}
	}
	$f = "None";
	if ($#facts != -1) {
		$f = join(",", @facts);
	}
	print "$f $total ", sprintf("%0.2f", length($text)/1024.0), "\n";
}
