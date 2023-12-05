import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        int totalPower = 0;

        // Obtain the filename from the first argument
        Path filename = Paths.get(args[0]);

        // For each game, find the smallest play that is possible
        try {
            for (String line : Files.readAllLines(filename)) {
                final Game game = Game.parseGame(line);
                final Play smallestPlay = game.getSmallestPlay();
                totalPower += smallestPlay.getPower();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // print the total number of possible games
        System.out.println(totalPower);
    }
}