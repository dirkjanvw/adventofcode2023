import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Records {
    final private Path path;
    final private List<Record> records;

    public Records(Path path) {
        this.path = path;
        this.records = new ArrayList<>();
        read();
    }

    private void read() {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            while (reader.ready()) {
                String line = reader.readLine();
                records.add(new Record(line));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BigInteger searchAllConfigurations() {
        BigInteger total = BigInteger.ZERO;
        for (Record record : records) {
            total = total.add(record.searchAllConfigurations());
        }
        return total;
    }
}
