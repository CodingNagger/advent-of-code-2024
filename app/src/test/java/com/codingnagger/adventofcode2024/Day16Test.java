package com.codingnagger.adventofcode2024;

import com.codingnagger.adventofcode2024.days.Day;
import com.codingnagger.adventofcode2024.days.Day16;
import com.codingnagger.adventofcode2024.utils.InputLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Day16Test {
    private static final List<String> INPUT = InputLoader.LoadTest("day16.txt");
    private static final Day DAY = new Day16();

    @Test
    void partOne() {
        String result = DAY.partOne(INPUT);

        assertThat(result).isEqualTo("11048");
    }

    @ParameterizedTest
    @CsvSource({
            "day16_smallExample,7036"})
    void partOne_smallTests(String filename, String expected) {
        String result = DAY.partOne(InputLoader.LoadTest(filename + ".txt"));

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void partTwo() {
        String result = DAY.partTwo(INPUT);

        assertThat(result).isEqualTo("64");
    }

    @ParameterizedTest
    @CsvSource({
            "day16_smallExample,45"})
    void partTwo_smallTests(String filename, String expected) {
        String result = DAY.partOne(InputLoader.LoadTest(filename + ".txt"));

        assertThat(result).isEqualTo(expected);
    }
}
