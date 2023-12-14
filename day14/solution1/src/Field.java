import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Field {
    final private List<Character[]> field; // '.' = empty, 'O' = rock, '#' = cube

    public Field(Path path) {
        this.field = new ArrayList<>();
        read(path);
    }

    private void read(Path path) {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            while (reader.ready()) {
                final char[] row = reader.readLine().toCharArray();
                final Character[] rowCharacter = new Character[row.length];
                for (int i = 0; i < row.length; i++) {
                    rowCharacter[i] = row[i];
                }
                this.field.add(rowCharacter);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void moveRocksNorth() {
        // store the position of cubes for each column
        Map<Integer, List<Integer>> cubes = new HashMap<>();
        for (int j = 0; j < this.field.get(0).length; j++) {
            cubes.put(j, new ArrayList<>());
            for (int i = 0; i < this.field.size(); i++) {
                if (this.field.get(i)[j] == '#') {
                    cubes.get(j).add(i);
                }
            }
        }

        // move rocks north (up the column to the first cube or top of field)
        for (int j = 0; j < this.field.get(0).length; j++) {
            for (int i = 0; i < this.field.size(); i++) {
                if (this.field.get(i)[j] == 'O') {
                    for (int k = i - 1; k >= 0; k--) {
                        if (this.field.get(k)[j] == '#' || this.field.get(k)[j] == 'O') {
                            break;
                        } else {
                            this.field.get(k)[j] = 'O';
                            this.field.get(k + 1)[j] = '.';
                        }
                    }
                }
            }
        }
    }

    public int getLoad() {
        final int height = this.field.size();
        int load = 0;
        for (int i = 0; i < height; i++) {
            final Character[] row = this.field.get(i);
            for (char c : row) {
                if (c == 'O') {
                    load += (height - i);
                }
            }
        }
        return load;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Character[] row : this.field) {
            for (char c : row) {
                sb.append(c);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
