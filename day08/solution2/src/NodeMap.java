import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class NodeMap {
    private char[] sequence;
    final private Map<String, String[]> nodes = new HashMap<>();

    public NodeMap(Path filename) {
        try {
            read(filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void read(Path filename) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(filename)) {
            for (int lineNr = 1; reader.ready(); lineNr++) {
                String line = reader.readLine();
                if (lineNr == 1) {
                    this.sequence = line.toCharArray();
                } else if (lineNr > 2) {
                    String[] parts = line
                            .replace("(", "")
                            .replace(")", "")
                            .replace(",", "")
                            .replace("=", "")
                            .split("\\s+");
                    String source = parts[0];
                    String leftTarget = parts[1];
                    String rightTarget = parts[2];
                    nodes.put(source, new String[]{leftTarget, rightTarget});
                }
            }
        } catch (IOException e) {
            throw new IOException("Error reading file " + filename, e);
        }
    }

    public long followAllPaths(char start, char end) {
        final String[] currentNodes = getStartNodes(start);
        final int[] lowestNrEndings = new int[currentNodes.length];
        int steps = 0;


        int totalFoundEndings = 0;
        while (totalFoundEndings < currentNodes.length) {
            char direction = sequence[steps % sequence.length];
            steps++;

            int nrEndings = 0;
            for (int i = 0; i < currentNodes.length; i++) {
                if (direction == 'L') {
                    currentNodes[i] = nodes.get(currentNodes[i])[0];
                } else {
                    currentNodes[i] = nodes.get(currentNodes[i])[1];
                }
                if (currentNodes[i].endsWith(String.valueOf(end))) {
                    nrEndings++;
                    lowestNrEndings[i] = steps;
                }
            }

            totalFoundEndings = 0;
            for (int lowestNrEnding : lowestNrEndings) {
                if (lowestNrEnding > 0) {
                    totalFoundEndings++;
                }
            }
        }
        
        return lcmOfArray(lowestNrEndings);
    }

    // Function to calculate GCD of two numbers (written by phind.com)
    private long gcd(long a, long b) {
        if (b == 0)
            return a;
        else
            return gcd(b, a % b);
    }

    // Function to calculate LCM of two numbers (written by phind.com)
    private long lcm(long a, long b) {
        return (a * b) / gcd(a, b);
    }

    // Function to calculate LCM of an array of numbers (written by phind.com)
    private long lcmOfArray(int[] arr) {
        long lcm = arr[0];
        for (int i = 1; i < arr.length; i++) {
            lcm = lcm(lcm, arr[i]);
        }
        return lcm;
    }

    private String[] getStartNodes(char start) {
        List<String> startNodes = new ArrayList<>();
        for (String key : nodes.keySet()) {
            if (key.endsWith(String.valueOf(start))) {
                startNodes.add(key);
            }
        }
        return startNodes.toArray(new String[0]);
    }

    @Override
    public String toString() {
        return String.format("NodeMap{sequence=%s, nodes=%s}", Arrays.toString(sequence), nodes);
    }
}
