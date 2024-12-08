package com.codingnagger.adventofcode2024.days;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 implements Day {

    @Override
    public String partOne(List<String> input) {
        var result = 0;
        final var parsingPattern = Pattern.compile("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)");


        for (var line : input) {
            Matcher matcher = parsingPattern.matcher(line);
            while (matcher.find()) {
                result += Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));
            }
        }

        return result + "";
    }

    @Override
    public String partTwo(List<String> input) {
        var result = 0;
        var disabled = false;

        final var parsingPattern = Pattern.compile("(do\\(\\)|don't\\(\\)|mul\\(([0-9]{1,3}),([0-9]{1,3})\\))");

        for (var line : input) {
            Matcher matcher = parsingPattern.matcher(line);
            while (matcher.find()) {
                System.out.println(matcher.group(1));
                if (matcher.group(1).equals("do()")) {
                    disabled = false;
                } else if (matcher.group(1).equals("don't()")) {
                    disabled = true;
                } else {
                    if (disabled) {
                        continue;
                    }

                    result += Integer.parseInt(matcher.group(2)) * Integer.parseInt(matcher.group(3));
                }
            }
        }

        return result + "";
    }
}
