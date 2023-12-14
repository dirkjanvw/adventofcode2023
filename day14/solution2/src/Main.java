import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Read the field of rocks from first argument
        Field field = new Field(Paths.get(args[0]));

        // Move all rocks round until the field configuration repeats
        final int totalCycles = 1_000_000_000;
        Map<String, Integer> map = new HashMap<>();
        int cycleStart = -1;
        int cycleLength = -1;
        for (int i = 0; i < totalCycles; i++) {
            field.spin();
            if (map.containsKey(field.toString())) {
                cycleStart = map.get(field.toString());
                cycleLength = i - cycleStart;
                break;
            } else {
                map.put(field.toString(), i);
            }
            System.out.printf("\r%d:\t%d%n", i, field.getLoad());
        }

        // Create field with the same configuration as it would have at the end of the cycles
        Field endField = new Field(Paths.get(args[0]));
        for (int i = 0; i < cycleStart + (totalCycles - cycleStart) % cycleLength; i++) {
            endField.spin();
        }

        // Print the load of the field
        System.out.println(endField.getLoad());
    }
}