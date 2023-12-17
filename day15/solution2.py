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


def parse_step(step):
    if step.endswith('-'):
        return (step[:-1], -1)
    else:
        parts = step.split('=')
        return (parts[0], int(parts[1]))


def calculate_hash(step):
    total = 0
    for s in step:
        total += ord(s)
        total *= 17
        total %= 256
    return total


def perform_steps(steps):
    boxes = {i: [] for i in range(256)}
    box_nr = 0
    for step in steps:
        (box, number) = parse_step(step)
        box_nr = calculate_hash(box)
        print(f"({box}, {number}): {box_nr}")
        lenses = boxes[box_nr]
        if number == -1:
            if len(lenses) > 0:
                for lens in lenses:
                    if lens[0] == box:
                        lenses.remove(lens)
                        print(f"\tRemoved lens {lens} from box {box_nr}")
                        break
        else:
            if len(lenses) == 0:
                lenses.append((box, number))
                print(f"\tAdded lens {box}={number} to box {box_nr}")
            else:
                replaced_lens = False
                for i in range(len(lenses)):
                    if lenses[i][0] == box:
                        lenses[i] = (box, number)
                        replaced_lens = True
                        print(f"\tUpdated lens {box}={number} in box {box_nr}")
                        break
                if not replaced_lens:
                    lenses.append((box, number))
                    print(f"\tAdded lens {box}={number} to box {box_nr}")

        print_boxes(boxes)
        print()

    return boxes


def calculate_power(boxes):
    total_power = 0

    for box_nr, lenses in boxes.items():
        if len(lenses) > 0:
            for i in range(len(lenses)):
                total_power += (box_nr + 1) * (i + 1) * lenses[i][1]

    return total_power


def print_boxes(boxes):
    output = "{"
    count = 0
    for box_nr, lenses in boxes.items():
        if len(lenses) > 0:
            count += 1
            output += str(box_nr) + ": " + str(lenses) + ", "
    if count > 0:
        output = output[:-2] + "}"
    else:
        output += "}"
    print(output)


def main():
    if len(argv) != 2:
        print("Usage: ./solution1.py <input>")
        exit(1)

    steps = parse_input(argv[1])
    boxes = perform_steps(steps)
    print(calculate_power(boxes))


if __name__ == "__main__":
    main()
