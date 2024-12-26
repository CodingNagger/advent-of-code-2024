package com.codingnagger.adventofcode2024.days;

import java.util.List;

public class Day22 implements Day {
    @Override
    public String partOne(List<String> input) {
        return "" + input.stream()
                .mapToLong(Long::parseLong)
                .map(this::evolveSecretNumber2000Times)
                .sum();
    }

    private long evolveSecretNumber2000Times(long value) {
        var count = 2000;
        var result = value;

        for (var n = 0; n < count; n++) {
            result = evolve(result);
        }

        return result;
    }

    private long evolve(long secretNumber) {
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

    private long prune(long secretNumber) {
        return secretNumber % 16777216;
    }

    private long mix(long value, long secretNumber) {
        return value ^ secretNumber;
    }

    @Override
    public String partTwo(List<String> input) {
        return "";
    }
}
