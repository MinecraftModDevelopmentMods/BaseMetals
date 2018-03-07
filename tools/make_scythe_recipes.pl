#!/usr/bin/perl -w
use strict;
use warnings;
use English qw/-no_match_vars/;
use File::Slurp;

my $basedata = read_file('scythe_recipe_template.json');
my $DATA;
my $OUTPUT;

open $DATA, '<', 'datafile.rec.txt'
  or die qq{Unable to open file of model names! $ERRNO};
for (<$DATA>) {
    chomp;
    my @vals = split /!!/;
    my $itemName = $vals[0];
    my $ingotName = $vals[1];
    my $matName = $vals[2];
    my $fileName = lc $matName . '_scythe.json';
    my $temp = $basedata;
    $temp =~ s/__ITEM_NAME__/$itemName/;
    $temp =~ s/__INGREDIENT_NAME__/$ingotName/;
    $temp =~ s/__MATERIAL_NAME__/$matName/;
    open $OUTPUT, '>', qq{$fileName}
      or die qq{Unable to open output file: $ERRNO};
    print $OUTPUT $temp;
    close $OUTPUT;
}

close $DATA;
