import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Record {
    final private char[] configuration;
    final private int[] configurationAsInt;
    final private Map<String, BigInteger> cache = new HashMap<>();

    /**
     * Create a new record from a line of input
     * @param line a line of input, a couple examples:
     *             ???.### 1,1,3
     *             .??..??...?##. 1,1,3
     *             ?#?#?#?#?#?#?#? 1,3,1,6
     *             ????.#...#... 4,1,1
     *             ????.######..#####. 1,6,5
     *             ?###???????? 3,2,1
     */
    public Record(String line) {
        String[] parts = line.split("\\s+");
        final char[] tmpConfiguration = parts[0].toCharArray();
        String[] configurationAsIntString = parts[1].split(",");
        final int[] tmpConfigurationAsInt = new int[configurationAsIntString.length];
        for (int i = 0; i < configurationAsIntString.length; i++) {
            tmpConfigurationAsInt[i] = Integer.parseInt(configurationAsIntString[i]);
        }

        // configuration is five copies of tmpConfiguration separated by a '?'
        configuration = new char[5 * tmpConfiguration.length + 4];
        for (int i = 0; i < configuration.length; i++) {
            int j = i % (tmpConfiguration.length + 1);
            if (j == tmpConfiguration.length) {
                configuration[i] = '?';
            } else {
                configuration[i] = tmpConfiguration[j];
            }
        }

        // configurationAsInt is five copies of tmpConfigurationAsInt
        configurationAsInt = new int[5 * tmpConfigurationAsInt.length];
        for (int i = 0; i < configurationAsInt.length; i++) {
            configurationAsInt[i] = tmpConfigurationAsInt[i % tmpConfigurationAsInt.length];
        }
    }

    public BigInteger searchAllConfigurations() {
        // Find all possible configurations for the record using recursion
        final BigInteger totalPossibleConfigurations = countPossibleConfigurations(
                configuration,
                configurationAsInt,
                0,
                0,
                0
        );
        System.out.printf("Found %d possible configurations for record %s in configuration %s%n",
                totalPossibleConfigurations,
                Arrays.toString(configurationAsInt),
                String.valueOf(configuration)
        );

        return totalPossibleConfigurations;
    }

    /**
     * Find all possible configurations for the record using recursion
     * @param configuration the configuration to search
     * @param configurationAsInt the lengths to match (e.g., 1,1,3) for #s
     * @param index the current index in the configuration
     * @param count the current count of #s matched
     * @return the total number of possible configurations
     *
     * Inspired by this Kotlin solution from a friendly stranger on reddit:
     * https://github.com/ClouddJR/advent-of-code-2023/blob/main/src/main/kotlin/com/clouddjr/advent2023/Day12.kt
     * ```
     * private fun getArrangements(conditions: String, groups: List<Int>, pos: Int, group: Int, groupLen: Int): Long {
     *   return cache.getOrPut(State(pos, group, groupLen)) {
     *     if (pos == conditions.length) {
     *       return if (group == groups.size && groupLen == 0) 1 else 0
     *     }
     *     var sum = 0L
     *     if (conditions[pos] in ".?") {
     *       if (group < groups.size && groupLen == groups[group]) {
     *         sum += getArrangements(conditions, groups, pos + 1, group + 1, 0)
     *       }
     *       if (groupLen == 0) {
     *         sum += getArrangements(conditions, groups, pos + 1, group, groupLen)
     *       }
     *     }
     *     if (conditions[pos] in "#?") {
     *       sum += getArrangements(conditions, groups, pos + 1, group, groupLen + 1)
     *     }
     *     sum
     *   }
     * }
     * ```
     */
    private BigInteger countPossibleConfigurations(char[] configuration, int[] configurationAsInt, int index, int count, int groupLength) {
        String key = Arrays.toString(configuration) + "-" + Arrays.toString(configurationAsInt) + "-" + index + "-" + count + "-" + groupLength;

        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        if (index == configuration.length) {
            BigInteger result = (count == configurationAsInt.length && groupLength == 0) ? BigInteger.ONE : BigInteger.ZERO;
            cache.put(key, result);
            return result;
        }

        BigInteger sum = BigInteger.ZERO;

        if (configuration[index] == '.' || configuration[index] == '?') {
            if (count < configurationAsInt.length && groupLength == configurationAsInt[count]) {
                sum = sum.add(countPossibleConfigurations(configuration, configurationAsInt, index + 1, count + 1, 0));
            }

            if (groupLength == 0) {
                sum = sum.add(countPossibleConfigurations(configuration, configurationAsInt, index + 1, count, groupLength));
            }
        }

        if (configuration[index] == '#' || configuration[index] == '?') {
            sum = sum.add(countPossibleConfigurations(configuration, configurationAsInt, index + 1, count, groupLength + 1));
        }

        cache.put(key, sum);
        return sum;
    }
}
