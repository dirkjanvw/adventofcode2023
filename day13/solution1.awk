#!/usr/bin/awk -f
# #.##..##.
# ..#.##.#.
# ##......#
# ##......#
# ..#.##.#.
# ..##..##.
# #.#.##.#.
# 
# #...##..#
# #....#..#
# ..##..###
# #####.##.
# #####.##.
# ..##..###
# #....#..#

function min(a, b) {
    return a < b ? a : b;
}

function max(a, b) {
    return a > b ? a : b;
}

BEGIN {
    RS = "";
    FS = "\n";
}

{
    delete rev;  # stores the transposed record

    for (i = 1; i <= NF; i++) {  # i is the row number
        c = 0;
        for (r = i+1; r <= min(NF, i*2); r++) {
            #print (i-c), $(i-c), r, $r;
            if ($(i-c) != $r) {
                break;
            }
            if ((i-c) == 1 || r == NF) {
                #print i;
                sum += 100*i;
            }
            c++;
        }

        for (j = 1; j <= length($1); j++) {
            rev[j] = rev[j]""substr($i, j, 1);
        }
    }

    for (j = 1; j <= length($1); j++) {  # j is the col number
        #print j, rev[j];
        c = 0;
        for (r = j+1; r <= min(length($1), j*2); r++) {
            #print (j-c), rev[j-c], r, rev[r];
            if (rev[j-c] != rev[r]) {
                break;
            }
            if ((j-c) == 1 || r == length($1)) {
                #print j;
                sum += j;
            }
            c++;
        }
    }
}

END {
    print sum;
}
