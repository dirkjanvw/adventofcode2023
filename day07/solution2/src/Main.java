import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        Path gamePath = Paths.get(args[0]);

        // Create a new game
        Game game = new Game(gamePath);

        // Return the game's result
        System.out.println(game.getResult());
    }
}