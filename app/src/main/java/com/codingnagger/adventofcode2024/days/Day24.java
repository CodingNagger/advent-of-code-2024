package com.codingnagger.adventofcode2024.days;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day24 implements Day {
    private static final Pattern INITIAL_VALUE_PATTERN = Pattern.compile("([^:]+): ([0-1])");
    private static final Pattern GATE_PATTERN = Pattern.compile("(\\S+) (\\S+) (\\S+) -> (\\S+)");

    @Override
    public String partOne(List<String> input) {
        var values = new HashMap<String, Integer>();
        Queue<Gate> gates = new LinkedList<>();

        for (String line : input) {
            var valueMatcher = INITIAL_VALUE_PATTERN.matcher(line);
            if (valueMatcher.find()) {
                values.put(valueMatcher.group(1), Integer.parseInt(valueMatcher.group(2)));
                continue;
            }

            var gateMatcher = GATE_PATTERN.matcher(line);
            if (gateMatcher.find()) {
                gates.add(new Gate(gateMatcher.group(1), gateMatcher.group(3), Operation.valueOf(gateMatcher.group(2)), gateMatcher.group(4)));
            }
        }

        while (!gates.isEmpty()) {
            var current = gates.poll();

            if (!current.canOperate(values)) {
                gates.add(current);
                continue;
            }

            values.put(current.output, current.compute(values));
        }

        return decimalNumber(values);
    }

    private String decimalNumber(HashMap<String, Integer> values) {
        return String.valueOf(Long.parseLong(binaryNumber(values), 2));
    }

    private String binaryNumber(HashMap<String, Integer> values) {
        return values.keySet()
                .stream()
                .filter(key -> key.startsWith("z"))
                .sorted(Comparator.reverseOrder())
                .map(values::get)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    @Override
    public String partTwo(List<String> input) {
        return "";
    }

    private record Gate(String left, String right, Operation operation, String output) {
        public boolean canOperate(HashMap<String, Integer> input) {
            return input.containsKey(left) && input.containsKey(right);
        }

        public Integer compute(HashMap<String, Integer> input) {
            return switch (operation) {
                case AND -> input.get(left) == 1 && input.get(right) == 1 ? 1 : 0;
                case OR -> input.get(left) == 1 || input.get(right) == 1 ? 1 : 0;
                case XOR -> input.get(left) ^ input.get(right);
            };
        }
    }

    private enum Operation {
        AND, OR, XOR;
    }
}
