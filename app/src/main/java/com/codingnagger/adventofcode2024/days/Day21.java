package com.codingnagger.adventofcode2024.days;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day21 implements Day {
    private static final char GAP = ' ';
    public static final char ACTION = 'A';
    public static final char UP = '^';
    public static final char LEFT = '<';
    public static final char DOWN = 'v';
    public static final char RIGHT = '>';

    @Override
    public String partOne(List<String> input) {
        return input.stream()
                .map(ChainSolver::new)
                .mapToLong(ChainSolver::complexity)
                .sum() + "";
    }

    @Override
    public String partTwo(List<String> input) {
        return "";
    }

    record ChainSolver(String desiredCode) {
        public long complexity() {
            var shortestSequence = shortestSequenceOfPress();
            return numericPart() * shortestSequence.length();
        }

        public long numericPart() {
            return Long.parseLong(desiredCode.substring(0, desiredCode.length() - 1));
        }

        public String shortestSequenceOfPress() {
            var numericDoorKeypad = KeyPad.numeric();
            var directionalKeypad = KeyPad.directional();

            return Stream.of(desiredCode)
                    .map(numericDoorKeypad::shortestSequencesOfPress)
                    .map(directionalKeypad::shortestSequencesOfPress)
                    .map(directionalKeypad::shortestSequencesOfPress)
                    .flatMap(Collection::stream)
                    .findFirst()
                    .orElseThrow();
        }
    }

    private record KeyPad(char[][] keys) {
        public static KeyPad numeric() {
            return new KeyPad(new char[][]{
                    {'7', '8', '9'},
                    {'4', '5', '6'},
                    {'1', '2', '3'},
                    {GAP, '0', ACTION},
            });
        }

        public static KeyPad directional() {
            return new KeyPad(new char[][]{
                    {GAP, UP, ACTION},
                    {LEFT, DOWN, RIGHT},
            });
        }

        public Set<String> shortestSequencesOfPress(Set<String> candidates) {
            var sequences = candidates.stream()
                    .map(this::shortestSequencesOfPress)
                    .flatMap(Collection::stream)
                    .toList();

            var smallestSize = sequences.stream().mapToLong(String::length).min().orElseThrow();

            return sequences.stream()
                    .filter(s -> s.length() == smallestSize)
                    .collect(Collectors.toSet());
        }

        public Set<String> shortestSequencesOfPress(String desiredCombination) {
            var cursor = locationOf(ACTION);
            var combination = desiredCombination.toCharArray();
            var i = 0;
            Set<String> result = new HashSet<>();
            result.add("");

            while (i < combination.length) {
                var destination = locationOf(combination[i]);

                result = splitSequences(result, shortestSequences(cursor, destination));

                cursor = destination;
                i++;
            }

//            System.out.println("Shortest sequence for " + desiredCombination + " is: " + result);

            return result;
        }

        private Set<String> splitSequences(Set<String> current, Set<String> valuesToAppend) {
            return current.stream()
                    .flatMap(c -> valuesToAppend.stream().map(v -> c + v + ACTION))
                    .collect(Collectors.toSet());
        }

        private Set<String> shortestSequences(Location start, Location destination) {
            var potentialPaths = new HashMap<Location, Set<String>>();
            var largestHistorySizePaths = new HashMap<Location, Integer>();

            Queue<LocationHistory> queue = new LinkedList<>();
            queue.add(new LocationHistory(start, ""));

            while (!queue.isEmpty()) {
                var cursor = queue.poll();

                var currentLength = cursor.history.length();
                if (largestHistorySizePaths.containsKey(cursor.current) &&
                        largestHistorySizePaths.get(cursor.current) < currentLength) {
                    continue;
                }

                if (!largestHistorySizePaths.containsKey(cursor.current) ||
                        currentLength > largestHistorySizePaths.get(cursor.current)) {
                    largestHistorySizePaths.put(cursor.current, currentLength);
                    potentialPaths.put(cursor.current, new HashSet<>());
                }

                potentialPaths.get(cursor.current).add(cursor.history);

                queue.addAll(neighbours(cursor));
            }

            return potentialPaths.get(destination);
        }

        private Collection<LocationHistory> neighbours(LocationHistory cursor) {
            return Stream.of(
                            cursor.up(),
                            cursor.right(),
                            cursor.down(),
                            cursor.left()
                    )
                    .filter(this::withinBounds)
                    .filter(this::notGap)
                    .toList();
        }

        private boolean notGap(LocationHistory locationHistory) {
            return GAP != keys[locationHistory.current.y][locationHistory.current.x];
        }

        private boolean withinBounds(LocationHistory locationHistory) {
            return 0 <= locationHistory.current.y && locationHistory.current.y < keys.length &&
                    0 <= locationHistory.current.x && locationHistory.current.x < keys[0].length;
        }

        private Location locationOf(char value) {
            for (var y = 0; y < keys.length; y++) {
                for (var x = 0; x < keys[y].length; x++) {
                    if (value == keys[y][x]) return new Location(x, y);
                }
            }

            throw new IllegalArgumentException("Could not find: " + value + " in " + Arrays.toString(keys));
        }
    }

    private record LocationHistory(Location current, String history) {
        public LocationHistory up() {
            return new LocationHistory(new Location(current.x, current.y - 1), history + UP);
        }

        public LocationHistory right() {
            return new LocationHistory(new Location(current.x + 1, current.y), history + RIGHT);
        }

        public LocationHistory down() {
            return new LocationHistory(new Location(current.x, current.y + 1), history + DOWN);
        }

        public LocationHistory left() {
            return new LocationHistory(new Location(current.x - 1, current.y), history + LEFT);
        }
    }

    private record Location(int x, int y) {
    }
}
