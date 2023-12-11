import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class TilePattern {
    private Tile[][] pattern;

    public TilePattern(Path filename) {
        // read the tile pattern from input file
        read(filename);
    }

    private void read(Path filename) {
        try (BufferedReader reader = Files.newBufferedReader(filename)) {
            for (int lineCount = 0; reader.ready(); lineCount++) {
                final char[] linePattern = reader.readLine().toCharArray();
                if (lineCount == 0) {
                    this.pattern = new Tile[linePattern.length][linePattern.length];
                }
                for (int i = 0; i < linePattern.length; i++) {
                    this.pattern[lineCount][i] = new Tile(linePattern[i]);
                }
            }
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Tile getTile(int x, int y) {
        return this.pattern[y][x];
    }

    private int[] findUniquePosition(char start) {
        int[] startCoords = new int[2];
        int occurences = 0;

        for (int y = 0; y < this.pattern.length; y++) {
            for (int x = 0; x < this.pattern[y].length; x++) {
                if (this.pattern[y][x].getType() == start) {
                    occurences++;
                    startCoords[0] = x;
                    startCoords[1] = y;
                }
            }
        }

        if (occurences != 1) {
            throw new RuntimeException("Start position not unique");
        }

        return startCoords;
    }

    private int[] getNext(int x, int y) {
        // check tile above
        if (y > 0 && getTile(x, y - 1).getOrientation()[2]) {
            return new int[]{x, y - 1, 0};
        }

        // check tile to the right
        if (x < this.pattern[y].length - 1 && getTile(x + 1, y).getOrientation()[3]) {
            return new int[]{x + 1, y, 1};
        }

        // check tile below
        if (y < this.pattern.length - 1 && getTile(x, y + 1).getOrientation()[0]) {
            return new int[]{x, y + 1, 2};
        }

        // check tile to the left
        if (x > 0 && getTile(x - 1, y).getOrientation()[1]) {
            return new int[]{x - 1, y, 3};
        }

        // to satisfy compiler
        throw new RuntimeException("No next tile found");
    }

    /**
     * Returns the next tile in the loop.
     * @param x The x-coordinate of the current tile.
     * @param y The y-coordinate of the current tile.
     * @param d The outgoing direction of the previous tile (0 = north, 1 = east, 2 = south, 3 = west).
     * @return The next tile in the loop (x, y, d).
     */
    private int[] getNext(int x, int y, int d) {
        // check outgoing direction current tile
        final Tile currentTile = getTile(x, y);
        final int incomingDirection = opposeEnd(d);
        final int outgoingDirection = currentTile.getOtherEnd(incomingDirection);
//        System.out.printf("\t%s -(%s,%s)-> %s  [%s]%n",
//                incomingDirection,
//                x,
//                y,
//                outgoingDirection,
//                currentTile.getType());

        switch (outgoingDirection) {
            case 0:
                return new int[]{x, y - 1, outgoingDirection};
            case 1:
                return new int[]{x + 1, y, outgoingDirection};
            case 2:
                return new int[]{x, y + 1, outgoingDirection};
            case 3:
                return new int[]{x - 1, y, outgoingDirection};
            default:
                throw new RuntimeException("Invalid outgoing direction");
        }
    }

    /**
     * Returns the opposite end of the given end. So: 0 -> 2, 1 -> 3, 2 -> 0, 3 -> 1.
     * @param end One end of a tile (0 = north, 1 = east, 2 = south, 3 = west).
     * @return The opposite end of the given end (0 = north, 1 = east, 2 = south, 3 = west).
     */
    private int opposeEnd(int end) {
        switch (end) {
            case 0:
                return 2;
            case 1:
                return 3;
            case 2:
                return 0;
            case 3:
                return 1;
            default:
                throw new RuntimeException("Invalid end");
        }
    }

    public int findLoopLength(char start) {
        int loopLength = 0;

        final int[] startCoords = findUniquePosition(start);

        int[] currentCoords = getNext(startCoords[0], startCoords[1]);
//        System.out.println("Current position: " + currentCoords[0] + ", " + currentCoords[1] + ", " + currentCoords[2]);

        // loop until we reach the start position again
        while (getTile(currentCoords[0], currentCoords[1]).getType() != start) {
            loopLength++;
            currentCoords = getNext(currentCoords[0], currentCoords[1], currentCoords[2]);
//            System.out.println("Position (" + loopLength + "): " + currentCoords[0] + ", " + currentCoords[1] + ", " + currentCoords[2]);
        }

        return loopLength;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Tile[] row : this.pattern) {
            for (Tile tile : row) {
                sb.append(tile);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
