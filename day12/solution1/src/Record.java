import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Record {
    final private char[] configuration;
    final private int[] configurationAsInt;

    public Record(String line) {
        String[] parts = line.split("\\s+");
        this.configuration = parts[0].toCharArray();
        String[] configurationAsIntString = parts[1].split(",");
        this.configurationAsInt = new int[configurationAsIntString.length];
        for (int i = 0; i < configurationAsIntString.length; i++) {
            configurationAsInt[i] = Integer.parseInt(configurationAsIntString[i]);
        }
    }

    public int searchAllConfigurations() {
        // Get the total of the numbers in configurationAsInt
        int totalLength = 0;
        for (int j : configurationAsInt) {
            totalLength += j;
        }

        // Create all possible configurations by replacing the '?' by '#' or '.' and add them to a set
        Set<String> possibleConfigurations = createAllConfigurations(totalLength);

        // Count the number of records that match the possible configurations
        int total = 0;
        for (String possibleConfiguration : possibleConfigurations) {
            if (matches(possibleConfiguration)) {
//                System.out.printf("possibleConfiguration = %s matches record %s\n", possibleConfiguration, Arrays.toString(configurationAsInt));
                total++;
            } else {
//                System.out.printf("possibleConfiguration = %s does not match record %s\n", possibleConfiguration, Arrays.toString(configurationAsInt));
            }
        }

        return total;
    }

    private Set<String> createAllConfigurations(int length) {
        // Record the positions of '?' in the configuration
        boolean[] isQuestionMark = new boolean[configuration.length];
        int totalQuestionMarks = 0;
        for (int i = 0; i < configuration.length; i++) {
            if (configuration[i] == '?') {
                isQuestionMark[i] = true;
                totalQuestionMarks++;
            }
        }

        // Create all possible configurations by replacing the '?' by '#' or '.' for each position
        Set<String> possibleConfigurations = new HashSet<>();
        for (int i = 0; i < Math.pow(2, totalQuestionMarks); i++) {
            StringBuilder possibleConfiguration = new StringBuilder();
            int j = 0;
            for (int k = 0; k < configuration.length; k++) {
                if (isQuestionMark[k]) {
                    if ((i & (1 << j)) != 0) {
                        possibleConfiguration.append('#');
                    } else {
                        possibleConfiguration.append('.');
                    }
                    j++;
                } else {
                    possibleConfiguration.append(configuration[k]);
                }
            }

            // only add if total number of '#' is equal to length
            int total = 0;
            for (int l = 0; l < possibleConfiguration.length(); l++) {
                if (possibleConfiguration.charAt(l) == '#') {
                    total++;
                }
            }
            if (total == length) {
                possibleConfigurations.add(possibleConfiguration.toString());
            }
        }

//        System.out.println("possibleConfigurations = " + possibleConfigurations);

        return possibleConfigurations;
    }

    private boolean matches(String possibleConfiguration) {
        // Check if the possible configuration matches the record (configurationAsInt mentions the length of each
        // sequence of '#' in the configuration)
        String[] parts = possibleConfiguration
                .replaceAll("^\\.+", "")
                .replaceAll("\\.+$", "")
                .split("\\.+");
        if (parts.length != configurationAsInt.length) {
            return false;
        }
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].length() != configurationAsInt[i]) {
                return false;
            }
        }
        return true;
    }
}
