package com.codingnagger.adventofcode2024;

import com.codingnagger.adventofcode2024.days.Day;
import com.codingnagger.adventofcode2024.days.Day17;
import com.codingnagger.adventofcode2024.utils.InputLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Day17Test {
    private static final List<String> INPUT = InputLoader.LoadTest("day17.txt");
    private static final Day DAY = new Day17();

    public static Stream<Arguments> partOne_smallTests_fixtures() {
        return Stream.of(
                Arguments.of("day17_miniExample1", "A:0;B:1;C:9\n"),
                Arguments.of("day17_miniExample2", "A:10;B:0;C:0\n0,1,2"),
                Arguments.of("day17_miniExample3", "A:0;B:0;C:0\n4,2,5,6,7,7,7,7,3,1,0"),
                Arguments.of("day17_miniExample4", "A:0;B:26;C:0\n"),
                Arguments.of("day17_miniExample5", "A:0;B:44354;C:43690\n")
        );
    }

    @Test
    void partOne() {
        String result = DAY.partOne(INPUT);

        assertThat(result.split("\n")[1]).isEqualTo("4,6,3,5,6,3,5,2,1,0");
    }

    @ParameterizedTest
    @MethodSource("partOne_smallTests_fixtures")
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
