package com.codingnagger.adventofcode2024.days;

import java.util.*;

public class Day12 implements Day {
    @Override
    public String partOne(List<String> input) {
        // 1140080 answer too low
        // the more distinct regions the more fences
        // square of 4 disctinct single-area regions -> totalPrice = (4 + 4 + 4 + 4) = 16
        // square of 1 disctinct 4-area region -> totalPrice = = 8
        prettyMapPrint(input);
        return Garden.parse(input).totalPrice() + "";
    }

    private void prettyMapPrint(List<String> input) {
        for (var line : input) {
            System.out.println(colorise(line));
        }
    }

    private static final Map<Character, String> charMap = new HashMap<>();

    private String colorise(String line) {
        var colored = line;
        var reset = "\033[0m";
        var random = new Random();

        for (char c = 'A'; c <= 'Z'; c++) {
            if (!charMap.containsKey(c)) {
                int r = random.nextInt(200);
                int g = random.nextInt(200);
                int b = random.nextInt(200);
                charMap.put(c, rgbColor(r, g, b));
            }

            // Generate a unique color for each letter using a simple formula


            colored = colored.replaceAll(Character.toString(c), charMap.get(c) + c + reset);
        }

        return colored;
    }

    String rgbColor(int r, int g, int b) {
        return "\033[38;2;" + r + ";" + g + ";" + b + "m";
    }

    @Override
    public String partTwo(List<String> input) {
        return "";
    }

    private record Garden(Set<Region> regions) {
        public static Garden parse(List<String> input) {
            var map = input.stream().map(String::toCharArray).toArray(char[][]::new);
            Region region = null;
            var visited = new HashSet<Location>();
            var regions = new HashSet<Region>();

            for (var y = 0; y < map.length; y++) {
                for (var x = 0; x < map[y].length; x++) {

                    Queue<Location> queue = new LinkedList<>();
                    queue.add(new Location(x, y));

                    while (!queue.isEmpty()) {
                        var current = queue.poll();

                        if (visited.contains(current)) {
                            continue;
                        }

                        visited.add(current);

                        if (region == null) {
                            region = Region.from(map, current);
                        } else {
                            region = region.with(current);
                        }

                        var gardenNeighbours = current.neighbours()
                                .stream()
                                .filter(l -> l.y >= 0 && l.y < map.length && l.x >= 0 && l.x < map[l.y].length)
                                .filter(l -> map[l.y][l.x] == map[current.y][current.x])
                                .toList();

                        queue.addAll(gardenNeighbours);
                    }

                    if (region != null) {
                        regions.add(region);
                        region = null;
                    }
                }
            }

            return new Garden(regions);
        }

        private static Set<Region> merge(HashSet<Region> regions) {
            var mergedRegions = new HashSet<Region>();

            for (var region : regions) {
                var mergeCandidate = mergedRegions.stream().filter(region::couldFuseWith).findFirst();

                if (mergeCandidate.isPresent()) {
                    var regionToMerge = mergeCandidate.get();
                    mergedRegions.remove(regionToMerge);
                    mergedRegions.add(regionToMerge.merge(region));
                } else {
                    mergedRegions.add(region);
                }
            }

            return mergedRegions;
        }

        public long totalPrice() {
            return regions.stream().mapToLong(Region::price).sum();
        }
    }

    private record Location(int x, int y) {
        public Collection<Location> neighbours(char[][] map) {
            return neighbours()
                    .stream()
                    .filter(l -> l.y >= 0 && l.y < map.length && l.x >= 0 && l.x < map[y].length)
                    .toList();
        }

        public Collection<Location> neighbours() {
            int[][] directions = {{0, -1}, {0, 1}, {1, -0}, {-1, 0}};

            return Arrays.stream(directions)
                    .map(d -> new Location(x + d[0], y + d[1]))
                    .toList();
        }
    }

    private record Region(char plantLabel, Set<Location> areas) {
        private boolean shouldContain(char[][] map, Location location) {
            var candidatePlantLabel = map[location.y][location.x];
            if (candidatePlantLabel != plantLabel) {
                return false;
            }

            return location.neighbours(map).stream().anyMatch(areas::contains);
        }

        private static Region from(char[][] map, Location location) {
            return new Region(map[location.y][location.x], Set.of(location));
        }

        private Region with(Location location) {
            var newLocations = new HashSet<>(areas);
            newLocations.add(location);
            return new Region(plantLabel, newLocations);
        }

        public long price() {
            System.out.println("Region with " + plantLabel + " plant has a price of " + areas.size() + " * " + perimeter() + " = " + (areas.size() * perimeter()));
            return areas.size() * perimeter();
        }

        private long perimeter() {
            return areas.stream()
                    .mapToLong(area -> 4L - area.neighbours().stream().filter(areas::contains).count())
                    .sum();
        }

        public Region merge(Region region) {
            assert plantLabel == region.plantLabel;
            var mergedAreas = new HashSet<Location>();
            mergedAreas.addAll(areas);
            mergedAreas.addAll(region.areas);
            return new Region(plantLabel, mergedAreas);
        }

        public boolean couldFuseWith(Region region) {
            return plantLabel == region.plantLabel &&
                    region.touches(this);
        }

        private boolean touches(Region region) {
            return areas.stream().flatMap(r -> r.neighbours().stream()).anyMatch(region.areas::contains);
        }
    }
}
