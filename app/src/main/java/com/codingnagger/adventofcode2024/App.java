/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.codingnagger.adventofcode2024;

import com.codingnagger.adventofcode2024.days.Day;
import com.codingnagger.adventofcode2024.days.Day19;
import com.codingnagger.adventofcode2024.utils.InputLoader;

import java.time.Instant;
import java.util.List;

public class App {
    public static void main(String[] args) {
        System.out.println("Advent of Code 2024");

        List<String> input = InputLoader.Load("day19.txt");

        Day day = new Day19();

        System.out.println("Part 1:");
        var partOneStart = Instant.now();
        System.out.println(day.partOne(input));
        var partOneEnd = Instant.now();
        printDurationBetween(partOneStart, partOneEnd);

        System.out.println("Part 2:");
        var partTwoStart = Instant.now();
        System.out.println(day.partTwo(input));
        var partTwoEnd = Instant.now();
        printDurationBetween(partTwoStart, partTwoEnd);
    }

    static void printDurationBetween(Instant start, Instant end) {
        var duration = end.toEpochMilli() - start.toEpochMilli();
        System.out.printf("Executed in %d:%02d:%02d.%03d%n",
                duration / 3600000, (duration % 3600000) / 60000, (duration % 60000) / 1000, duration % 1000);
    }
}
