package com.codingnagger.adventofcode2024.days;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class Day13 implements Day {
    @Override
    public String partOne(List<String> input) {
        return fewestTokens(input, 0L) + "";
    }

    @Override
    public String partTwo(List<String> input) {
        return fewestTokens(input, 10000000000000L) + "";
    }

    private long fewestTokens(List<String> input, long offset) {
        final var aButtonPattern = Pattern.compile("Button A: X\\+([0-9]+), Y\\+([0-9]+)");
        final var bButtonPattern = Pattern.compile("Button B: X\\+([0-9]+), Y\\+([0-9]+)");
        final var prizePattern = Pattern.compile("Prize: X=([0-9]+), Y=([0-9]+)");

        Vector a = null;
        Vector b = null;
        Location prize = null;
        var machines = new ArrayList<ClawMachine>();

        for (var line : input) {
            if (line.isBlank()) {
                machines.add(new ClawMachine(a, b, prize));
                a = null;
                b = null;
                prize = null;
            } else if (a == null) {
                var matcher = aButtonPattern.matcher(line);
                if (matcher.find()) {
                    a = new Vector(Long.parseLong(matcher.group(1)), Long.parseLong(matcher.group(2)));
                } else {
                    throw new IllegalStateException("There should be a A button");
                }
            } else if (b == null) {
                var matcher = bButtonPattern.matcher(line);
                if (matcher.find()) {
                    b = new Vector(Long.parseLong(matcher.group(1)), Long.parseLong(matcher.group(2)));
                } else {
                    throw new IllegalStateException("There should be a B button");
                }
            } else if (prize == null) {
                var matcher = prizePattern.matcher(line);
                if (matcher.find()) {
                    prize = new Location(Long.parseLong(matcher.group(1)) + offset, Long.parseLong(matcher.group(2)) + offset);
                } else {
                    throw new IllegalStateException("There should be a prize");
                }
            }
        }

        machines.add(new ClawMachine(a, b, prize));

        return machines.stream()
                .map(ClawMachine::minimumSpendForVictory)
                .<Long>mapMulti(Optional::ifPresent)
                .mapToLong(l -> l)
                .sum();
    }

    private record ClawMachine(Vector a, Vector b, Location prize) {
        public Optional<Long> minimumSpendForVictory() {
            long determinant = a.x * b.y - a.y * b.x;

            long aPresses = (prize.x * b.y - prize.y * b.x) / determinant;
            long bPresses = (a.x * prize.y - a.y * prize.x) / determinant;

            if ((a.x * aPresses + b.x * bPresses == prize.x) &&
                    (a.y * aPresses + b.y * bPresses == prize.y)) {
                return Optional.of(aPresses * 3L + bPresses);
            } else {
                return Optional.empty();
            }
        }
    }

    private record Vector(long x, long y) {
    }

    private record Location(long x, long y) {
    }
}
