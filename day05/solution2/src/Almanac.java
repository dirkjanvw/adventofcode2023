import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class Almanac {
    final private Path almanacPath;
    final private Set<Long[]> seeds = new HashSet<>();
    final private AlmanacMap soilToSeed = new AlmanacMap();
    final private AlmanacMap fertilizerToSoil = new AlmanacMap();
    final private AlmanacMap waterToFertilizer = new AlmanacMap();
    final private AlmanacMap lightToWater = new AlmanacMap();
    final private AlmanacMap temperatureToLight = new AlmanacMap();
    final private AlmanacMap humidityToTemperature = new AlmanacMap();
    final private AlmanacMap locationToHumidity = new AlmanacMap();

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
                    for (int i = 1; i < seedsString.length; i += 2) {
                        final long seedStart = Long.parseLong(seedsString[i]);
                        final long seedRange = Long.parseLong(seedsString[i + 1]);
                        this.seeds.add(new Long[]{seedStart, seedRange});
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
                    this.soilToSeed.put(destinationStart, sourceStart, range);
                } else if (isSoilToFertilizerMap) {
                    this.fertilizerToSoil.put(destinationStart, sourceStart, range);
                } else if (isFertilizerToWaterMap) {
                    this.waterToFertilizer.put(destinationStart, sourceStart, range);
                } else if (isWaterToLightMap) {
                    this.lightToWater.put(destinationStart, sourceStart, range);
                } else if (isLightToTemperatureMap) {
                    this.temperatureToLight.put(destinationStart, sourceStart, range);
                } else if (isTemperatureToHumidityMap) {
                    this.humidityToTemperature.put(destinationStart, sourceStart, range);
                } else if (isHumidityToLocationMap) {
                    this.locationToHumidity.put(destinationStart, sourceStart, range);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getLowestLocation() {
        for (long location = 0; true; location++) {
            if (isLocationValid(location)) {
                return location;
            }
        }
    }

    private boolean isLocationValid(long location) {
        long humidity = this.locationToHumidity.get(location);
        long temperature = this.humidityToTemperature.get(humidity);
        long light = this.temperatureToLight.get(temperature);
        long water = this.lightToWater.get(light);
        long fertilizer = this.waterToFertilizer.get(water);
        long soil = this.fertilizerToSoil.get(fertilizer);
        long seed = this.soilToSeed.get(soil);
        for (Long[] seedRange : this.seeds) {
            long seedStart = seedRange[0];
            long seedRangeLength = seedRange[1];
            if (seed >= seedStart && seed < seedStart + seedRangeLength) {
                return true;
            }
        }
        return false;
    }
}
