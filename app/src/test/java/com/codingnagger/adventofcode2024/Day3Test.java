package com.codingnagger.adventofcode2024;

import com.codingnagger.adventofcode2024.days.Day;
import com.codingnagger.adventofcode2024.days.Day3;
import com.codingnagger.adventofcode2024.utils.InputLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Day3Test {
    private static final List<String> INPUT = InputLoader.LoadTest("day3.txt");
    private static final Day DAY = new Day3();

    @Test
    void partOne() {
        String result = DAY.partOne(INPUT);

        assertThat(result).isEqualTo("161");
    }

    @ParameterizedTest
    @CsvSource({"day3_doNothing,0"})
    void partOne_smallTests(String filename, String expected) {
        String result = DAY.partOne(InputLoader.LoadTest(filename + ".txt"));

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void partTwo() {
        String result = DAY.partTwo(InputLoader.LoadTest("day3_partTwo.txt"));

        assertThat(result).isEqualTo("48");
    }
}
