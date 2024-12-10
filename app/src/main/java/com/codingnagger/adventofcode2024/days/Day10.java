package com.codingnagger.adventofcode2024.days;

import java.util.*;

public class Day10 implements Day {
    @Override
    public String partOne(List<String> input) {
        return TopographicMap.parse(input).sumTrailheadScores() + "";
    }

    @Override
    public String partTwo(List<String> input) {
        return TopographicMap.parse(input).sumTrailheadRatings() + "";
    }

    private record TopographicMap(int[][] map) {
        static TopographicMap parse(List<String> input) {
            return new TopographicMap(
                    input.stream()
                            .map(line -> line.chars().map(Character::getNumericValue).toArray())
                            .toArray(int[][]::new)
            );
        }

        public long sumTrailheadScores() {
            return findTrailheads().stream()
                    .mapToLong(this::trailheadScore)
                    .sum();
        }

        private long trailheadScore(MappedLocation trailhead) {
            var queue = new PriorityQueue<MappedLocation>();
            queue.add(trailhead);

            var visited = new HashSet<MappedLocation>();

            while (!queue.isEmpty()) {
                var cursor = queue.poll();

                if (visited.contains(cursor)) {
                    continue;
                }

                visited.add(cursor);

                queue.addAll(neighbours(cursor));
            }

            return visited.stream().filter(v -> v.height == 9).count();
        }

        private Collection<MappedLocation> neighbours(MappedLocation cursor) {
            int[][] directions = {{0, -1}, {0, 1}, {1, -0}, {-1, 0}};

            return Arrays.stream(directions)
                    .flatMap(d -> move(cursor, d).stream())
                    .toList();
        }

        private Optional<MappedLocation> move(MappedLocation location, int[] direction) {
            var newX = location.location().x() + direction[0];
            var newY = location.location().y() + direction[1];

            if (newY < 0 || newY >= map.length) {
                return Optional.empty();
            }

            if (newX < 0 || newX >= map[newY].length) {
                return Optional.empty();
            }

            var height = map[newY][newX];

            if (height - 1 != location.height) {
                return Optional.empty();
            }

            return Optional.of(new MappedLocation(height, new Location(newX, newY)));
        }

        private List<MappedLocation> findTrailheads() {
            var trailheads = new ArrayList<MappedLocation>();

            for (var y = 0; y < map.length; y++) {
                for (var x = 0; x < map[y].length; x++) {
                    if (map[y][x] == 0L) {
                        trailheads.add(MappedLocation.trailhead(x, y));
                    }
                }
            }

            return trailheads;
        }

        public long sumTrailheadRatings() {
            return findTrailheads().stream()
                    .mapToLong(this::trailheadRating)
                    .sum();
        }

        private long trailheadRating(MappedLocation trailhead) {
            var queue = new PriorityQueue<LocationHistory>();
            queue.add(LocationHistory.start(trailhead));

            var visitedTrails = new HashSet<LocationHistory>();

            while (!queue.isEmpty()) {
                var cursor = queue.poll();

                for (var neighbor : neighbours(cursor.current)) {
                    var next = cursor.next(neighbor);

                    if (visitedTrails.contains(next)) {
                        continue;
                    }

                    visitedTrails.add(next);

                    queue.add(next);
                }
            }

            for (var v : visitedTrails.stream().distinct().toList()) {
                if (v.current.height != 9) continue;

                System.out.println(v.fullHistory());
            }

            return visitedTrails.stream()
                    .filter(v -> v.current.height == 9)
                    .distinct()
                    .count();
        }
    }

    private record LocationHistory(MappedLocation current,
                                   List<MappedLocation> history) implements Comparable<LocationHistory> {
        public static LocationHistory start(MappedLocation trailhead) {
            return new LocationHistory(trailhead, List.of());
        }

        @Override
        public int compareTo(LocationHistory o) {
            return current.compareTo(o.current);
        }

        public LocationHistory next(MappedLocation location) {
            var updatedHistory = new ArrayList<>(history);
            updatedHistory.add(current);
            return new LocationHistory(location, updatedHistory);
        }

        public String stringHistory() {
            return String.join(",", history().stream().map(String::valueOf).toList());
        }

        public String fullHistory() {
            return current + " - " + stringHistory();
        }
    }

    private record MappedLocation(int height, Location location) implements Comparable<MappedLocation> {
        public static MappedLocation trailhead(int x, int y) {
            return new MappedLocation(0, new Location(x, y));
        }

        @Override
        public int compareTo(MappedLocation o) {
            return Integer.compare(height, o.height);
        }
    }

    private record Location(int x, int y) {
    }
}
