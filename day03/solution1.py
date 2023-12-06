#!/usr/bin/env python3

from sys import argv
import re


def read_file(filename):
    re_symbol = re.compile(r"[^0-9\.]")
    re_number = re.compile(r"[0-9]+")

    symbol_list = []  # list of tuples with (row, column)
    number_list = []  # list of tuples with (row, start, end, number)
    line_number = 0

    with open(filename, 'r') as f:
        for line in f:
            line = line.strip()
            if not line:
                continue

            for match in re_symbol.finditer(line):
                symbol_list.append((line_number,
                                    match.start()))

            for match in re_number.finditer(line):
                number_list.append((line_number,
                                    match.start(),
                                    match.end(),
                                    int(match.group())))

            line_number += 1

    return symbol_list, number_list


def create_positions(t):
    row = t[0]
    start = t[1]
    end = t[2]
    number = t[3]

    positions = []
    for i in [row - 1, row, row + 1]:
        for j in range(start - 1, end + 1):
            positions.append((i, j))

    return positions


def solve(symbol_list, number_list):
    total = 0

    for t in number_list:
        for position in create_positions(t):
            if position in symbol_list:
                total += t[3]
                break

    return total


def main():
    if len(argv) != 2:
        print("Usage: python3 solution1.py <filename>")
        return

    # Get a symbol list and number list from the file
    symbol_list, number_list = read_file(argv[1])

    # Get the solution
    solution = solve(symbol_list, number_list)

    # Print the solution
    print(solution)


if __name__ == "__main__":
    main()
