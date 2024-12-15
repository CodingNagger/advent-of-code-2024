package com.codingnagger.adventofcode2024.days;

import java.util.*;

public class Day12 implements Day {
    @Override
    public String partOne(List<String> input) {
        prettyMapPrint(input);
        return Garden.parse(input).totalPrice() + "";
    }

    @Override
    public String partTwo(List<String> input) {
        return Garden.parse(input).bulkDiscountPrice() + "";
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

        public long totalPrice() {
            return regions.stream().mapToLong(Region::price).sum();
        }

        public long bulkDiscountPrice() {
            return regions.stream().mapToLong(Region::bulkDiscountPrice).sum();
        }
    }

    private record Location(int x, int y) {
        public Collection<Location> neighbours() {
            int[][] directions = {{0, -1}, {0, 1}, {1, -0}, {-1, 0}};

            return Arrays.stream(directions)
                    .map(d -> new Location(x + d[0], y + d[1]))
                    .toList();
        }

        public Location top() {
            return new Location(x, y - 1);
        }

        public Location left() {
            return new Location(x - 1, y);
        }

        public Location right() {
            return new Location(x + 1, y);
        }

        public Location bottom() {
            return new Location(x, y + 1);
        }

        public Location topLeft() {
            return new Location(x - 1, y - 1);
        }

        public Location topRight() {
            return new Location(x + 1, y - 1);
        }

        public Location bottomRight() {
            return new Location(x + 1, y + 1);
        }

        public Location bottomLeft() {
            return new Location(x - 1, y + 1);
        }
    }

    private record Region(char plantLabel, Set<Location> areas) {
        private static Region from(char[][] map, Location location) {
            return new Region(map[location.y][location.x], Set.of(location));
        }

        private Region with(Location location) {
            var newLocations = new HashSet<>(areas);
            newLocations.add(location);
            return new Region(plantLabel, newLocations);
        }

        public long price() {
            return areas.size() * perimeter();
        }

        private long perimeter() {
            return areas.stream()
                    .mapToLong(area -> 4L - area.neighbours().stream().filter(areas::contains).count())
                    .sum();
        }

        public long bulkDiscountPrice() {
            System.out.println("Region with " + plantLabel + " plant has a price of " + areas.size() + " * " + sideCount() + " = " + (areas.size() * sideCount()));
            return areas.size() * sideCount();
        }

        private long sideCount() {
            var result = 0L;

            result += topLeftOuterTurnCount();
            result += topRightOuterTurnCount();
            result += bottomRightOuterTurnCount();
            result += bottomLeftOuterTurnCount();

            result += topLeftInnerTurnCount();
            result += topRightInnerTurnCount();
            result += bottomRightInnerTurnCount();
            result += bottomLeftInnerTurnCount();

            return result;
        }

        private long bottomLeftInnerTurnCount() {
            return areas.stream().filter(a -> areas().contains(a.bottom()) && areas.contains(a.left()) && !areas.contains(a.bottomLeft())).count();
        }

        private long bottomRightInnerTurnCount() {
            return areas.stream().filter(a -> areas().contains(a.bottom()) && areas.contains(a.right()) && !areas.contains(a.bottomRight())).count();
        }

        private long topRightInnerTurnCount() {
            return areas.stream().filter(a -> areas().contains(a.top()) && areas.contains(a.right()) && !areas.contains(a.topRight())).count();
        }

        private long topLeftInnerTurnCount() {
            return areas.stream().filter(a -> areas().contains(a.top()) && areas.contains(a.left()) && !areas.contains(a.topLeft())).count();
        }

        // count border corners
        // #o
        // ## - count diagonal neighbors outside region
        //     ooo
        // ####n#o - topright, topleft, bottomright, bottoleft
        //   ##nno

        private long bottomLeftOuterTurnCount() {
            return areas.stream().filter(a -> !areas().contains(a.bottom()) && !areas.contains(a.left())).count();
        }

        private long bottomRightOuterTurnCount() {
            return areas.stream().filter(a -> !areas().contains(a.bottom()) && !areas.contains(a.right())).count();
        }

        private long topRightOuterTurnCount() {
            return areas.stream().filter(a -> !areas().contains(a.top()) && !areas.contains(a.right())).count();
        }

        private long topLeftOuterTurnCount() {
            return areas.stream().filter(a -> !areas().contains(a.top()) && !areas.contains(a.left())).count();
        }
    }
}
