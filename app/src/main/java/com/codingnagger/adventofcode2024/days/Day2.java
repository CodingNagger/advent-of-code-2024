package com.codingnagger.adventofcode2024.days;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day2 implements Day {
    @Override
    public String partOne(List<String> input) {
        return input.stream().map(Report::parse)
                .filter(Report::isSafe)
                .count() + "";
    }

    @Override
    public String partTwo(List<String> input) {
        return input.stream().map(Report::parse)
                .filter(Report::isSafeWithParallelUniverses)
                .count() + "";
    }

    private record Report(long[] levels) {
        static Report parse(String line) {
            return new Report(Arrays.stream(line.split(" ")).mapToLong(Long::parseLong).toArray());
        }

        public boolean isSafe() {
            System.out.println("Checking safety " + Arrays.toString(levels));

            var left = 0;
            var right = 1;

            var allUp = true;
            var allDown = true;

            while (right < levels.length) {
                System.out.println("Comparing " + levels[left] + " and " + levels[right]);
                var diff = levels[left] - levels[right];
                var absDiff = Math.abs(diff);

                if (absDiff < 1 || absDiff > 3) {
                    System.out.println("Unsafe absDiff = " + absDiff);
                    return false;
                }

                allUp = allUp && diff < 0;
                allDown = allDown && diff > 0;

                if (!allUp && !allDown) {
                    System.out.println("Direction changes = " + diff);
                    return false;
                }

                left++;
                right++;
            }

            System.out.println("Safe");
            return true;
        }

        public boolean isSafeWithParallelUniverses() {
            return isSafe() || Arrays.stream(subsets()).anyMatch(Report::isSafe);
        }

        private Report[] subsets() {
            return IntStream.range(0, levels.length)
                    .mapToObj(
                            i -> IntStream.range(0, levels.length)
                                    .filter(j -> j != i)
                                    .mapToLong(j -> levels[j]).toArray()
                    )
                    .map(Report::new)
                    .toArray(Report[]::new);
        }
    }
}
