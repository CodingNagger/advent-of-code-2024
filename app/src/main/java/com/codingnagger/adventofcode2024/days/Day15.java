package com.codingnagger.adventofcode2024.days;

import java.util.*;
import java.util.function.Function;

public class Day15 implements Day {
    @Override
    public String partOne(List<String> input) {
        return Warehouse.parse(input).boxes().stream().mapToLong(Location::coordinates).sum() + "";
    }

    @Override
    public String partTwo(List<String> input) {
        return BigBoxWarehouse.parse(input).boxes().stream().map(BigBox::left).mapToLong(Location::coordinates).sum() + "";
    }

    private record BigBoxWarehouse(Location bottomRight, Location robot, Set<BigBox> boxes, Set<Location> walls) {
        public static BigBoxWarehouse parse(List<String> input) {
            var splitIndex = input.indexOf("");

            var mapInput = input.subList(0, splitIndex);
            var directions = String.join("", input.subList(splitIndex + 1, input.size())).toCharArray();
            var map = mapInput.stream().map(String::toCharArray).toArray(char[][]::new);

            Location robot = null;
            var walls = new HashSet<Location>();
            var boxes = new HashSet<BigBox>();

            for (var y = 0; y < map.length; y++) {
                for (var x = 0; x < map[y].length; x++) {
                    switch (map[y][x]) {
                        case '#':
                            walls.add(new Location(2 * x, y));
                            walls.add(new Location(2 * x + 1, y));
                            break;
                        case 'O':
                            boxes.add(new BigBox(
                                    new Location(2 * x, y),
                                    new Location(2 * x + 1, y)
                            ));
                            break;
                        case '@':
                            robot = new Location(2 * x, y);
                            break;
                    }
                }
            }

            return new BigBoxWarehouse(new Location(2 * map[0].length, map.length), robot, boxes, walls).navigateRobot(directions);
        }

        private BigBoxWarehouse navigateRobot(char[] directions) {
            var cursor = this;
            cursor.print();

            for (var direction : directions) {
                cursor = switch (direction) {
                    case '^' -> cursor.navigateRobot(Location::moveUp, BigBox::moveUp);
                    case '>' -> cursor.navigateRobot(Location::moveRight, BigBox::moveRight);
                    case 'v' -> cursor.navigateRobot(Location::moveDown, BigBox::moveDown);
                    case '<' -> cursor.navigateRobot(Location::moveLeft, BigBox::moveLeft);
                    default -> throw new IllegalStateException("Unexpected value: " + direction);
                };

                cursor.print();
            }

            return cursor;
        }

        private void print() {
            var lefts = boxes.stream().map(b -> b.left).toList();
            var rights = boxes.stream().map(b -> b.right).toList();

            for (var y = 0; y < bottomRight.y; y++) {
                for (var x = 0; x < bottomRight.x; x++) {
                    var location = new Location(x, y);
                    if (lefts.contains(location)) {
                        System.out.print("[");
                    } else if (rights.contains(location)) {
                        System.out.print("]");
                    } else if (walls.contains(location)) {
                        System.out.print("#");
                    } else if (robot.equals(location)) {
                        System.out.print("@");
                    } else {
                        System.out.print(".");
                    }
                }
                System.out.println();
            }
            System.out.println();
        }

        private BigBoxWarehouse navigateRobot(Function<Location, Location> robotMove, Function<BigBox, BigBox> boxMove) {
            var cursor = robotMove.apply(robot);
            var boxesToMove = new HashSet<BigBox>();

            Queue<Location> queue = new LinkedList<>();
            queue.add(cursor);

            var visited = new HashSet<Location>();

            while (!queue.isEmpty()) {
                var current = queue.poll();

                if (visited.contains(current)) {
                    continue;
                }

                visited.add(current);

                if (walls.contains(current)) {
                    return this;
                }

                var potentialBoxes = boxes.stream()
                        .filter(b -> b.left.equals(current) || b.right.equals(current))
                        .toList();

                for (var box : potentialBoxes) {
                    if (boxesToMove.contains(box)) {
                        continue;
                    }

                    boxesToMove.add(box);

                    var potentialMove = boxMove.apply(box);

                    queue.add(potentialMove.left);
                    queue.add(potentialMove.right);
                }
            }

            var newBoxes = replaceOldBoxes(boxesToMove, boxMove);

            return new BigBoxWarehouse(bottomRight, robotMove.apply(robot), newBoxes, walls);
        }

        private HashSet<BigBox> replaceOldBoxes(Set<BigBox> boxesToMove, Function<BigBox, BigBox> move) {
            var newBoxes = new HashSet<>(boxes);
            boxesToMove.forEach(newBoxes::remove);

            var movedBoxes = boxesToMove.stream().map(move).toList();
            newBoxes.addAll(movedBoxes);
            return newBoxes;
        }
    }

    private record BigBox(Location left, Location right) {
        public BigBox moveUp() {
            return new BigBox(left.moveUp(), right.moveUp());
        }

        public BigBox moveRight() {
            return new BigBox(left.moveRight(), right.moveRight());
        }

        public BigBox moveDown() {
            return new BigBox(left.moveDown(), right.moveDown());
        }

        public BigBox moveLeft() {
            return new BigBox(left.moveLeft(), right.moveLeft());
        }
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
