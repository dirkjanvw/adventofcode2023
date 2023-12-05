import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        int totalPossibleGames = 0;

        // Obtain the filename from the first argument
        Path filename = Paths.get(args[0]);

        // Obtain the number of red, green and blue cubes from the second, third and fourth arguments
        int red = Integer.parseInt(args[1]);
        int green = Integer.parseInt(args[2]);
        int blue = Integer.parseInt(args[3]);
        Play maxPlay = new Play(red, green, blue);
        System.out.printf("maxPlay: %s%n", maxPlay);

        // Find the total number of possible games
        try {
            for (String line : Files.readAllLines(filename)) {
                Game game = Game.parseGame(line);
                System.out.println("\tchecking game: " + game + " ...");
                if (game.isPossible(maxPlay)) {
                    System.out.println("\t\tgame is possible");
                    totalPossibleGames += game.getId();
                } else {
                    System.out.println("\t\tgame is not possible");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // print the total number of possible games
        System.out.println(totalPossibleGames);
    }
}