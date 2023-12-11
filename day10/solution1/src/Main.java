import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        // read the tile pattern from input file
        TilePattern tilePattern = new TilePattern(Paths.get(args[0]));

        // find the length for the loop containing 'S'
        int loopLength = tilePattern.findLoopLength('S');

        // return half of the loop length (rounded up) to get the furthest distance from 'S'
        System.out.println((int) Math.ceil(loopLength / 2.0));
    }
}