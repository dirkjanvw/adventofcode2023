import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        // read sky data from file
        Sky sky = new Sky(Paths.get(args[0]));

        // expand galaxy
        sky.expand(1000000);

        // calculate pair-wise distance between galaxies and sum
        System.out.println(sky.sumDistances());
    }
}