import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        // Read the records from the input file
        Records records = new Records(Paths.get(args[0]));

        // Search for all configurations and sum the total number of records
        System.out.println(records.searchAllConfigurations());
    }
}