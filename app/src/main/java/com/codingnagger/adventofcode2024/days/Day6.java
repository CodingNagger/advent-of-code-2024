package com.codingnagger.adventofcode2024.days;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day6 implements Day {
    @Override
    public String partOne(List<String> input) {
        return new MappedArea(input.stream().map(String::toCharArray).toArray(char[][]::new))
                .distinctVisitedPositionsCount() + "";
    }

    @Override
    public String partTwo(List<String> input) {
        return new MappedArea(input.stream().map(String::toCharArray).toArray(char[][]::new))
                .potentialObstaclesCount() + "";
    }

    enum Direction {UP, DOWN, LEFT, RIGHT}

    interface Location {
        int x();

        int y();
    }

    record Guard(Direction direction, int x, int y) implements Location {
        public Guard next() {
            return switch (direction) {
                case UP -> new Guard(direction, x, y - 1);
                case DOWN -> new Guard(direction, x, y + 1);
                case LEFT -> new Guard(direction, x - 1, y);
                case RIGHT -> new Guard(direction, x + 1, y);
            };
        }

        public Guard turnRight() {
            return switch (direction) {
                case UP -> new Guard(Direction.RIGHT, x, y);
                case RIGHT -> new Guard(Direction.DOWN, x, y);
                case DOWN -> new Guard(Direction.LEFT, x, y);
                case LEFT -> new Guard(Direction.UP, x, y);
            };
        }

        public Point location() {
            return new Point(x, y);
        }
    }

    record Point(int x, int y) implements Location {
    }

    record MappedArea(Guard guard, int yBound, int xBound, Set<Location> obstacles) {
        public MappedArea(char[][] tiles) {
            this(findGuard(tiles), tiles.length, tiles[0].length, findObstacles(tiles));
        }

        private static Set<Location> findObstacles(char[][] tiles) {
            var result = new ArrayList<Location>();

            for (var y = 0; y < tiles.length; y++) {
                for (var x = 0; x < tiles[y].length; x++) {
                    if (tiles[y][x] == '#') {
                        result.add(new Point(x, y));
                    }
                }
            }

            return Set.copyOf(result);
        }

        public long distinctVisitedPositionsCount() {
            var visited = new HashSet<Location>();
            var cursor = guard;

            while (withinBounds(cursor)) {
                visited.add(new Point(cursor.x, cursor.y));
                cursor = moveGuard(cursor);
            }

            print(visited);

            return visited.size();
        }

        public long potentialObstaclesCount() {
            var potentialObstacles = new HashSet<Point>();

            for (var y = 0; y < yBound(); y++) {
                for (var x = 0; x < xBound(); x++) {
                    var point = new Point(x, y);
                    if (!guard.location().equals(point) && !obstacles.contains(point)) {
                        potentialObstacles.add(point);
                    }
                }
            }

            return potentialObstacles.stream().map(o -> {
                        var newObstacles = new ArrayList<Location>();
                        newObstacles.add(o);
                        newObstacles.addAll(obstacles);
                        return newObstacles;
                    })
                    .map(newObstacles -> new MappedArea(guard, yBound, xBound, new HashSet<>(newObstacles)))
                    .filter(MappedArea::didEnterLoop)
                    .count();
        }

        private boolean didEnterLoop() {
            var hare = guard;
            var tortoise = guard;

            while (withinBounds(hare) && withinBounds(tortoise)) {
                tortoise = moveGuard(tortoise);
                hare = moveGuard(moveGuard(hare));

                if (hare.x == tortoise.x && hare.y == tortoise.y && hare.direction() == tortoise.direction()) {
                    return true;
                }
            }

            return false;
        }

        private Guard moveGuard(Guard guard) {
            var nextPosition = guard.next();

            if (isObstacle(nextPosition)) {
                guard = guard.turnRight();
            } else {
                guard = guard.next();
            }

            return guard;
        }

        private void print(HashSet<Location> visited) {
            System.out.println();
            for (var y = 0; y < yBound(); y++) {
                for (var x = 0; x < xBound(); x++) {
                    if (visited.contains(new Point(x, y))) {
                        System.out.print('X');
                    } else if (isObstacle(new Point(x, y))) {
                        System.out.print('#');
                    } else {
                        System.out.print('.');
                    }
                }
                System.out.println();
            }
            System.out.println();
        }

        private void print(Guard guard) {
            System.out.println();
            for (var y = 0; y < yBound(); y++) {
                for (var x = 0; x < yBound(); x++) {
                    if (isObstacle(new Point(x, y))) {
                        System.out.print('#');
                    } else if (guard.x == x && guard.y == y) {
                        System.out.print('G');
                    } else {
                        System.out.print('.');
                    }
                }
                System.out.println();
            }
            System.out.println();
        }

        private boolean isObstacle(Location location) {
            return withinBounds(location) && obstacleExistsAt(location);
        }

        private boolean obstacleExistsAt(Location location) {
            return obstacles.contains(new Point(location.x(), location.y()));
        }

        private boolean withinBounds(Location location) {
            return location.y() >= 0 && location.y() < yBound() && location.x() >= 0 && location.x() < xBound();
        }

        private static Guard findGuard(char[][] tiles) {
            for (int y = 0; y < tiles.length; y++) {
                for (int x = 0; x < tiles[y].length; x++) {
                    if (tiles[y][x] == '^') {
                        return new Guard(Direction.UP, x, y);
                    }
                }
            }

            throw new IllegalStateException("Guard position not set");
        }
    }
}
