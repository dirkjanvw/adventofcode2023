import java.util.Arrays;

public class Game {
    final private int id;
    final private Play[] plays;

    public Game(int id, Play[] plays) {
        this.id = id;
        this.plays = plays;
    }

    public static Game parseGame(String gameString) { // e.g. "Game 1: 1 red, 1 green, 1 blue; 2 blue, 2 green"
        final String[] gameStringArray = gameString.split(": ");
        final int id = Integer.parseInt(gameStringArray[0].split(" ")[1]);
        final String[] playsStringArray = gameStringArray[1].split("; ");

        final Play[] plays = new Play[playsStringArray.length];
        for (int i = 0; i < playsStringArray.length; i++) {
            plays[i] = Play.parsePlay(playsStringArray[i]);
        }

        return new Game(id, plays);
    }

    public boolean isPossible(Play maxPlay) {
        for (Play play : plays) {
            if (!play.isPossible(maxPlay)) {
                return false;
            }
        }
        return true;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("Game{id=%d, plays=%s}", id, Arrays.toString(plays));
    }
}
