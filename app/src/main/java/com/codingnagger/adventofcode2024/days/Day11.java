package com.codingnagger.adventofcode2024.days;

import java.util.List;
import java.util.regex.Pattern;

public class Day11 implements Day {
    private static final Pattern NUMBER_PATTERN = Pattern.compile("[0-9]+");

    @Override
    public String partOne(List<String> input) {
        return StoneSet.parse(input).blink(25).countStones() + "";
    }

    @Override
    public String partTwo(List<String> input) {
        return StoneSet.parse(input).blink(75).countStones() + "";
    }

    record StoneSet(String stones) {
        static StoneSet parse(List<String> input) {
            return new StoneSet(input.getFirst());
        }

        StoneSet blink(int times) {
            var result = this;

            for (var n = 0; n < times; n++) {
                result = result.blink();
                print(result);
            }

            return result;
        }

        private void print(StoneSet stones) {
            System.out.println(stones.stones);
        }

        StoneSet blink() {
            var matcher = NUMBER_PATTERN.matcher(stones);
            var builder = new StringBuilder();

            while (matcher.find()) {
                transform(matcher.group()).forEach(f -> builder.append(f).append(' '));
            }

            return new StoneSet(builder.toString().trim());
        }

        private List<String> transform(String number) {
            if ("0".equals(number)) {
                return List.of("1");
            }

            if (number.length() % 2 == 0) {
                return splitStone(number);
            }

            return times2024(number);
        }

        private List<String> splitStone(String number) {
            var firstPart = number.substring(0, (number.length() / 2));
            var secondPart = String.valueOf(Long.parseLong(number.substring(number.length() / 2)));

            return List.of(firstPart, secondPart);
        }

        private List<String> times2024(String number) {
            return List.of(String.valueOf(Long.parseLong(number) * 2024));
        }

        public long countStones() {
            return stones.chars().filter(c -> c == ' ').count() + 1;
        }
    }
}
