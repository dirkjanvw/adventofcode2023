import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    final private List<Card> cards;

    public Game(Path gamePath) {
        try {
            this.cards = new CardParser(gamePath).parse();
        } catch (IOException e) {
            throw new RuntimeException("Could not parse game file", e);
        }
    }

    public long getResult() {
        Collections.sort(cards);

        long total = 0;
        for (int i = 0; i < cards.size(); i++) {
            int bid = cards.get(i).getBid();
            total += (long) bid * (i + 1);
        }
        return total;
    }

    private static class CardParser {
        final private Path gamePath;

        public CardParser(Path gamePath) {
            this.gamePath = gamePath;
        }

        public List<Card> parse() throws IOException {
            final List<Card> cards = new ArrayList<>();
            try (BufferedReader reader = Files.newBufferedReader(gamePath)) {
                while (reader.ready()) {
                    String line = reader.readLine();
                    String[] parts = line.split(" ");
                    cards.add(new Card(parts[0], Integer.parseInt(parts[1])));
                }
            } catch (IOException e) {
                throw new IOException("Could not read game file", e);
            }
            return cards;
        }
    }
}
