#!/usr/bin/perl -w
use strict;
use warnings;
use English qw/-no_match_vars/;
use File::Slurp;

my $basedata = read_file('template.json');
my $DATA;
my $OUTPUT;

open $DATA, '<', 'datafile.txt'
  or die qq{Unable to open file of model names! $ERRNO};
for (<$DATA>) {
    chomp;
    my $temp = $basedata;
    $temp =~ s/__VALUE__/$_/;
    open $OUTPUT, '>', qq{$_.json}
      or die qq{Unable to open output file: $ERRNO};
    print $OUTPUT $temp;
    close $OUTPUT;
}

close $DATA;
