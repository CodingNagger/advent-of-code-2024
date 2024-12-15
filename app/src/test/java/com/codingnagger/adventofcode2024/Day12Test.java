package com.codingnagger.adventofcode2024;

import com.codingnagger.adventofcode2024.days.Day;
import com.codingnagger.adventofcode2024.days.Day12;
import com.codingnagger.adventofcode2024.utils.InputLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Day12Test {
    private static final List<String> INPUT = InputLoader.LoadTest("day12.txt");
    private static final Day DAY = new Day12();

    @Test
    void partOne() {
        String result = DAY.partOne(INPUT);

        assertThat(result).isEqualTo("1930");
    }

    @ParameterizedTest
    @CsvSource({
            "day12_simplest,12", "day12_simple,140", "day12_simpleRegionWithInnerRegion,132",
            "day12_simpleRegionWithoutInnerRegion,108", "day12_oneRegionContainingFiveRegions,772",
            "day12_weird, 426452"})
    void partOne_smallTests(String filename, String expected) {
        String result = DAY.partOne(InputLoader.LoadTest(filename + ".txt"));

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void partTwo() {
        String result = DAY.partTwo(INPUT);

        assertThat(result).isEqualTo("1206");
    }
}
