package com.codingnagger.adventofcode2024.days;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11 implements Day {
    private static final Map<String, Long> CACHE = new HashMap<>();

    @Override
    public String partOne(List<String> input) {
        return Arrays.stream(input.getFirst().split(" ")).mapToLong(line -> countStonesAfterBlinking(25, line)).sum() + "";
    }

    @Override
    public String partTwo(List<String> input) {
        return Arrays.stream(input.getFirst().split(" ")).mapToLong(line -> countStonesAfterBlinking(75, line)).sum() + "";
    }

    private long countStonesAfterBlinking(int times, String stones) {
        var cacheKey = stones + "_" + times;
        if (CACHE.containsKey(cacheKey)) {
            return CACHE.get(cacheKey);
        }

        long result;

        if (times == 0) {
            result = 1;
        } else if (stones.equals("0")) {
            result = countStonesAfterBlinking(times - 1, "1");
        } else if (stones.length() % 2 == 0) {
            var splitStones = splitStone(stones);
            result = countStonesAfterBlinking(times - 1, splitStones.getFirst()) +
                    countStonesAfterBlinking(times - 1, splitStones.getLast());
        } else {
            result = countStonesAfterBlinking(times - 1, times2024(stones));
        }

        CACHE.put(cacheKey, result);
        return result;
    }

    private List<String> splitStone(String number) {
        var firstPart = number.substring(0, (number.length() / 2));
        var secondPart = String.valueOf(Long.parseLong(number.substring(number.length() / 2)));

        return List.of(firstPart, secondPart);
    }

    private String times2024(String number) {
        return String.valueOf(Long.parseLong(number) * 2024);
    }
}
