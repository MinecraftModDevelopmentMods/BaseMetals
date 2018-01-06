#!/usr/bin/perl -w
use strict;
use warnings;
use English qw/-no_match_vars/;

my $DATA;
my $OUTPUT;
my $IN;
my $TEMPLATE;
my $line;
my $FILE;

open $DATA, '<', 'datafile.txt'
    or die qq{Unable to open file of model names! $ERRNO};

for $IN (<$DATA>) {
    chomp $IN;
    print "Working on material $IN\n";
    my $tt;
    open $FILE, '<', 'itemplates.txt' or die qq{Unable to open list of templates: $ERRNO};
    for $tt (<$FILE>) {
	chomp $tt;
	print "Working on template $tt\n";
	open $TEMPLATE, "<", $tt or die qq{Unable to open template: $ERRNO};
	my @output;
	for $line (<$TEMPLATE>) {
	    chomp $line;
	    $line =~ s/__MATERIAL__/$IN/;
	    push @output, $line;
	}
	close $TEMPLATE;
    
	my $temp = join "\n", @output;
	my $outname = $tt;
	$outname =~ s/template/$IN/;
	open $OUTPUT, '>', "output/$outname"
	or die qq{Unable to open output file: $ERRNO};
	print $OUTPUT $temp;
	close $OUTPUT;
    }
    close $FILE;
}
close $DATA;
