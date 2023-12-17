#!/usr/bin/env python3

from sys import argv

def parse_input(filename):
    steps = []
    with open(filename, 'r') as f:
        for line in f:
            line = line.strip()
            linelist = line.split(',')
            for l in linelist:
                steps.append(l)
    return steps

def calculate_hash(step):
    total = 0
    for s in step:
        total += ord(s)
        total *= 17
        total %= 256
    return total

def main():
    if len(argv) != 2:
        print("Usage: ./solution1.py <input>")
        exit(1)

    steps = parse_input(argv[1])

    total = 0
    for step in steps:
        total += calculate_hash(step)

    print(total)

if __name__ == "__main__":
    main()
