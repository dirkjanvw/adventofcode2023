import java.io.IOException;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        // Read the card from the input file
        final Card card;
        try {
            card = Card.parseCardFromFile(Paths.get(args[0]));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        // Find the path with the minimum cost
        final int cost = card.findMinimumCost();

        // Print the result
        System.out.println(cost);
    }
}