package com.codingnagger.adventofcode2024;

import com.codingnagger.adventofcode2024.days.Day;
import com.codingnagger.adventofcode2024.days.Day7;
import com.codingnagger.adventofcode2024.utils.InputLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Day7Test {
    private static final List<String> INPUT = InputLoader.LoadTest("day7.txt");
    private static final Day DAY = new Day7();

    @Test
    void partOne() {
        String result = DAY.partOne(INPUT);

        assertThat(result).isEqualTo("3749");
    }

    @ParameterizedTest
    @CsvSource({"day7_simplest,3267", "day7_testValueBeforeEnd,0"})
    void partOne_smallTests(String filename, String expected) {
        String result = DAY.partOne(InputLoader.LoadTest(filename + ".txt"));

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void partTwo() {
        String result = DAY.partTwo(INPUT);

        assertThat(result).isEqualTo("");
    }

    @ParameterizedTest
    @ValueSource(strings = {"day8_simplest"})
    void partTwo_smallTests(String filename) {
        String result = DAY.partTwo(InputLoader.LoadTest(filename + ".txt"));

        assertThat(result).isEqualTo("");
    }
}
