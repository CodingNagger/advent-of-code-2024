package com.codingnagger.adventofcode2024;

import com.codingnagger.adventofcode2024.days.Day;
import com.codingnagger.adventofcode2024.days.Day10;
import com.codingnagger.adventofcode2024.utils.InputLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Day10Test {
    private static final List<String> INPUT = InputLoader.LoadTest("day10.txt");
    private static final Day DAY = new Day10();

    @Test
    void partOne() {
        String result = DAY.partOne(INPUT);

        assertThat(result).isEqualTo("36");
    }

    @ParameterizedTest
    @CsvSource({"day10_simplest,1", "day10_simple,2"})
    void partOne_smallTests(String filename, String expected) {
        String result = DAY.partOne(InputLoader.LoadTest(filename + ".txt"));

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void partTwo() {
        String result = DAY.partTwo(INPUT);

        assertThat(result).isEqualTo("81");
    }

    @ParameterizedTest
    @CsvSource({
            "day10_simple,2",
            "day10_partTwo_simplest,3",
            "day10_partTwo_secondExample, 13",
            "day10_partTwo_thirdExample,227",
            "day10_partTwo_simplestWithExtraPath,4",
            "day10_partTwo_simplestWithExtraSummits,6",
            "day10_partTwo_confirmExtraEndingBug, 2"
    })
    void partTwo_smallTests(String filename, String expected) {
        String result = DAY.partTwo(InputLoader.LoadTest(filename + ".txt"));

        assertThat(result).isEqualTo(expected);
    }

}
