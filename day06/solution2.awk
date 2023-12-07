#!/usr/bin/awk -f
# Time:      7  15   30
# Distance:  9  40  200

BEGIN {
    FS = ":";
    OFS = "\t";
}

{
    gsub(" ", "", $0);
}

/^Time/ {
    t = $2;
    next;
}

/^Distance/ {
    d = $2;
    next;
}

END {
    l = .5 * t - sqrt(.25 * t^2 - d);
    h = .5 * t + sqrt(.25 * t^2 - d);

    l = int(l);
    if (int(h) != h) {
        h = int(h);
    } else {
        h--;
    }

    print h - l;
}
