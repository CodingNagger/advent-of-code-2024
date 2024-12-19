package com.codingnagger.adventofcode2024.days;

import java.util.*;

public class Day19 implements Day {
    private static final Map<String, Integer> CACHE = new HashMap<>();

    @Override
    public String partOne(List<String> input) {
        CACHE.clear();

        var towels = Arrays.stream(input.getFirst().split(",")).map(String::trim).sorted(Comparator.comparing(String::length).reversed()).toList();
        var designs = input.subList(2, input.size());

        return designs.stream().filter(d -> reproduceCount(d, towels) > 0).count() + "";
    }

    @Override
    public String partTwo(List<String> input) {
        CACHE.clear();

        var towels = Arrays.stream(input.getFirst().split(",")).map(String::trim).sorted(Comparator.comparing(String::length).reversed()).toList();
        var designs = input.subList(2, input.size());

        return designs.stream().mapToInt(d -> reproduceCount(d, towels)).sum() + "";
    }

    private int reproduceCount(String desiredDesign, List<String> orderedTowels) {
        if (CACHE.containsKey(desiredDesign)) {
            return CACHE.get(desiredDesign);
        }

        if (desiredDesign.isEmpty()) {
            return 1;
        }

        var result = orderedTowels.stream()
                .filter(desiredDesign::startsWith)
                .mapToInt(towel -> reproduceCount(desiredDesign.substring(towel.length()), orderedTowels))
                .sum();

        CACHE.put(desiredDesign, result);

        return result;
    }
}
