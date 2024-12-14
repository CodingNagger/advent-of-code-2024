package com.codingnagger.adventofcode2024.days;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14 implements Day {
    private final static Pattern pattern = Pattern.compile("p=(-?[0-9]+),(-?[0-9]+) v=(-?[0-9]+),(-?[0-9]+)");
    private final BathroomBoundaries boundaries;

    public Day14() {
        this(101, 103);
    }

    public Day14(int width, int height) {
        boundaries = new BathroomBoundaries(width, height);
    }

    @Override
    public String partOne(List<String> input) {
        var bathroom = Bathroom.from(boundaries, input);
        var bathroomAfterHundredSeconds = bathroom.tick(100);

        return bathroomAfterHundredSeconds.safetyFactor() + "";
    }

    @Override
    public String partTwo(List<String> input) {
        var bathroom = Bathroom.from(boundaries, input);
        var bathroomWithChristmasTree = bathroom.tickUntilChristmasTree();

        return "";
    }

    private record Bathroom(BathroomBoundaries boundaries, List<Robot> robots) {
        public static Bathroom from(BathroomBoundaries boundaries, List<String> input) {
            var robots = input.stream()
                    .map(String::trim)
                    .map(pattern::matcher)
                    .filter(Matcher::matches)
                    .map(Robot::from)
                    .toList();

            assert robots.size() == input.size();

            return new Bathroom(boundaries, robots);
        }

        public void print() {
            System.out.println();
            System.out.println("==========================================");
            System.out.println();
            for (var y = 0; y < boundaries.height; y++) {
                for (var x = 0; x < boundaries.width; x++) {
                    var currentLocation = new Location(x, y);
                    var robotCount = robotCountAt(currentLocation);

                    if (robotCount > 0) {
                        System.out.print(robotCount);
                    } else {
                        System.out.print('.');
                    }

                }
                System.out.println();
            }
        }

        private long robotCountAt(Location currentLocation) {
            return robots.stream().filter(robot -> robot.location.equals(currentLocation)).count();
        }

        public Bathroom tick(int seconds) {
            var result = this;
            var countdown = seconds;

            while (countdown > 0) {
                result = result.tick();
                countdown--;
            }

            result.print();

            return result;
        }

        private Bathroom tick() {
            return new Bathroom(boundaries, robots.stream().map(r -> r.tick(boundaries)).toList());
        }

        public long safetyFactor() {
            return topLeftQuadrantRobotCount() *
                    topRightQuadrantRobotCount() *
                    bottomLeftQuadrantRobotCount() *
                    bottomRightQuadrantRobotCount();
        }

        private long bottomRightQuadrantRobotCount() {
            return countRobotsBetween(new Location((boundaries().width / 2) + 1, (boundaries().height / 2) + 1), new Location(boundaries().width - 1, boundaries().height - 1));
        }

        private long bottomLeftQuadrantRobotCount() {
            return countRobotsBetween(new Location(0, (boundaries().height / 2) + 1), new Location((boundaries().width / 2) - 1, boundaries().height - 1));
        }

        private long topRightQuadrantRobotCount() {
            return countRobotsBetween(new Location((boundaries().width / 2) + 1, 0), new Location(boundaries().width - 1, (boundaries().height / 2) - 1));
        }

        private long topLeftQuadrantRobotCount() {
            return countRobotsBetween(new Location(0, 0), new Location((boundaries().width / 2) - 1, (boundaries().height / 2) - 1));
        }

        private long countRobotsBetween(Location topLeft, Location bottomRight) {
            return robots.stream()
                    .filter(robot ->
                            robot.location.x >= topLeft.x &&
                                    robot.location.y >= topLeft.y &&
                                    robot.location.x <= bottomRight.x &&
                                    robot.location.y <= bottomRight.y
                    ).count();
        }

        public Bathroom tickUntilChristmasTree() {
            var bathrooms = new HashMap<Bathroom, Long>();
            var iterations = new HashMap<Bathroom, Integer>();
            var cursor = this;
            for (var count = 0; count < boundaries().width * boundaries.height; count++) {
                bathrooms.put(cursor, cursor.safetyFactor());
                iterations.put(cursor, count);
                cursor = cursor.tick();
            }

            var list = bathrooms.entrySet().stream().sorted((o1, o2) -> -Long.compare(o2.getValue(), o1.getValue())).toList();

            for (var i = 0; i < 20; i++) {
                var bathroom = list.get(i).getKey();
                var iteration = iterations.get(bathroom);

                System.out.print("Bathroom after iteration: " + iteration);
                bathroom.print();
            }

            return cursor;
        }
    }

    private record BathroomBoundaries(int width, int height) {
    }

    private record Location(int x, int y) {
    }

    private record Velocity(int x, int y) {
    }

    private record Robot(Location location, Velocity velocity) {
        public static Robot from(Matcher matcher) {
            return new Robot(
                    new Location(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))),
                    new Velocity(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)))
            );
        }

        public Robot tick(BathroomBoundaries boundaries) {
            return new Robot(
                    new Location(newX(boundaries), newY(boundaries)),
                    velocity
            );
        }

        private int newY(BathroomBoundaries boundaries) {
            var nextY = location().y + velocity.y;

            if (nextY >= boundaries.height) {
                nextY = nextY - boundaries.height;
            } else if (nextY < 0) {
                nextY = nextY + boundaries.height;
            }

            return nextY;
        }

        private int newX(BathroomBoundaries boundaries) {
            var nextX = location().x + velocity.x;

            if (nextX >= boundaries.width) {
                nextX = nextX - boundaries.width;
            } else if (nextX < 0) {
                nextX = nextX + boundaries.width;
            }

            return nextX;
        }
    }
}
