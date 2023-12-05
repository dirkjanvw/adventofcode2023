#!/usr/bin/awk -f

BEGIN {
    FS = OFS = "";
    sum = 0;
}

{
    gsub(/^[a-zA-Z]*/, "", $0);
    gsub(/[a-zA-Z]*$/, "", $0);
    number = substr($0, 0, 1)""substr($0, length($0), 1);
    sum += number;
}

END {
    print sum;
}
