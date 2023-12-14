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


def find_first_mirror_line(pattern):
    for i in range(1, len(pattern)):
        c = 0
        for r in range(i+1, min(len(pattern), i*2)+1):
            if pattern[i-c-1] != pattern[r-1]:
                break
            if i-c-1 == 0 or r == len(pattern):
                return i
            c += 1
    return 0


def find_mirror_line(pattern):
    total = 0

    total += 100*find_first_mirror_line(pattern)
    total += find_first_mirror_line(transpose(pattern))

    ## Check if the pattern is symmetric horizontally
    #for i in range(1, len(pattern)):
    #    c = 0
    #    for r in range(i+1, min(len(pattern), i*2)+1):
    #        if pattern[i-c-1] != pattern[r-1]:
    #            break
    #        if i-c-1 == 0 or r == len(pattern):
    #            total += 100*i
    #            hlines.append(i)
    #        c += 1

    ## Check if the pattern is symmetric vertically
    #tpattern = transpose(pattern)
    #for i in range(1, len(tpattern)):
    #    c = 0
    #    for r in range(i+1, min(len(tpattern), i*2)+1):
    #        if tpattern[i-c-1] != tpattern[r-1]:
    #            break
    #        if i-c-1 == 0 or r == len(tpattern):
    #            total += i
    #        c += 1

    return total


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
