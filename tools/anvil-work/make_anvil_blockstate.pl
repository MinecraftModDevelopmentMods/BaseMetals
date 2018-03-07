#!/usr/bin/perl -w
use strict;
use warnings;
use English qw/-no_match_vars/;

my $DATA;
my $OUTPUT;
my $IN;
my $TEMPLATE;
my $line;

open $DATA, '<', 'datafile.txt'
    or die qq{Unable to open file of model names! $ERRNO};

for $IN (<$DATA>) {
    chomp $IN;
    open $TEMPLATE, "<", "template_anvil_blockstate.json" or die qq{Unable to open template: $ERRNO};
    my @output;
    for $line (<$TEMPLATE>) {
	chomp $line;
	$line =~ s/__MATERIAL__/$IN/;
	push @output, $line;
    }
    close $TEMPLATE;
    
    my $temp = join "\n", @output;
    my $outname = "output/" . $IN . '_anvil.json';
    open $OUTPUT, '>', qq{$outname}
      or die qq{Unable to open output file: $ERRNO};
    print $OUTPUT $temp;
    close $OUTPUT;
}

close $DATA;
