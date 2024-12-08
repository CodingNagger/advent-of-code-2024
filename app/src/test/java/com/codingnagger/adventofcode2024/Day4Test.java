package com.codingnagger.adventofcode2024;

import com.codingnagger.adventofcode2024.days.Day;
import com.codingnagger.adventofcode2024.days.Day4;
import com.codingnagger.adventofcode2024.utils.InputLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Day4Test {
    private static final List<String> INPUT = InputLoader.LoadTest("day4.txt");
    private static final Day DAY = new Day4();

    @Test
    void partOne() {
        String result = DAY.partOne(INPUT);

        assertThat(result).isEqualTo("18");
    }

    @ParameterizedTest
    @ValueSource(strings = {"day4_simplestXmas", "day4_simplestReverseXmas"})
    void partOne_smallTests(String filename) {
        String result = DAY.partOne(InputLoader.LoadTest(filename + ".txt"));

        assertThat(result).isEqualTo("1");
    }

    @Test
    void partTwo() {
        String result = DAY.partTwo(INPUT);

        assertThat(result).isEqualTo("9");
    }

    @ParameterizedTest
    @CsvSource({"day4_part2_smallestXmas,1", "day4_part2_tangledXmas,2", "day4_part2_notXmas,0"})
    void partTwo_smallTests(String filename, String expected) {
        String result = DAY.partTwo(InputLoader.LoadTest(filename + ".txt"));

        assertThat(result).isEqualTo(expected);
    }
}
