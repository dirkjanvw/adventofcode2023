import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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

    public void spin() {
        moveRocksNorth();
        moveRocksWest();
        moveRocksSouth();
        moveRocksEast();
    }

    private void moveRocksNorth() {
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

    private void moveRocksWest() {
        // move rocks west (left the row to the first cube or left edge of field)
        for (Character[] row : this.field) {
            for (int j = 0; j < this.field.get(0).length; j++) {
                if (row[j] == 'O') {
                    for (int k = j - 1; k >= 0; k--) {
                        if (row[k] == '#' || row[k] == 'O') {
                            break;
                        } else {
                            row[k] = 'O';
                            row[k + 1] = '.';
                        }
                    }
                }
            }
        }
    }

    private void moveRocksSouth() {
        // move rocks south (down the column to the first cube or bottom of field)
        for (int j = 0; j < this.field.get(0).length; j++) {
            for (int i = this.field.size() - 1; i >= 0; i--) {
                if (this.field.get(i)[j] == 'O') {
                    for (int k = i + 1; k < this.field.size(); k++) {
                        if (this.field.get(k)[j] == '#' || this.field.get(k)[j] == 'O') {
                            break;
                        } else {
                            this.field.get(k)[j] = 'O';
                            this.field.get(k - 1)[j] = '.';
                        }
                    }
                }
            }
        }
    }

    private void moveRocksEast() {
        // move rocks east (right the row to the first cube or right edge of field)
        for (Character[] row : this.field) {
            for (int j = this.field.get(0).length - 1; j >= 0; j--) {
                if (row[j] == 'O') {
                    for (int k = j + 1; k < this.field.get(0).length; k++) {
                        if (row[k] == '#' || row[k] == 'O') {
                            break;
                        } else {
                            row[k] = 'O';
                            row[k - 1] = '.';
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
