import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class TilePattern {
    final private char[][] pattern;
    final private char[][] energizedPattern;
    final Map<Integer, Set<Integer>> seenTiles = new HashMap<>(); // key: coordinates as single integer, value: set of directions

    public TilePattern(char[][] pattern) {
        this.pattern = pattern;
        this.energizedPattern = new char[pattern.length][pattern[0].length];
        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern[0].length; j++) {
                this.energizedPattern[i][j] = '.';
            }
        }
    }

    public static TilePattern parseTilePatternFromFile(Path path) throws IOException {
        // Read the file
        List<String> lines = Files.readAllLines(path);

        // Parse the lines into a 2D char array
        char[][] pattern = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            pattern[i] = lines.get(i).toCharArray();
        }

        // Return the tile pattern
        return new TilePattern(pattern);
    }

    /**
     * Tries out all possible incoming directions for the tile pattern from the
     * sides (thus two for the corners) and returns the maximum number of
     * energized tiles
     * @return The maximum number of energized tiles
     */
    public int findMaxEnergizedTiles() {
        int maxEnergizedTiles = 0;
        for (int i = 0; i < this.pattern.length; i++) {
            int leftTry = followLightBeams(i, 0, 1);
            System.out.printf("Left try:\t%d\t(%d, %d)\n", leftTry, i, 0);
            if (leftTry > maxEnergizedTiles) {
                maxEnergizedTiles = leftTry;
            }
            int rightTry = followLightBeams(i, this.pattern[0].length - 1, 3);
            System.out.printf("Right try:\t%d\t(%d, %d)\n", rightTry, i, this.pattern[0].length - 1);
            if (rightTry > maxEnergizedTiles) {
                maxEnergizedTiles = rightTry;
            }
        }
        for (int i = 0; i < this.pattern[0].length; i++) {
            int topTry = followLightBeams(0, i,  2);
            System.out.printf("Top try:\t%d\t(%d, %d)\n", topTry, 0, i);
            if (topTry > maxEnergizedTiles) {
                maxEnergizedTiles = topTry;
            }
            int bottomTry = followLightBeams(this.pattern.length - 1, i, 0);
            System.out.printf("Bottom try:\t%d\t(%d, %d)\n", bottomTry, this.pattern.length - 1, i);
            if (bottomTry > maxEnergizedTiles) {
                maxEnergizedTiles = bottomTry;
            }
        }
        return maxEnergizedTiles;
    }

    /**
     * Follow the light beams and count the number of energized tiles
     * @param x The starting x coordinate
     * @param y The starting y coordinate
     * @param direction The direction into the tile (0 = up, 1 = right, 2 = down, 3 = left)
     * @return The number of energized tiles (i.e. touched by a light beam)
     */
    private int followLightBeams(int x, int y, int direction) {
        // Reset the seen tiles
        this.seenTiles.clear();

        // Reset the energized pattern
        for (int i = 0; i < this.energizedPattern.length; i++) {
            for (int j = 0; j < this.energizedPattern[0].length; j++) {
                this.energizedPattern[i][j] = '.';
            }
        }

        // Follow the light beams
        follow(x, y, direction);

        // Count the number of energized tiles
        int energizedTiles = 0;
        for (char[] row : this.energizedPattern) {
            for (char tile : row) {
                if (tile == '#') {
                    energizedTiles++;
                }
            }
        }
        return energizedTiles;
    }

    /**
     * Follow the light beams and energize the tiles by marking them with a '#'
     * in the energizedPattern
     * @param x The starting x coordinate
     * @param y The starting y coordinate
     * @param direction The direction into the tile (0 = up, 1 = right, 2 = down, 3 = left)
     */
    private void follow(int x, int y, int direction) {
        // If we're out of bounds, return
        if (x < 0 || x >= this.pattern[0].length || y < 0 || y >= this.pattern.length) {
            return;
        }

        // If we've already seen this tile with the same direction, return; otherwise, mark the tile as seen
        int position = x * this.pattern[0].length + y;
        if (this.seenTiles.containsKey(position)) {
            if (this.seenTiles.get(position).contains(direction)) {
                return;
            }
        } else {
            this.seenTiles.put(position, new HashSet<>());
        }
        this.seenTiles.get(position).add(direction);

        // Mark the current tile as energized
        this.energizedPattern[x][y] = '#';

        // Depending on the current tile, follow the light beam
        switch (this.pattern[x][y]) {
            case '.':
                // If the current tile is a '.', follow the light beam in the same direction
                switch (direction) {
                    case 0:
                        follow(x - 1, y, direction);
                        break;
                    case 1:
                        follow(x, y + 1, direction);
                        break;
                    case 2:
                        follow(x + 1, y, direction);
                        break;
                    case 3:
                        follow(x, y - 1, direction);
                        break;
                }
                break;
            case '/':
                // If the current tile is a '/', follow the light beam in the mirror
                switch (direction) {
                    case 0:
                        follow(x, y + 1, 1);
                        break;
                    case 1:
                        follow(x - 1, y, 0);
                        break;
                    case 2:
                        follow(x, y - 1, 3);
                        break;
                    case 3:
                        follow(x + 1, y, 2);
                        break;
                }
                break;
            case '\\':
                // If the current tile is a '\', follow the light beam in the mirror
                switch (direction) {
                    case 0:
                        follow(x, y - 1, 3);
                        break;
                    case 1:
                        follow(x + 1, y, 2);
                        break;
                    case 2:
                        follow(x, y + 1, 1);
                        break;
                    case 3:
                        follow(x - 1, y, 0);
                        break;
                }
                break;
            case '|':
                // If the current tile is a '|', follow the light beam straight if vertical direction
                // or split the light beam if horizontal direction
                switch (direction) {
                    case 0:
                        follow(x - 1, y, direction);
                        break;
                    case 2:
                        follow(x + 1, y, direction);
                        break;
                    case 1:
                    case 3:
                        follow(x - 1, y, 0);
                        follow(x + 1, y, 2);
                        break;
                }
                break;
            case '-':
                // If the current tile is a '-', follow the light beam straight if horizontal direction
                // or split the light beam if vertical direction
                switch (direction) {
                    case 1:
                        follow(x, y + 1, direction);
                        break;
                    case 3:
                        follow(x, y - 1, direction);
                        break;
                    case 0:
                    case 2:
                        follow(x, y - 1, 3);
                        follow(x, y + 1, 1);
                        break;
                }
                break;
            default:
                System.out.printf("Invalid tile: %c\n", this.pattern[x][y]);
                System.exit(0);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Seen tiles: ");
        for (Map.Entry<Integer, Set<Integer>> entry : this.seenTiles.entrySet()) {
            int x = entry.getKey() / this.pattern[0].length;
            int y = entry.getKey() % this.pattern[0].length;
            sb.append(String.format("(%d, %d) -> %s, ", x, y, entry.getValue()));
        }
        sb.append('\n');
        for (int i = 0; i < this.pattern.length; i++) {
            for (int j = 0; j < this.pattern[0].length; j++) {
                sb.append(this.pattern[i][j]);
            }
            sb.append('\t');
            for (int j = 0; j < this.energizedPattern[0].length; j++) {
                sb.append(this.energizedPattern[i][j]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
