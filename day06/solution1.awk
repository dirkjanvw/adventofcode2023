#!/usr/bin/awk -f
# Time:      7  15   30
# Distance:  9  40  200

BEGIN {
    OFS = "\t";
}

/^Time/ {
    for (i = 2; i <= NF; i++) {
        time[i] = $i;
    }
}

/^Distance/ {
    for (i = 2; i <= NF; i++) {
        distance[i] = $i;
    }
}

END {
    total = 1;

    for (i in time) {
        t = time[i];
        d = distance[i];
        l = .5 * t - sqrt(.25 * t^2 - d);
        h = .5 * t + sqrt(.25 * t^2 - d);

        l = int(l);
        if (int(h) != h) {
            h = int(h);
        } else {
            h--;
        }

        total *= (h - l);
    }

    print total;
}
