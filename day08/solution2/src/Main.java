import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        // read node map from input file
        final NodeMap nodeMap = new NodeMap(Paths.get(args[0]));

        // follow all paths from ending on A to ending on Z
        final long steps = nodeMap.followAllPaths('A', 'Z');

        // print the number of steps
        System.out.println(steps);
    }
}