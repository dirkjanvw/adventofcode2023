import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    final private List<Hand> hands;

    public Game(Path gamePath) {
        try {
            this.hands = new HandParser(gamePath).parse();
        } catch (IOException e) {
            throw new RuntimeException("Could not parse game file", e);
        }
    }

    public long getResult() {
        Collections.sort(hands);

        long total = 0;
        for (int i = 0; i < hands.size(); i++) {
            int bid = hands.get(i).getBid();
            total += (long) bid * (i + 1);
        }
        return total;
    }

    private static class HandParser {
        final private Path gamePath;

        public HandParser(Path gamePath) {
            this.gamePath = gamePath;
        }

        public List<Hand> parse() throws IOException {
            final List<Hand> cards = new ArrayList<>();
            try (BufferedReader reader = Files.newBufferedReader(gamePath)) {
                while (reader.ready()) {
                    String line = reader.readLine();
                    String[] parts = line.split(" ");
                    cards.add(new Hand(parts[0], Integer.parseInt(parts[1])));
                }
            } catch (IOException e) {
                throw new IOException("Could not read game file", e);
            }
            return cards;
        }
    }
}
