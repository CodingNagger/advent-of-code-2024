package com.codingnagger.adventofcode2024;

import com.codingnagger.adventofcode2024.days.Day;
import com.codingnagger.adventofcode2024.days.Day2;
import com.codingnagger.adventofcode2024.utils.InputLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Day2Test {
    private static final List<String> INPUT = InputLoader.LoadTest("day2.txt");
    private static final Day DAY = new Day2();

    @Test
    void partOne() {
        String result = DAY.partOne(INPUT);

        assertThat(result).isEqualTo("2");
    }

    @ParameterizedTest
    @CsvSource({"day2_directionUnsafe,0"})
    void partOne_smallTests(String filename, String expected) {
        String result = DAY.partOne(InputLoader.LoadTest(filename + ".txt"));

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void partTwo() {
        String result = DAY.partTwo(INPUT);

        assertThat(result).isEqualTo("4");
    }

    @ParameterizedTest
    @CsvSource({"day2_partTwoSkipFirst,2", "day2_partTwoSkipLast,2"})
    void partTwo_smallTests(String filename, String expected) {
        String result = DAY.partTwo(InputLoader.LoadTest(filename + ".txt"));

        assertThat(result).isEqualTo(expected);
    }
}
