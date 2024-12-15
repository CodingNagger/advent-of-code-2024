package com.codingnagger.adventofcode2024;

import com.codingnagger.adventofcode2024.days.Day;
import com.codingnagger.adventofcode2024.days.Day15;
import com.codingnagger.adventofcode2024.utils.InputLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Day15Test {
    private static final List<String> INPUT = InputLoader.LoadTest("day15.txt");
    private static final Day DAY = new Day15();

    @Test
    void partOne() {
        String result = DAY.partOne(INPUT);

        assertThat(result).isEqualTo("10092");
    }

    @ParameterizedTest
    @CsvSource({
            "day15_starterExample,2028"})
    void partOne_smallTests(String filename, String expected) {
        String result = DAY.partOne(InputLoader.LoadTest(filename + ".txt"));

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void partTwo() {
        String result = DAY.partTwo(INPUT);

        assertThat(result).isEqualTo("");
    }
}
