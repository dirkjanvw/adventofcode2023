#!/usr/bin/env python3
# two1nine
# eightwothree
# abcone2threexyz
# xtwone3four
# 4nineeightseven2
# zoneight234
# 7pqrstsixteen

import re
import sys

def to_integer_combination(number1, number2):
    number1 = re.sub(r'zero', '0', number1)
    number1 = re.sub(r'one', '1', number1)
    number1 = re.sub(r'two', '2', number1)
    number1 = re.sub(r'three', '3', number1)
    number1 = re.sub(r'four', '4', number1)
    number1 = re.sub(r'five', '5', number1)
    number1 = re.sub(r'six', '6', number1)
    number1 = re.sub(r'seven', '7', number1)
    number1 = re.sub(r'eight', '8', number1)
    number1 = re.sub(r'nine', '9', number1)
    number2 = re.sub(r'zero', '0', number2)
    number2 = re.sub(r'one', '1', number2)
    number2 = re.sub(r'two', '2', number2)
    number2 = re.sub(r'three', '3', number2)
    number2 = re.sub(r'four', '4', number2)
    number2 = re.sub(r'five', '5', number2)
    number2 = re.sub(r'six', '6', number2)
    number2 = re.sub(r'seven', '7', number2)
    number2 = re.sub(r'eight', '8', number2)
    number2 = re.sub(r'nine', '9', number2)

    combination = int(f"{number1}{number2}")

    print(f"\t\t{number1} + {number2} = {combination}")

    return combination


def main():
    re_zero = re.compile(r'(0|zero)')
    re_one = re.compile(r'(1|one)')
    re_two = re.compile(r'(2|two)')
    re_three = re.compile(r'(3|three)')
    re_four = re.compile(r'(4|four)')
    re_five = re.compile(r'(5|five)')
    re_six = re.compile(r'(6|six)')
    re_seven = re.compile(r'(7|seven)')
    re_eight = re.compile(r'(8|eight)')
    re_nine = re.compile(r'(9|nine)')
    all_re = (re_zero, re_one, re_two, re_three, re_four, re_five, re_six, re_seven, re_eight, re_nine)

    total = 0

    with open(sys.argv[1]) as f:
        for line in f:
            line = line.strip()
            if not line:
                continue

            first_match = ""
            to_first_match = len(line)
            last_match = ""
            from_last_match = 0

            print(line)

            for re_number in all_re:
                for match in re_number.finditer(line):
                    if not match:
                        continue
                    print(f"\t{match}")

                    if match.start() < to_first_match:
                        first_match = match.group()
                        to_first_match = match.start()
                    if match.end() > from_last_match:
                        last_match = match.group()
                        from_last_match = match.end()

            if first_match and last_match:
                total += to_integer_combination(first_match, last_match)

    print(total)

if __name__ == "__main__":
    main()
