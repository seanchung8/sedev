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

print "query nfact factsize facts ndim dimsize dims qsize nfrom nwhere nskwhere ngroup norder ncte nsum navg ncase inter_size final_size\n";

@files = glob("query*.tpl");
foreach (@files) {
	open(TEMP, $_);
	$_ =~ m|(\S+?).tpl|;
	print "$1 ";
	$total = 0;
	$text = "";
	while ($_ = <TEMP>) {
		chomp;
		next if ($_ =~ m|^--|);
		$text .= " " . $_;
	}
	#print $text, "\n";

	# Identify which of the fact tables are under consideration.
	@facts = ();
	@dimensions = ();
	$factSize = $dimSize = 0;
	foreach $table (keys %tables) {
		if ($text =~ m/$table(\s|,)/) {
			$total += $tables{$table};
			if ($table eq "catalog_sales" || $table eq "store_sales" || $table eq "web_sales") {
				push(@facts, $table);
				$factSize += $tables{$table};
			} else {
				push(@dimensions, $table);
				$dimSize += $tables{$table};
			}
		}
	}
	$f = "None";
	if ($#facts != -1) {
		$f = join(",", @facts);
	}
	print $#facts+1, " ";
	print $factSize, " ";
	print $f, " ";

	$f = "None";
	if ($#dimensions != -1) {
		$f = join(",", @dimensions);
	}
	print $#dimensions+1, " ";
	print $dimSize, " ";
	print $f, " ";

	# The size of the query (proxy for complexity).
	print sprintf("%0.2f", length($text)/1024.0), " ";

	# The number of from clauses
	@matches = $text =~ m|(from)|gs;
	print $#matches+1, " ";

	# The number of comparisons clauses
	@matches = $text =~ m/(\bwhere\b|\band\b|\bor\b)/gs;
	print $#matches+1, " ";

	# Subtract out half the total count of _sk items
	@sks = $text =~ m/(_sk)/gs;
	print $#matches+1 - (($#sks+1)/ 2), " ";

	# Number of group bys
	@matches = $text =~ m/(group\s+by)/gs;
	print $#matches+1, " ";

	# Number of order bys
	@matches = $text =~ m/(order\s+by)/gs;
	print $#matches+1, " ";

	# Number of CTEs
	@matches = $text =~ m/(as\s*\()/gs;
	print $#matches+1, " ";

	# Number of sums
	@matches = $text =~ m/(sum\s*\()/gs;
	print $#matches+1, " ";

	# Number of averages
	@matches = $text =~ m/(avg\s*\()/gs;
	print $#matches+1, " ";

	# Number of case
	@matches = $text =~ m/(case\s+when)/gs;
	print $#matches+1, " ";

	print "0 0\n";
}
