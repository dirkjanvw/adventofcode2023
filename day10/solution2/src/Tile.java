public class Tile {
    final private char type;
    final private boolean[] orientation; // 0 = north, 1 = east, 2 = south, 3 = west

    public Tile(char type) {
        this.type = type;
        this.orientation = getOrientation(type);
    }

    private boolean[] getOrientation(char type) {
        switch (type) {
            case 'F':
                return new boolean[]{false, true, true, false};
            case 'L':
                return new boolean[]{true, true, false, false};
            case '7':
                return new boolean[]{false, false, true, true};
            case 'J':
                return new boolean[]{true, false, false, true};
            case '-':
                return new boolean[]{false, true, false, true};
            case '|':
                return new boolean[]{true, false, true, false};
            case 'S':
                return new boolean[]{true, true, true, true};
            default:
                return new boolean[]{false, false, false, false};
        }
    }

    /**
     * Returns the end of the tile that is connected to the given end.
     * @param end One end of a tile (0 = north, 1 = east, 2 = south, 3 = west)
     * @return The other end of the tile (0 = north, 1 = east, 2 = south, 3 = west)
     */
    public int getOtherEnd(int end) {
        // check that tile has two ends
        int ends = 0;
        for (boolean b : this.orientation) {
            if (b) {
                ends++;
            }
        }
        if (ends != 2) {
            return -1;
        }

        // check if end is valid
        if (end < 0 || end > 3) {
            throw new RuntimeException("Invalid end");
        }

        // check if end is connected at all
        if (!this.orientation[end]) {
            return -1;
        }

        // check if end is connected to anything
        for (int i = 0; i < 4; i++) {
            if (i != end && this.orientation[i]) {
                return i;
            }
        }

        // to satisfy compiler
        throw new RuntimeException("No other end");
    }

    public boolean[] getOrientation() {
        return orientation;
    }

    public char getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return String.valueOf(this.type);
    }
}
