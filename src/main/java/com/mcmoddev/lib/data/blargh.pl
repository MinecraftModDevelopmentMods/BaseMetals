#!/usr/bin/perl -w

while(<>) {
  if( /(\s+public static final String)\s+(\w+)\s+(=.*;)/ ) {
	print $1;
	my $ot = $2;
	$ot =~ tr/[a-z]/[A-Z]/;
	print " $ot ";
	print $3;
	print "\n";
  } else {
	print;
  }
}

