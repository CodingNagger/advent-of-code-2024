package com.codingnagger.adventofcode2024.days;

import java.util.*;
import java.util.stream.Collectors;

public class Day16 implements Day {
    @Override
    public String partOne(List<String> input) {
        return ReindeerMaze.parse(input).lowestScore() + "";
    }

    @Override
    public String partTwo(List<String> input) {
        return ReindeerMaze.parse(input).bestPathTileCount() + "";
    }

    private record ReindeerMaze(Location start, Location end, char[][] map) {
        public static ReindeerMaze parse(List<String> input) {
            var map = input.stream()
                    .map(String::toCharArray)
                    .toArray(char[][]::new);

            Location start = null, end = null;

            for (var y = 0; y < map.length; y++) {
                for (var x = 0; x < map[y].length; x++) {
                    if (map[y][x] == 'S') {
                        start = new Location(x, y);
                    } else if (map[y][x] == 'E') {
                        end = new Location(x, y);
                    }
                }
            }

            if (start == null || end == null) {
                throw new IllegalStateException("Could not find start " + start + " or end " + end);
            }

            return new ReindeerMaze(start, end, map);
        }

        public long lowestScore() {
            var lowestScoreAtLocation = new HashMap<DirectedLocation, Long>();

            var visited = new HashSet<ScoredLocation>();

            Queue<ScoredLocation> queue = new LinkedList<>();
            queue.add(new ScoredLocation(new DirectedLocation(Direction.EAST, start), 0L, List.of()));

            while (!queue.isEmpty()) {
                var current = queue.poll();

                if (lowestScoreAtLocation.containsKey(current.directedLocation) &&
                        lowestScoreAtLocation.get(current.directedLocation) < current.score) {
                    continue;
                }

                lowestScoreAtLocation.put(current.directedLocation, current.score);

                visited.add(current);

                queue.addAll(next(current));
            }

            var lowestScore = lowestScoreAtLocation.entrySet().stream()
                    .filter(e -> e.getKey().location.equals(end))
                    .mapToLong(Map.Entry::getValue)
                    .min()
                    .orElseThrow();

            var arrived = visited.stream()
                    .filter(v -> v.directedLocation.location.equals(end))
                    .filter(v -> v.score == lowestScore)
                    .toList();

            var visitedLocations = arrived
                    .stream()
                    .map(ScoredLocation::historyPlusSelf)
                    .flatMap(List::stream)
                    .collect(Collectors.toSet());

//            print(visitedLocations);

            return lowestScore;
        }

        public long bestPathTileCount() {
            var lowestScoreAtLocation = new HashMap<DirectedLocation, Long>();

            var visited = new HashSet<ScoredLocation>();

            Queue<ScoredLocation> queue = new LinkedList<>();
            queue.add(new ScoredLocation(new DirectedLocation(Direction.EAST, start), 0L, List.of()));

            while (!queue.isEmpty()) {
                var current = queue.poll();

                if (lowestScoreAtLocation.containsKey(current.directedLocation) &&
                        lowestScoreAtLocation.get(current.directedLocation) < current.score) {
                    continue;
                }

                lowestScoreAtLocation.put(current.directedLocation, current.score);

                visited.add(current);

                queue.addAll(next(current));
            }

            var lowestScore = lowestScoreAtLocation.entrySet().stream()
                    .filter(e -> e.getKey().location.equals(end))
                    .mapToLong(Map.Entry::getValue)
                    .min()
                    .orElseThrow();

            var arrived = visited.stream()
                    .filter(v -> v.directedLocation.location.equals(end))
                    .filter(v -> v.score == lowestScore)
                    .toList();

            var visitedLocations = arrived
                    .stream()
                    .map(ScoredLocation::historyPlusSelf)
                    .flatMap(List::stream)
                    .collect(Collectors.toSet());

//            print(visitedLocations);

            return visitedLocations.size();
        }

//        private void print(Set<Location> visitedLocations) {
//            for (var y = 0; y < map.length; y++) {
//                for (var x = 0; x < map[y].length; x++) {
//                    if (visitedLocations.contains(map[y][x])) {
//                        System.out.print("O");
//                    } else {
//                        System.out.print(map[y][x]);
//                    }
//                }
//
//                System.out.println();
//            }
//        }

        private Collection<ScoredLocation> next(ScoredLocation location) {
            var neighbours = switch (location.direction()) {
                case NORTH -> location.neighboursFacingNorth();
                case SOUTH -> location.neighboursFacingSouth();
                case EAST -> location.neighboursFacingEast();
                case WEST -> location.neighboursFacingWest();
            };

            return neighbours.stream()
                    .filter(this::withinBounds)
                    .filter(this::notWall)
                    .toList();
        }

        private boolean notWall(ScoredLocation scoredLocation) {
            return map[scoredLocation.y()][scoredLocation.x()] != '#';
        }

        private boolean withinBounds(ScoredLocation scoredLocation) {
            return 0 <= scoredLocation.x() && scoredLocation.x() < map[0].length &&
                    0 <= scoredLocation.y() && scoredLocation.y() < map.length;
        }
    }

    private record ScoredLocation(DirectedLocation directedLocation, Long score, List<Location> history) {
        public Collection<ScoredLocation> neighboursFacingNorth() {
            return List.of(
                    new ScoredLocation(new DirectedLocation(directedLocation.direction, new Location(directedLocation.x(), directedLocation.y() - 1)), score + 1, historyPlusSelf()),
                    new ScoredLocation(new DirectedLocation(Direction.EAST, directedLocation.location), score + 1000, historyPlusSelf()),
                    new ScoredLocation(new DirectedLocation(Direction.WEST, directedLocation.location), score + 1000, historyPlusSelf())
            );
        }

        private List<Location> historyPlusSelf() {
            var newHistory = new ArrayList<Location>(history);
            newHistory.add(directedLocation.location);
            return newHistory;
        }

        public Collection<ScoredLocation> neighboursFacingSouth() {
            return List.of(
                    new ScoredLocation(new DirectedLocation(directedLocation.direction, new Location(directedLocation.x(), directedLocation.y() + 1)), score + 1, historyPlusSelf()),
                    new ScoredLocation(new DirectedLocation(Direction.EAST, directedLocation.location), score + 1000, historyPlusSelf()),
                    new ScoredLocation(new DirectedLocation(Direction.WEST, directedLocation.location), score + 1000, historyPlusSelf())
            );
        }

        public Collection<ScoredLocation> neighboursFacingEast() {
            return List.of(
                    new ScoredLocation(new DirectedLocation(directedLocation.direction, new Location(directedLocation.x() + 1, directedLocation.y())), score + 1, historyPlusSelf()),
                    new ScoredLocation(new DirectedLocation(Direction.NORTH, directedLocation.location), score + 1000, historyPlusSelf()),
                    new ScoredLocation(new DirectedLocation(Direction.SOUTH, directedLocation.location), score + 1000, historyPlusSelf())
            );
        }

        public Collection<ScoredLocation> neighboursFacingWest() {
            return List.of(
                    new ScoredLocation(new DirectedLocation(directedLocation.direction, new Location(directedLocation.x() - 1, directedLocation.y())), score + 1, historyPlusSelf()),
                    new ScoredLocation(new DirectedLocation(Direction.NORTH, directedLocation.location), score + 1000, historyPlusSelf()),
                    new ScoredLocation(new DirectedLocation(Direction.SOUTH, directedLocation.location), score + 1000, historyPlusSelf())
            );
        }

        public Direction direction() {
            return directedLocation.direction();
        }

        public int x() {
            return directedLocation.x();
        }

        public int y() {
            return directedLocation.y();
        }
    }

    private enum Direction {
        NORTH, SOUTH, EAST, WEST
    }

    private record DirectedLocation(Direction direction, Location location) {
        public int y() {
            return location.y();
        }

        public int x() {
            return location.x();
        }
    }

    private record Location(int x, int y) {
    }
}
