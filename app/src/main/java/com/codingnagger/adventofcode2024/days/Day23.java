package com.codingnagger.adventofcode2024.days;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day23 implements Day {
    @Override
    public String partOne(List<String> input) {
        return "" + interconnectedTriplets(getPairs(input))
                .stream()
                .filter(s -> s.stream().anyMatch(v -> v.startsWith("t")))
                .count();
    }

    @Override
    public String partTwo(List<String> input) {
        var pairs = getPairs(input);
        var groups = new ArrayList<>(interconnectedTriplets(pairs));

        int leftCursor = 0;
        while (leftCursor < groups.size()) {
            int rightCursor = leftCursor + 1;

            comparison:
            while (rightCursor < groups.size()) {
                if (groups.get(leftCursor).containsAll(groups.get(rightCursor))) {
                    rightCursor++;
                    continue;
                }
                // Check if there is any intersection between the sets
                Set<String> intersection = new HashSet<>(groups.get(leftCursor));
                intersection.retainAll(groups.get(rightCursor));

                if (!intersection.isEmpty()) {
                    // use subsets without intersections
                    // if all left computers are interconnected with all the right computers
                    // then merge the sets
                    var leftComputers = new HashSet<>(groups.get(leftCursor));
                    leftComputers.removeAll(intersection);

                    var rightComputers = new HashSet<>(groups.get(rightCursor));
                    rightComputers.removeAll(intersection);

                    for (var leftComputer : leftComputers) {
                        for (var rightComputer : rightComputers) {
                            if (!pairs.contains(new Pair(leftComputer, rightComputer)) &&
                                    !pairs.contains(new Pair(rightComputer, leftComputer))) {
                                rightCursor++;
                                continue comparison;
                            }
                        }
                    }

                    // merge the sets
                    var newGroup = new HashSet<>(groups.get(leftCursor));
                    newGroup.addAll(rightComputers);

                    groups.remove(leftCursor);
                    groups.add(leftCursor, newGroup);

                    groups.remove(rightCursor);

                    groups.add(leftCursor, newGroup);
                } else {
                    rightCursor++;
                }
            }
            leftCursor++;
        }

        var maxSize = groups.stream().mapToLong(Set::size).max().orElseThrow();

        var resultGroup = groups.stream().filter(s -> s.size() == maxSize).toList().getFirst();

        return String.join(",", new TreeSet<>(resultGroup));
    }

    private List<Set<String>> interconnectedTriplets(Set<Pair> pairs) {
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

        return interconnectedComputers.stream().toList();
    }

    private static Set<Pair> getPairs(List<String> input) {
        return input.stream()
                .map(line -> line.split("-"))
                .map(values -> new Pair(values[0].trim(), values[1].trim()))
                .collect(Collectors.toSet());
    }

    private record Pair(String left, String right) {
    }
}
