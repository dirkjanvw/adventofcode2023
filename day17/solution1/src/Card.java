import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Card {
    final int[][] card;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";


    public Card(final int[][] card) {
        this.card = card;
    }

    public static Card parseCardFromFile(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        int[][] card = new int[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            char[] chars = lines.get(i).toCharArray();
            card[i] = new int[chars.length];
            for (int j = 0; j < chars.length; j++) {
                card[i][j] = Character.getNumericValue(chars[j]);
            }
        }
        return new Card(card);
    }

    /**
     * Implements Dijkstra's algorithm to find the minimum cost path from the top left to the bottom right of the card.
     * @return The minimum cost to get from the top left to the bottom right of the card.
     */
    public int findMinimumCost() {
        // Initialise cost
        final int[][] cost = new int[card.length][card[0].length]; // will store the minimum cost to get to each cell
        final String[][] prev = new String[card.length][card[0].length]; // will store the previous cell in the path (> for left, < for right, v for up, ^ for down)
        final List<Integer> unvisited = new ArrayList<>(card.length * card[0].length); // will store the unvisited cells
        for (int i = 0; i < cost.length; i++) {
            for (int j = 0; j < cost[0].length; j++) {
                cost[i][j] = Integer.MAX_VALUE;
                prev[i][j] = "";
                unvisited.add(i * cost[0].length + j);
            }
        }
        cost[0][0] = 0;

        // Find the minimum cost path
        while (!unvisited.isEmpty()) {
            // Find the unvisited cell with the minimum cost
            int minCost = Integer.MAX_VALUE;
            int minCostCell = -1;
            for (int cell : unvisited) {
                int x = cell / cost[0].length;
                int y = cell % cost[0].length;
                if (cost[x][y] < minCost) {
                    minCost = cost[x][y];
                    minCostCell = cell;
                }
            }
            unvisited.remove(Integer.valueOf(minCostCell));

            // Get the x and y coordinates of the minimum cost cell
            final int x = minCostCell / cost[0].length;
            final int y = minCostCell % cost[0].length;
            System.out.printf("Visiting cell (%d, %d) with cost %d\n", x, y, minCost);
            System.out.println("Previous cell: " + prev[x][y]);

            // Get all possible neighbours of the minimum cost cell: exclude
            // cells that are out of bounds, cells for which the previous cell
            // in the path is the current cell (to avoid cycles), and cells for
            // which the same direction was taken twice in a row (to avoid
            // >3 long stretches of the same direction)
            final Map<Character, Integer> neighbours = new HashMap<>(); // will store the prev identifier and the cell number
            if (y > 0 && !(prev[x][y].length() >= 3 && prev[x][y].endsWith("<"))) {
                neighbours.put('<', x * cost[0].length + y - 1); // right
            }
            if (y < cost[0].length - 1 && !(prev[x][y].length() >= 3 && prev[x][y].endsWith(">"))) {
                neighbours.put('>', x * cost[0].length + y + 1); // left
            }
            if (x > 0 && !(prev[x][y].length() >= 3 && prev[x][y].endsWith("^"))) {
                neighbours.put('^', (x - 1) * cost[0].length + y); // down
            }
            if (x < cost.length - 1 && !(prev[x][y].length() >= 3 && prev[x][y].endsWith("v"))) {
                neighbours.put('v', (x + 1) * cost[0].length + y); // up
            }

            // Update the cost of each unvisited neighbour
            for (Map.Entry<Character, Integer> neighbour : neighbours.entrySet()) {
                final char neighbourPrev = neighbour.getKey();
                final int neighbourX = neighbour.getValue() / cost[0].length;
                final int neighbourY = neighbour.getValue() % cost[0].length;
                final int neighbourCost = cost[x][y] + card[neighbourX][neighbourY];
                if (neighbourCost < cost[neighbourX][neighbourY]) {
                    cost[neighbourX][neighbourY] = neighbourCost;
                    if (prev[x][y].endsWith(String.valueOf(neighbourPrev))) {
                        prev[neighbourX][neighbourY] += prev[x][y] + neighbourPrev;
                    } else {
                        prev[neighbourX][neighbourY] += neighbourPrev;
                    }
                }
            }
        }

        // Print the path
        int cell = cost.length * cost[0].length - 1;
        List<Integer> path = new ArrayList<>();
        while (cell != -1) {
            path.add(cell);
            int x = cell / cost[0].length;
            int y = cell % cost[0].length;
            if (prev[x][y].contains("<")) {
                cell = x * cost[0].length + y + 1;
            } else if (prev[x][y].contains(">")) {
                cell = x * cost[0].length + y - 1;
            } else if (prev[x][y].contains("^")) {
                cell = (x + 1) * cost[0].length + y;
            } else if (prev[x][y].contains("v")) {
                cell = (x - 1) * cost[0].length + y;
            } else {
                cell = -1;
            }
        }
        for (int i = 0; i < card.length; i++) {
            for (int j = 0; j < card[0].length; j++) {
                if (path.contains(i * card[0].length + j)) {
                    System.out.print(ANSI_RED + card[i][j] + ANSI_RESET);
                } else {
                    System.out.print(card[i][j]);
                }
            }
            System.out.println();
        }

        // Return the minimum cost
        return cost[cost.length - 1][cost[0].length - 1];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : card) {
            for (int i : row) {
                sb.append(i);
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
