import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // read the tile pattern from input file
        TilePattern tilePattern = new TilePattern(Paths.get(args[0]));
        System.out.println("Tile size: " + Arrays.toString(tilePattern.getSize()));

        // find the length for the loop containing 'S'
        List<Integer[]> loopCoords = tilePattern.findLoop('S');
        System.out.println("Loop length: " + loopCoords.size());

        // get the coordinates of all tiles not in the loop
        List<Integer[]> notInLoop = tilePattern.findNotInLoop(loopCoords);
        System.out.println("Tiles not in loop: " + notInLoop.size());

        // mask the tiles not in the loop
        TilePattern maskedTilePattern = tilePattern.mask(notInLoop, '.');
        System.out.println(tilePattern);

        // find all tiles that are enclosed by the loop
        List<Integer[]> enclosed = maskedTilePattern.findEnclosed(loopCoords);
        System.out.println(tilePattern.mask(enclosed, 'I'));

        // return the number of tiles enclosed by the loop
        System.out.println("Tiles enclosed by loop: " + enclosed.size());
    }
}