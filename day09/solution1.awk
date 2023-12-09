#!/usr/bin/awk -f
# 0 3 6 9 12 15
# 1 3 6 10 15 21
# 10 13 16 21 30 45

function sumvalue(arr, len, val) {
    for (i = 1; i <= len; i++) {
        if (arr[i] != 0)
            return 0;
    }
    return 1;
}

function getprediction(arr, len) {
    if (len == 1) {
        return arr[len];
    }

    if (sumvalue(arr, len, 0)) {
        return 0;
    }

    for (i = 1; i < len; i++) {
        b[i] = arr[i+1] - arr[i];
    }

    return getprediction(b, len - 1) + arr[len];
}

{
    n=split($0, a, " ");
    pred = getprediction(a, n);
    total += pred;
}

END {
    print total;
}
