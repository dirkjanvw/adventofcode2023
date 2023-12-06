#!/usr/bin/awk -f

BEGIN {
    FS = "[:|]";
}

{
    all[FNR]++;
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

    for (i=FNR+1; i<=FNR+c; i++) {
        all[i] += all[FNR];
    }
}

END {
    for (i in all) {
        total += all[i];
    }
    print total;
}
