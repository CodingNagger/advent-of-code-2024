package com.codingnagger.adventofcode2024.days;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class Day15 implements Day {
    @Override
    public String partOne(List<String> input) {
        return Warehouse.parse(input).boxes().stream().mapToLong(Location::coordinates).sum() + "";
    }

    @Override
    public String partTwo(List<String> input) {
        return "";
    }

    private record Warehouse(Location robot, Set<Location> boxes, Set<Location> walls) {
        public static Warehouse parse(List<String> input) {
            var splitIndex = input.indexOf("");

            var mapInput = input.subList(0, splitIndex);
            var directions = String.join("", input.subList(splitIndex + 1, input.size())).toCharArray();
            var map = mapInput.stream().map(String::toCharArray).toArray(char[][]::new);

            Location robot = null;
            var walls = new HashSet<Location>();
            var boxes = new HashSet<Location>();

            for (var y = 0; y < map.length; y++) {
                for (var x = 0; x < map[y].length; x++) {
                    switch (map[y][x]) {
                        case '#':
                            walls.add(new Location(x, y));
                            break;
                        case 'O':
                            boxes.add(new Location(x, y));
                            break;
                        case '@':
                            robot = new Location(x, y);
                            break;
                    }
                }
            }

            return new Warehouse(robot, boxes, walls).navigateRobot(directions);
        }

        private Warehouse navigateRobot(char[] directions) {
            var cursor = this;

            for (var direction : directions) {
                cursor = switch (direction) {
                    case '^' -> cursor.navigateRobot(Location::moveUp);
                    case '>' -> cursor.navigateRobot(Location::moveRight);
                    case 'v' -> cursor.navigateRobot(Location::moveDown);
                    case '<' -> cursor.navigateRobot(Location::moveLeft);
                    default -> throw new IllegalStateException("Unexpected value: " + direction);
                };
            }

            return cursor;
        }

        private Warehouse navigateRobot(Function<Location, Location> move) {
            var cursor = move.apply(robot);
            var boxesToMove = new ArrayList<Location>();

            while (boxes.contains(cursor)) {
                boxesToMove.add(cursor);
                cursor = move.apply(cursor);
            }

            if (walls.contains(cursor)) {
                return this;
            }

            var newBoxes = replaceOldBoxes(boxesToMove, move);

            return new Warehouse(move.apply(robot), newBoxes, walls);
        }

        private HashSet<Location> replaceOldBoxes(ArrayList<Location> boxesToMove, Function<Location, Location> move) {
            var newBoxes = new HashSet<>(boxes);
            boxesToMove.forEach(newBoxes::remove);

            var movedBoxes = boxesToMove.stream().map(move).toList();
            newBoxes.addAll(movedBoxes);
            return newBoxes;
        }
    }

    private record Location(int x, int y) {
        public long coordinates() {
            return y * 100L + x;
        }

        public Location moveUp() {
            return new Location(x, y - 1);
        }

        public Location moveRight() {
            return new Location(x + 1, y);
        }

        public Location moveDown() {
            return new Location(x, y + 1);
        }

        public Location moveLeft() {
            return new Location(x - 1, y);
        }
    }
}
