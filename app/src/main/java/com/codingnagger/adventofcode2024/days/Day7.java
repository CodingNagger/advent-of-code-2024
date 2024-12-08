package com.codingnagger.adventofcode2024.days;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

public class Day7 implements Day {
    @Override
    public String partOne(List<String> input) {
        return input.stream().map(Equation::parse)
                .filter(Equation::isSolvable)
                .mapToLong(Equation::test)
                .sum() + "";
    }

    @Override
    public String partTwo(List<String> input) {
        return input.stream().map(Equation::parse)
                .map(Equation::partTwo)
                .filter(Equation::isSolvable)
                .mapToLong(Equation::test)
                .sum() + "";
    }

    record Equation(long test, List<Long> components, boolean isPartTwo) {
        public static Equation parse(String line) {
            String[] parts = line.split(": ");
            var test = Long.parseLong(parts[0]);
            var components = Arrays.stream(parts[1].split(" ")).map(Long::parseLong).toList();

            return new Equation(test, components, false);
        }

        public Equation partTwo() {
            return new Equation(test, components, true);
        }

        public boolean isSolvable() {
            var queue = new ArrayDeque<SpitEquationComponents>();
            queue.add(new SpitEquationComponents(0, components));

            while (!queue.isEmpty()) {
                var current = queue.poll();

                if (current.accumulated == test && current.remainer.isEmpty()) {
                    return true;
                }

                if (current.accumulated > test) {
                    continue;
                }

                if (current.remainer.isEmpty()) {
                    continue;
                }

                var nextComponent = current.remainer.getFirst();
                var nextRemainder = current.remainer.subList(1, current.remainer.size());

                queue.add(new SpitEquationComponents(current.accumulated + nextComponent, nextRemainder));
                queue.add(new SpitEquationComponents(current.accumulated * nextComponent, nextRemainder));

                if (isPartTwo()) {
                    var accumulated = Long.parseLong("" + current.accumulated + nextComponent);
                    queue.add(new SpitEquationComponents(accumulated, nextRemainder));
                }
            }

            return false;
        }
    }

    record SpitEquationComponents(long accumulated, List<Long> remainer) {
    }
}
