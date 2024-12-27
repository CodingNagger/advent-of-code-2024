package com.codingnagger.adventofcode2024.days;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day23 implements Day {
    @Override
    public String partOne(List<String> input) {
        var pairs = input.stream()
                .map(line -> line.split("-"))
                .map(values -> new Pair(values[0].trim(), values[1].trim()))
                .collect(Collectors.toSet());

        var computers = pairs.stream()
                .flatMap(Pair::stream)
                .collect(Collectors.toSet());

        var interconnectedComputers = new HashSet<Set<String>>();

        for (var pair : pairs) {
            var leftLeftMatches = pairs.stream()
                    .filter(p -> !p.equals(pair))
                    .filter(p -> pair.left.equals(p.left))
                    .map(Pair::right)
                    .toList();

            var leftRightMatches = pairs.stream()
                    .filter(p -> !p.equals(pair))
                    .filter(p -> pair.left.equals(p.right))
                    .map(Pair::left)
                    .toList();

            var leftMatches = new HashSet<>(leftLeftMatches);
            leftMatches.addAll(leftRightMatches);

            var rightLeftMatches = pairs.stream()
                    .filter(p -> !p.equals(pair))
                    .filter(p -> pair.right.equals(p.left))
                    .map(Pair::right)
                    .toList();

            var rightRightMatches = pairs.stream()
                    .filter(p -> !p.equals(pair))
                    .filter(p -> pair.right.equals(p.right))
                    .map(Pair::left)
                    .toList();

            var rightMatches = new HashSet<>(rightLeftMatches);
            rightMatches.addAll(rightRightMatches);

            Set<String> intersection = new HashSet<>(leftMatches);
            intersection.retainAll(rightMatches);

            for (var computer : intersection) {
                var candidateSet = Stream.of(computer, pair.left, pair.right).collect(Collectors.toSet());

                if (interconnectedComputers.contains(candidateSet)) {
                    continue;
                }

                interconnectedComputers.add(candidateSet);
            }
        }

        return "" + interconnectedComputers.stream()
                .filter(s -> s.stream().anyMatch(v -> v.startsWith("t")))
                .count();
    }

    @Override
    public String partTwo(List<String> input) {
        return "";
    }

    private record Pair(String left, String right) {
        public Stream<String> stream() {
            return Stream.of(left, right);
        }

        public boolean contains(String computer) {
            return left.equals(computer) || right.equals(computer);
        }
    }
}
