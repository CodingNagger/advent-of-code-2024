package com.codingnagger.adventofcode2024.days;

import java.util.*;

public class Day5 implements Day {
    @Override
    public String partOne(List<String> input) {
        var afterRules = new HashMap<String, List<String>>();
        var result = 0;

        var loadRules = true;

        for (var line : input) {
            if (line.isBlank()) {
                loadRules = false;
                continue;
            }

            if (loadRules) {
                var rule = line.split("\\|");
                if (!afterRules.containsKey(rule[0])) {
                    afterRules.put(rule[0], new ArrayList<>());
                }

                afterRules.get(rule[0]).add(rule[1]);
            } else {
                var maybeNumber = middlePageNumberIfCorrectlyOrdered(line, afterRules);

                if (maybeNumber.isPresent()) {
                    result += maybeNumber.get();
                }
            }
        }

        return result + "";
    }

    private Optional<Integer> middlePageNumberIfCorrectlyOrdered(String line, HashMap<String, List<String>> afterRules) {
        var order = Arrays.stream(line.split(",")).toList();

        var published = new ArrayList<String>();

        for (var page : order) {
            if (afterRules.containsKey(page) && afterRules.get(page).stream().anyMatch(published::contains)) {
                return Optional.empty();
            }
            published.add(page);
        }

        return Optional.of(Integer.parseInt(published.get(published.size() / 2)));
    }

    @Override
    public String partTwo(List<String> input) {
        var afterRules = new HashMap<String, List<String>>();
        var result = 0;

        var loadRules = true;

        for (var line : input) {
            if (line.isBlank()) {
                loadRules = false;
                continue;
            }

            if (loadRules) {
                var rule = line.split("\\|");
                if (!afterRules.containsKey(rule[0])) {
                    afterRules.put(rule[0], new ArrayList<>());
                }

                afterRules.get(rule[0]).add(rule[1]);
            } else {
                if (middlePageNumberIfCorrectlyOrdered(line, afterRules).isEmpty()) {
                    result += fixOrder(line, afterRules);
                }
            }
        }

        return result + "";
    }

    private int fixOrder(String line, HashMap<String, List<String>> afterRules) {
        var order = Arrays.stream(line.split(",")).toList();
        var published = new ArrayList<String>();

        for (var page : order) {
//            System.out.println("Adding " + page);
            published.add(page);

            if (afterRules.containsKey(page)) {
                var pagesToReorder = new ArrayList<String>();

                for (var publishedPage : published) {
                    if (afterRules.get(page).contains(publishedPage)) {
                        pagesToReorder.add(publishedPage);
                    }
                }

                for (var publishedPage : pagesToReorder) {
                    published.remove(publishedPage);
                    published.add(publishedPage);
                }
            }
        }

        System.out.println(Arrays.toString(published.toArray()));

        return Integer.parseInt(published.get(published.size() / 2));
    }
}
