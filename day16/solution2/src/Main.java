import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        // Get tile pattern from first input
        TilePattern tilePattern;
        try {
            tilePattern = TilePattern.parseTilePatternFromFile(Paths.get(args[0]));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Follow the light beams and count the number of energized tiles
        int maxEnergizedTiles = tilePattern.findMaxEnergizedTiles();

        // Print the number of max energized tiles
        System.out.println(maxEnergizedTiles);
    }
}