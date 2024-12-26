package com.codingnagger.adventofcode2024;

import com.codingnagger.adventofcode2024.days.Day;
import com.codingnagger.adventofcode2024.days.Day22;
import com.codingnagger.adventofcode2024.utils.InputLoader;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Day22Test {
    private static final Day DAY = new Day22();

    @Test
    void partOne() {
        String result = DAY.partOne(InputLoader.LoadTest("day22.txt"));

        assertThat(result).isEqualTo("37327623");
    }

    @Test
    void partTwo() {
        String result = DAY.partTwo(InputLoader.LoadTest("day22_partTwo.txt"));

        assertThat(result).isEqualTo("23");
    }
}
