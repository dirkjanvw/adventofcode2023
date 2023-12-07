import java.util.HashSet;
import java.util.Set;

public class AlmanacMap {
    final private Set<Long[]> entries;

    public AlmanacMap() {
        this.entries = new HashSet<>();
    }

    public void put(long sourceStart, long destinationStart, long range) {
        this.entries.add(new Long[]{sourceStart, destinationStart, range});
    }

    public long get(long query) {
        for (Long[] entry : this.entries) {
            long sourceStart = entry[0];
            long destinationStart = entry[1];
            long range = entry[2];
            if (query >= sourceStart && query < sourceStart + range) {
                return destinationStart + query - sourceStart;
            }
        }
        return query;
    }
}
