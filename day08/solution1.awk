#!/usr/bin/awk -f
# LLR
# 
# AAA = (BBB, BBB)
# BBB = (AAA, ZZZ)
# ZZZ = (ZZZ, ZZZ)

FNR == 1 {
    n=split($0, sequence, "");
}

FNR > 2 {
    gsub(/[\(,]/, "", $3);
    gsub(/\)/, "", $4);
    dagl[$1] = $3;
    dagr[$1] = $4;
}

END {
    step = "AAA";
    while (1) {
        total ++;

        if (sequence[total%n] == "L") {
            step = dagl[step];
        } else {
            step = dagr[step];
        }

        if (step == "ZZZ") {
            print total;
            exit;
        }
    }
}
