package com.codingnagger.adventofcode2024.days;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;

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

    enum Obstacle {NONE, ADDED, DODGED}

    interface Location {
        int x();

        int y();

        boolean hasObstacle();
    }

    record Guard(Direction direction, int x, int y, Obstacle obstacle) implements Location {
        public Guard next() {
            return switch (direction) {
                case UP -> new Guard(direction, x, y - 1, obstacle);
                case DOWN -> new Guard(direction, x, y + 1, obstacle);
                case LEFT -> new Guard(direction, x - 1, y, obstacle);
                case RIGHT -> new Guard(direction, x + 1, y, obstacle);
            };
        }

        public Guard turnRight() {
            var newObstacle = obstacle == Obstacle.ADDED ? Obstacle.DODGED : obstacle;

            return switch (direction) {
                case UP -> new Guard(Direction.RIGHT, x, y, newObstacle);
                case RIGHT -> new Guard(Direction.DOWN, x, y, newObstacle);
                case DOWN -> new Guard(Direction.LEFT, x, y, newObstacle);
                case LEFT -> new Guard(Direction.UP, x, y, newObstacle);
            };
        }

        public Guard addObstacle() {
            if (obstacle != Obstacle.NONE) {
                throw new IllegalStateException("Obstacle cannot be added from " + obstacle);
            }

            return new Guard(direction, x, y, Obstacle.ADDED);
        }

        public boolean obstacleMet() {
            return obstacle != Obstacle.NONE;
        }

        @Override
        public boolean hasObstacle() {
            return obstacle == Obstacle.ADDED;
        }
    }

    record Point(int x, int y) implements Location {
        @Override
        public boolean hasObstacle() {
            return false;
        }
    }

    record MappedArea(char[][] tiles) {
        public long distinctVisitedPositionsCount() {
            var guard = findGuard();
            var visited = new HashSet<Point>();

            while (withinBounds(guard)) {
                visited.add(new Point(guard.x, guard.y));
                guard = moveGuard(guard);
            }

            print(visited);

            return visited.size();
        }

        public long potentialObstaclesCount() {
            var start = findGuard();
            var queue = new ArrayDeque<Guard>();
            queue.add(start);
            var result = -1;

            while (!queue.isEmpty()) {
                var guard = queue.poll();

                if (guard.x == start.x && guard.y == start.y && guard.direction == Direction.UP) {
                    result++;
                }

                if (!withinBounds(guard)) {
                    continue;
                }

                var nextPosition = guard.next();

                if (!isObstacle(nextPosition) && !guard.obstacleMet()) {
                    queue.add(guard.addObstacle());
                }

                queue.add(moveGuard(guard));
            }
            return result;
        }

        private Guard moveGuard(Guard guard) {
            var nextPosition = guard.next();

            if (isObstacle(nextPosition)) {
                guard = guard.turnRight();
            }

            guard = guard.next();
            return guard;
        }

        private void print(HashSet<Point> visited) {
            System.out.println();
            for (var y = 0; y < tiles.length; y++) {
                for (var x = 0; x < tiles.length; x++) {
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
            for (var y = 0; y < tiles.length; y++) {
                for (var x = 0; x < tiles.length; x++) {
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
            return withinBounds(location) &&
                    (tiles[location.y()][location.x()] == '#' || location.hasObstacle());
        }

        private boolean withinBounds(Location location) {
            return location.y() >= 0 && location.y() < tiles.length && location.x() >= 0 && location.x() < tiles[location.y()].length;
        }

        private Guard findGuard() {
            for (int y = 0; y < tiles.length; y++) {
                for (int x = 0; x < tiles[y].length; x++) {
                    if (tiles[y][x] == '^') {
                        return new Guard(Direction.UP, x, y, Obstacle.NONE);
                    }
                }
            }

            throw new IllegalStateException("Guard position not set");
        }


    }
}
