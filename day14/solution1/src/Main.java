import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        // Read the field of rocks from first argument
        Field field = new Field(Paths.get(args[0]));

        // Move all rocks north
        field.moveRocksNorth();

        // Print the load of the field
        System.out.println(field.getLoad());
    }
}