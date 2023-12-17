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
        int energizedTiles = tilePattern.followLightBeams(0, 0, 1);

        // Print the number of energized tiles
        System.out.println(energizedTiles);
    }
}