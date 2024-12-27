package com.codingnagger.adventofcode2024.days;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day22 implements Day {
    @Override
    public String partOne(List<String> input) {
        return "" + input.stream()
                .mapToLong(Long::parseLong)
                .map(this::evolveSecretNumber2000Times)
                .sum();
    }

    @Override
    public String partTwo(List<String> input) {
        return Solver.from(input).solve();
    }

    private long evolveSecretNumber2000Times(long value) {
        var count = 2000;
        var result = value;

        for (var n = 0; n < count; n++) {
            result = evolve(result);
        }

        return result;
    }

    private static long evolve(long secretNumber) {
        var multBy64 = secretNumber * 64;
        var firstMix = mix(multBy64, secretNumber);
        var firstPrune = prune(firstMix);
        var divBy32 = firstPrune / 32;
        var secondMix = mix(divBy32, firstPrune);
        var secondPrune = prune(secondMix);
        var multBy2048 = secondPrune * 2048;
        var thirdMix = mix(multBy2048, secondPrune);
        return prune(thirdMix);
    }

    private static long prune(long secretNumber) {
        return secretNumber % 16777216;
    }

    private static long mix(long value, long secretNumber) {
        return value ^ secretNumber;
    }

    private record Solver(List<Merchant> merchants) {
        public static Solver from(List<String> input) {
            return new Solver(input.stream().map(Merchant::from).toList());
        }

        public String solve() {
            var merchantMaxes = merchants.stream().map(Merchant::maxBySequence).toList();

            var allKeys = merchantMaxes.stream()
                    .flatMap(m -> m.keySet().stream())
                    .collect(Collectors.toSet());

            var max = Long.MIN_VALUE;

            for (var key : allKeys) {
                var value = merchantMaxes.stream()
                        .mapToLong(m -> m.containsKey(key) ? m.get(key) : 0)
                        .sum();

                if (value > max) {
                    max = value;
                }
            }

            return max + "";
        }
    }

    private record Merchant(long[] prices, long[] changes) {
        public static Merchant from(String input) {
            var secretNumber = Long.parseLong(input);
            var prices = new long[2001];
            var changes = new long[2001];

            var count = 2001;

            secretNumber = evolve(secretNumber);
            prices[0] = secretNumber % 10;

            for (var n = 1; n < count; n++) {
                secretNumber = evolve(secretNumber);
                prices[n] = secretNumber % 10;
                changes[n] = prices[n] - prices[n - 1];
            }

            return new Merchant(prices, changes);
        }

        public Map<String, Long> maxBySequence() {
            var maxes = new HashMap<String, Long>();

            for (var n = 4; n < changes.length; n++) {
                var sequenceKey = Stream.of(changes[n - 3], changes[n - 2], changes[n - 1], changes[n])
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));

                if (!maxes.containsKey(sequenceKey)) {
                    maxes.put(sequenceKey, prices[n]);
                }
            }

            return maxes;
        }
    }
}
