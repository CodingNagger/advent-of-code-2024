package com.codingnagger.adventofcode2024.days;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day18 implements Day {
    private final int maxXY;
    private final int fallenBytesToSimulate;

    public Day18(int maxXY, int fallenBytesToSimulate) {
        this.maxXY = maxXY;
        this.fallenBytesToSimulate = fallenBytesToSimulate;
    }

    public Day18() {
        this(70, 1024);
    }

    @Override
    public String partOne(List<String> input) {
        var tickedLocations = IntStream.range(0, fallenBytesToSimulate)
                .mapToObj(tick -> new TimedLocation(tick, Location.parse(input.get(tick))))
                .toList();
        var corruptedLocations = tickedLocations.stream().map(TimedLocation::location).collect(Collectors.toSet());

        return "" + shortedDistance(new Location(0, 0), new Location(maxXY, maxXY), corruptedLocations);
    }

    @Override
    public String partTwo(List<String> input) {
        return "";
    }


    private int shortedDistance(Location start, Location destination, Set<Location> corruptedLocations) {
        var lowestDistanceTo = new HashMap<Location, Integer>();

        Queue<LocationWithSteps> queue = new LinkedList<>();
        queue.add(new LocationWithSteps(start, 0));

        while (!queue.isEmpty()) {
            var current = queue.poll();

            if (lowestDistanceTo.containsKey(current.location) &&
                    (lowestDistanceTo.get(current.location) < current.steps ||
                            lowestDistanceTo.values().stream().max(Integer::compareTo).orElseThrow() < current.steps + 1)
            ) {
                continue;
            }

            lowestDistanceTo.put(current.location, current.steps);

            queue.addAll(neighbours(current, corruptedLocations));
        }

        return lowestDistanceTo.get(destination);
    }

    private Collection<LocationWithSteps> neighbours(LocationWithSteps location, Set<Location> corrupted) {
        return uncorruptedNeighbours(location.location, corrupted)
                .stream()
                .map(l -> new LocationWithSteps(l, location.steps + 1))
                .toList();
    }

    private Collection<Location> uncorruptedNeighbours(Location location, Set<Location> corrupted) {
        int[][] directions = {{0, -1}, {0, 1}, {1, -0}, {-1, 0}};

        return Arrays.stream(directions)
                .map(d -> new Location(d[0] + location.x(), d[1] + location.y()))
                .filter(l -> !corrupted.contains(l))
                .filter(this::withinBounds)
                .toList();
    }

    private boolean withinBounds(Location location) {
        return 0 <= location.x() && location.x() <= maxXY &&
                0 <= location.y() && location.y() <= maxXY;
    }


    private record LocationWithSteps(Location location, int steps) {
        public int x() {
            return location.x;
        }

        public int y() {
            return location.y;
        }
    }

    private record Location(int x, int y) {
        public static Location parse(String line) {
            var parts = line.split(",");
            return new Location(Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim()));
        }
    }

    private record TimedLocation(int tick, Location location) {
        public int x() {
            return location.x;
        }

        public int y() {
            return location.y;
        }
    }
}
