import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Almanac {
    final private Path almanacPath;
    private long[] seeds;
    final private AlmanacMap seedToSoil = new AlmanacMap();
    final private AlmanacMap soilToFertilizer = new AlmanacMap();
    final private AlmanacMap fertilizerToWater = new AlmanacMap();
    final private AlmanacMap waterToLight = new AlmanacMap();
    final private AlmanacMap lightToTemperature = new AlmanacMap();
    final private AlmanacMap temperatureToHumidity = new AlmanacMap();
    final private AlmanacMap humidityToLocation = new AlmanacMap();

    public Almanac(Path almanacPath) {
        this.almanacPath = almanacPath;
        read();
    }

    private void read() {
        boolean isSeedToSoilMap = false;
        boolean isSoilToFertilizerMap = false;
        boolean isFertilizerToWaterMap = false;
        boolean isWaterToLightMap = false;
        boolean isLightToTemperatureMap = false;
        boolean isTemperatureToHumidityMap = false;
        boolean isHumidityToLocationMap = false;
        try (BufferedReader newBufferedReader = Files.newBufferedReader(almanacPath)) {
            while (newBufferedReader.ready()) {
                String line = newBufferedReader.readLine();

                if (line.equals("")) {
                    continue;
                }

                if (line.startsWith("seeds: ")) {
                    String[] seedsString = line.split("\\s+");
                    this.seeds = new long[seedsString.length - 1];
                    for (int i = 1; i < seedsString.length; i++) {
                        this.seeds[i - 1] = Long.parseLong(seedsString[i]);
                    }
                    continue;
                }

                if (line.equals("seed-to-soil map:")) {
                    isSeedToSoilMap = true;
                    continue;
                }
                if (line.equals("soil-to-fertilizer map:")) {
                    isSeedToSoilMap = false;
                    isSoilToFertilizerMap = true;
                    continue;
                }
                if (line.equals("fertilizer-to-water map:")) {
                    isSoilToFertilizerMap = false;
                    isFertilizerToWaterMap = true;
                    continue;
                }
                if (line.equals("water-to-light map:")) {
                    isFertilizerToWaterMap = false;
                    isWaterToLightMap = true;
                    continue;
                }
                if (line.equals("light-to-temperature map:")) {
                    isWaterToLightMap = false;
                    isLightToTemperatureMap = true;
                    continue;
                }
                if (line.equals("temperature-to-humidity map:")) {
                    isLightToTemperatureMap = false;
                    isTemperatureToHumidityMap = true;
                    continue;
                }
                if (line.equals("humidity-to-location map:")) {
                    isTemperatureToHumidityMap = false;
                    isHumidityToLocationMap = true;
                    continue;
                }

                String[] lineSplit = line.split("\\s+");
                long destinationStart = Long.parseLong(lineSplit[0]);
                long sourceStart = Long.parseLong(lineSplit[1]);
                long range = Long.parseLong(lineSplit[2]);
                if (isSeedToSoilMap) {
                    this.seedToSoil.put(sourceStart, destinationStart, range);
                } else if (isSoilToFertilizerMap) {
                    this.soilToFertilizer.put(sourceStart, destinationStart, range);
                } else if (isFertilizerToWaterMap) {
                    this.fertilizerToWater.put(sourceStart, destinationStart, range);
                } else if (isWaterToLightMap) {
                    this.waterToLight.put(sourceStart, destinationStart, range);
                } else if (isLightToTemperatureMap) {
                    this.lightToTemperature.put(sourceStart, destinationStart, range);
                } else if (isTemperatureToHumidityMap) {
                    this.temperatureToHumidity.put(sourceStart, destinationStart, range);
                } else if (isHumidityToLocationMap) {
                    this.humidityToLocation.put(sourceStart, destinationStart, range);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getLowestLocation() {
        final Map<Long, Long> seedToLocation = new HashMap<>();
        for (long seed : seeds) {
            long soil = seedToSoil.get(seed);
            long fertilizer = soilToFertilizer.get(soil);
            long water = fertilizerToWater.get(fertilizer);
            long light = waterToLight.get(water);
            long temperature = lightToTemperature.get(light);
            long humidity = temperatureToHumidity.get(temperature);
            long location = humidityToLocation.get(humidity);
            seedToLocation.put(seed, location);
        }

        long lowestLocation = Long.MAX_VALUE;
        for (Map.Entry<Long, Long> entry : seedToLocation.entrySet()) {
            if (entry.getValue() < lowestLocation) {
                lowestLocation = entry.getValue();
            }
        }

        return lowestLocation;
    }
}
