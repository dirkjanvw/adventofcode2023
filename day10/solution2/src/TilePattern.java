import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TilePattern {
    private Tile[][] pattern;

    public TilePattern(Path filename) {
        // read the tile pattern from input file
        read(filename);
    }

    public TilePattern(Tile[][] pattern) {
        this.pattern = pattern;
    }

    private void read(Path filename) {
        // read tile pattern from file
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

        // remove rows with null at the end
        int nullRows = 0;
        for (int i = this.pattern.length - 1; i >= 0; i--) {
            if (this.pattern[i][this.pattern.length - 1] == null) {
                nullRows++;
            } else {
                break;
            }
        }
        Tile[][] newPattern = new Tile[this.pattern.length - nullRows][this.pattern.length];
        System.arraycopy(this.pattern, 0, newPattern, 0, newPattern.length);
        this.pattern = newPattern;
    }

    private Tile getTile(int x, int y) {
        return this.pattern[y][x];
    }

    public int[] getSize() {
        return new int[]{this.pattern.length, this.pattern[0].length};
    }

    private int[] findUniquePosition(char start) {
        int[] startCoords = new int[2];
        int occurrences = 0;

        for (int y = 0; y < this.pattern.length; y++) {
            for (int x = 0; x < this.pattern[y].length; x++) {
                if (this.pattern[y][x].getType() == start) {
                    occurrences++;
                    startCoords[0] = x;
                    startCoords[1] = y;
                }
            }
        }

        if (occurrences != 1) {
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

    /**
     * Returns the coordinates of all tiles in the loop.
     * @param start The type of the tile to start the loop from.
     * @return The coordinates of all tiles in the loop (x, y, d; where x is the
     *        x-coordinate, y is the y-coordinate and d are the directions on
     *        the right side of the loop (0 = north, 1 = east, 2 = south, 3 = west)).
     */
    public List<Integer[]> findLoop(char start) {
        List<Integer[]> loopCoords = new ArrayList<>();

        final int[] startCoords = findUniquePosition(start);
        loopCoords.add(new Integer[]{startCoords[0], startCoords[1]});

        // loop until we reach the start position again
        int[] currentCoords = getNext(startCoords[0], startCoords[1]);
        while (getTile(currentCoords[0], currentCoords[1]).getType() != start) {
            loopCoords.add(new Integer[]{currentCoords[0], currentCoords[1]});
            currentCoords = getNext(currentCoords[0], currentCoords[1], currentCoords[2]);
        }

        return loopCoords;
    }

    public List<Integer[]> findNotInLoop(List<Integer[]> loopCoords) {
        List<Integer[]> notInLoop = new ArrayList<>();

        for (int y = 0; y < this.pattern.length; y++) {
            for (int x = 0; x < this.pattern[y].length; x++) {
                boolean inLoop = false;
                for (Integer[] loopCoord : loopCoords) {
                    if (loopCoord[0] == x && loopCoord[1] == y) {
                        inLoop = true;
                        break;
                    }
                }
                if (!inLoop) {
                    notInLoop.add(new Integer[]{x, y});
                }
            }
        }

        return notInLoop;
    }

    public TilePattern mask(List<Integer[]> notInLoop, char mask) {
        TilePattern maskedTilePattern = new TilePattern(this.pattern);
        for (Integer[] coord : notInLoop) {
            maskedTilePattern.pattern[coord[1]][coord[0]] = new Tile(mask);
        }
        return maskedTilePattern;
    }

    /**
     * Returns the coordinates of all tiles that are enclosed by the loop.
     * @param loopCoords The coordinates of the tiles in the loop.
     * @return The coordinates of all tiles that are enclosed by the loop.
     */
    public List<Integer[]> findEnclosed(List<Integer[]> loopCoords) {
        List<Integer[]> enclosedCoords = new ArrayList<>();

        // go over all tiles in the pattern and check if they are enclosed
        for (int y = 0; y < this.pattern.length; y++) {
            for (int x = 0; x < this.pattern[y].length; x++) {
                if (isPointWithinLoop(loopCoords, x, y)) {
                    enclosedCoords.add(new Integer[]{x, y});
                }
            }
        }

        return enclosedCoords;
    }

    /**
     * Returns whether the given point is within the loop using the even-odd rule.
     * @param loopCoords The coordinates of the tiles in the loop.
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     * @return Whether the given point is within the loop.
     *
     * Python code stolen from wikipedia:
     * num = len(poly)
     * j = num - 1
     * c = False
     * for i in range(num):
     *   if (x == poly[i][0]) and (y == poly[i][1]):
     *   # point is a corner
     *     return True
     *   if (poly[i][1] > y) != (poly[j][1] > y):
     *     slope = (x - poly[i][0]) * (poly[j][1] - poly[i][1]) - (
     *       poly[j][0] - poly[i][0]
     *     ) * (y - poly[i][1])
     *     if slope == 0:
     *     # point is on boundary
     *       return True
     *     if (slope < 0) != (poly[j][1] < poly[i][1]):
     *       c = not c
     *   j = i
     * return c
     */
    private boolean isPointWithinLoop(List<Integer[]> loopCoords, int x, int y) {
        for (Integer[] loopCoord : loopCoords) {
            if (loopCoord[0] == x && loopCoord[1] == y) {
                // point is part of loop
                return false;
            }
        }

        boolean c = false;
        int j = loopCoords.size() - 1;
        for (int i = 0; i < loopCoords.size(); i++) {
            if (x == loopCoords.get(i)[0] && y == loopCoords.get(i)[1]) {
                // point is a corner
                return true;
            }
            if ((loopCoords.get(i)[1] > y) != (loopCoords.get(j)[1] > y)) {
                int slope = (x - loopCoords.get(i)[0]) * (loopCoords.get(j)[1] - loopCoords.get(i)[1]) - (loopCoords.get(j)[0] - loopCoords.get(i)[0]) * (y - loopCoords.get(i)[1]);
                if (slope == 0) {
                    // point is on boundary
                    return true;
                }
                if ((slope < 0) != (loopCoords.get(j)[1] < loopCoords.get(i)[1])) {
                    c = !c;
                }
            }
            j = i;
        }
        return c;
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
