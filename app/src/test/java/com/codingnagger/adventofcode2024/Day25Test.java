package com.codingnagger.adventofcode2024;

import com.codingnagger.adventofcode2024.days.Day;
import com.codingnagger.adventofcode2024.days.Day25;
import com.codingnagger.adventofcode2024.utils.InputLoader;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Day25Test {
    private static final List<String> INPUT = InputLoader.LoadTest("day25.txt");
    private static final Day DAY = new Day25();


    @Test
    void partOne() {
        String result = DAY.partOne(INPUT);

        assertThat(result).isEqualTo("3");
    }

    @Test
    void partTwo() {
        String result = DAY.partTwo(INPUT);

        assertThat(result).isEqualTo("");
    }
}
