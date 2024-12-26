package com.codingnagger.adventofcode2024.days;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day25 implements Day {
    @Override
    public String partOne(List<String> input) {
        var locations = new ArrayList<String>();
        var keys = new ArrayList<Key>();
        var locks = new ArrayList<Lock>();

        for (var line : input) {
            if (line.isBlank()) {
                var filledLocations = Location.parseFilledFrom2D(locations);
                if (locations.getFirst().contains(".")) {
                    keys.add(new Key(filledLocations));
                } else {
                    locks.add(new Lock(filledLocations, Location.parseEmptyFrom2D(locations)));
                }
                locations = new ArrayList<>();
            } else {
                locations.add(line.trim());
            }
        }

        var filledLocations = Location.parseFilledFrom2D(locations);
        if (locations.getFirst().contains(".")) {
            keys.add(new Key(filledLocations));
        } else {
            locks.add(new Lock(filledLocations, Location.parseEmptyFrom2D(locations)));
        }

        return keys.stream().mapToLong(key -> locks.stream().filter(key::fits).count()).sum() + "";
    }

    @Override
    public String partTwo(List<String> input) {
        return "";
    }

    private record Location(int x, int y) {
        public static Set<Location> parseFilledFrom2D(List<String> locations) {
            return new HashSet<>(parseFrom2D(locations, '#'));
        }

        public static List<Location> parseEmptyFrom2D(ArrayList<String> locations) {
            return parseFrom2D(locations, '.');
        }

        public static List<Location> parseFrom2D(List<String> locations, char match) {
            var result = new ArrayList<Location>();

            var map = locations.stream().map(String::toCharArray).toArray(char[][]::new);
            for (var y = 0; y < map.length; y++) {
                for (var x = 0; x < map[y].length; x++) {
                    if (map[y][x] == match) {
                        result.add(new Location(x, y));
                    }
                }
            }

            return result;
        }
    }

    private record Lock(Set<Location> filledLocations, List<Location> emptyLocations) {
    }

    private record Key(Set<Location> filledLocations) {
        public boolean fits(Lock lock) {
            return lock.filledLocations().stream().noneMatch(filledLocations::contains);
        }
    }
}
