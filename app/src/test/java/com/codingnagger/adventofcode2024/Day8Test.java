package com.codingnagger.adventofcode2024;

import com.codingnagger.adventofcode2024.days.Day;
import com.codingnagger.adventofcode2024.days.Day8;
import com.codingnagger.adventofcode2024.utils.InputLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Day8Test {
    private static final List<String> INPUT = InputLoader.LoadTest("day8.txt");
    private static final Day DAY = new Day8();

    @Test
    void partOne() {
        String result = DAY.partOne(INPUT);

        assertThat(result).isEqualTo("14");
    }

    @ParameterizedTest
    @ValueSource(strings = {"day8_simplest"})
    void partOne_smallTests(String filename) {
        String result = DAY.partOne(InputLoader.LoadTest(filename + ".txt"));

        assertThat(result).isEqualTo("2");
    }

    @Test
    void partTwo() {
        String result = DAY.partTwo(INPUT);

        assertThat(result).isEqualTo("34");
    }

    @ParameterizedTest
    @ValueSource(strings = {"day8_simplest"})
    void partTwo_smallTests(String filename) {
        String result = DAY.partTwo(InputLoader.LoadTest(filename + ".txt"));

        assertThat(result).isEqualTo("4");
    }
}
