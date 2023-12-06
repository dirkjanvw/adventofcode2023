#!/usr/bin/awk -f

BEGIN {
    FS = "[:|]";
}

{
    c = 0;

    split($2, a, " ");
    split($3, b, " ");

    for (i in a) {
        for (j in b) {
            if (a[i] == b[j]) {
                c++;
            }
        }
    }

    if (c == 0) {
        next;
    }

    total += 2^(c-1);
}

END {
    print total;
}
