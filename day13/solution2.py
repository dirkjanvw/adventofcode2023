#!/usr/bin/env python3
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

import sys


def parse_patterns(filename):
    with open(filename, 'r') as f:
        pattern = []
        for line in f:
            line = line.strip()
            if not line:
                yield pattern
                pattern = []
                continue
            pattern.append([*line])

        if pattern:
            yield pattern


def transpose(pattern):
    return list(map(list, zip(*pattern)))


def find_mirror_line(pattern):
    total = 0

    hline = find_first_mirror_line(pattern)
    print(f"\tHorizontal line: {hline}")
    for position in get_positions(pattern):
        new_pattern = get_pattern(pattern, position)
        new_hline = find_first_mirror_line(new_pattern, hline)
        if new_hline != 0:
            print(f"\t\tFound new mirror line: {new_hline} at {position}")
            total += 100*new_hline
            break

    tpattern = transpose(pattern)
    vline = find_first_mirror_line(tpattern)
    print(f"\tVertical line: {vline}")
    for position in get_positions(tpattern):
        new_pattern = get_pattern(tpattern, position)
        new_vline = find_first_mirror_line(new_pattern, vline)
        if new_vline != 0:
            print(f"\t\tFound new mirror line: {new_vline} at {position}")
            total += 1*new_vline
            break

    return total


def find_first_mirror_line(pattern, ignore=0):
    for i in range(1, len(pattern)):
        if i == ignore:
            continue
        c = 0
        for r in range(i+1, min(len(pattern), i*2)+1):
            #print(f"{i-c-1}, {r-1}, {pattern[i-c-1]}, {pattern[r-1]}")
            if pattern[i-c-1] != pattern[r-1]:
                break
            if i-c-1 == 0 or r == len(pattern):
                return i
            c += 1
    return 0


def get_positions(pattern):
    for i in range(len(pattern)):
        for j in range(len(pattern[i])):
            yield (i, j)


def get_pattern(pattern, position):
    i, j = position
    old_char = pattern[i][j]
    if old_char == '#':
        new_char = '.'
    else:
        new_char = '#'

    new_pattern = []
    for row in pattern:
        new_pattern.append(row.copy())

    new_pattern[i][j] = new_char

    return new_pattern


def print_pattern(pattern):
    for row in pattern:
        print("".join(row))
    print()


def main():
    # Read input filename
    if len(sys.argv) != 2:
        print("Usage: python3 solution2.py <input_filename>")
        return
    filename = sys.argv[1]

    # Read input file
    list_of_patterns = parse_patterns(filename)

    # For each pattern, find the (horizontal or vertical) line that can be used
    # as mirror to get the same pattern
    pc = 0
    total = 0;
    for pattern in list_of_patterns:
        print(f"Checking {pc}")
        total += find_mirror_line(pattern)
        pc += 1

    # Print the total score
    print(total)


if __name__ == "__main__":
    main()
