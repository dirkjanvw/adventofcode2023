import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        Path almanacPath = Paths.get(args[0]);

        // Read the almanac
        Almanac almanac = new Almanac(almanacPath);

        // Print the input for the almanac that generates the lowest output
        System.out.println(almanac.getLowestLocation());
    }
}