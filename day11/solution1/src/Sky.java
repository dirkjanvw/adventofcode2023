import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class Sky {
    final private Set<Galaxy> galaxies = new HashSet<>();

    public Sky(Path galaxyFile) {
        read(galaxyFile);
    }

    /**
     * Read galaxy data from a file.
     * @param galaxyFile the file containing the galaxy data
     */
    private void read(Path galaxyFile) {
        try (BufferedReader reader = Files.newBufferedReader(galaxyFile)) {
            for (int lineNr = 0; reader.ready(); lineNr++) {
                char[] line = reader.readLine().toCharArray();
                for (int colNr = 0; colNr < line.length; colNr++) {
                    if (line[colNr] == '#') {
                        this.galaxies.add(new Galaxy(colNr, lineNr));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Expand the sky by adding a new galaxy at the origin.
     */
    public void expand() {
        // find the highest x and y coordinates
        int maxX = 0;
        int maxY = 0;
        for (Galaxy galaxy : this.galaxies) {
            if (galaxy.getX() > maxX) {
                maxX = galaxy.getX();
            }
            if (galaxy.getY() > maxY) {
                maxY = galaxy.getY();
            }
        }

        // for each x that doesn't occur, double that column
        Set<Integer> emptyColumns = new HashSet<>();
        for (int x = 0; x <= maxX; x++) {
            boolean found = false;
            for (Galaxy galaxy : this.galaxies) {
                if (galaxy.getX() == x) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                emptyColumns.add(x);
            }
        }
        for (Galaxy galaxy : this.galaxies) {
            int emptyColumnsToLeft = 0;
            for (int x = 0; x < galaxy.getX(); x++) {
                if (emptyColumns.contains(x)) {
                    emptyColumnsToLeft++;
                }
            }
            galaxy.setX(galaxy.getX() + emptyColumnsToLeft);
        }

        // for each y that doesn't occur, double that row
        Set<Integer> emptyRows = new HashSet<>();
        for (int y = 0; y <= maxY; y++) {
            boolean found = false;
            for (Galaxy galaxy : this.galaxies) {
                if (galaxy.getY() == y) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                emptyRows.add(y);
            }
        }
        for (Galaxy galaxy : this.galaxies) {
            int emptyRowsAbove = 0;
            for (int y = 0; y < galaxy.getY(); y++) {
                if (emptyRows.contains(y)) {
                    emptyRowsAbove++;
                }
            }
            galaxy.setY(galaxy.getY() + emptyRowsAbove);
        }
    }

    /**
     * Calculate the sum of the distances between all pairs of galaxies.
     * @return the sum of the distances between all pairs of galaxies
     */
    public int sumDistances() {
        int sum = 0;
        Set<Galaxy> seenGalaxies = new HashSet<>();
        for (Galaxy galaxy : this.galaxies) {
            for (Galaxy other : this.galaxies) {
                if (galaxy != other && !seenGalaxies.contains(other)) {
                    sum += galaxy.distanceTo(other);
                }
            }
            seenGalaxies.add(galaxy);
        }
        return sum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int maxX = 0;
        int maxY = 0;
        for (Galaxy galaxy : this.galaxies) {
            if (galaxy.getX() > maxX) {
                maxX = galaxy.getX();
            }
            if (galaxy.getY() > maxY) {
                maxY = galaxy.getY();
            }
        }
        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
                boolean found = false;
                for (Galaxy galaxy : this.galaxies) {
                    if (galaxy.getX() == x && galaxy.getY() == y) {
                        sb.append('#');
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    sb.append('.');
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
