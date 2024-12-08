package com.codingnagger.adventofcode2024.days;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Day8 implements Day {
    @Override
    public String partOne(List<String> input) {
        var map = input.stream().map(String::toCharArray).toArray(char[][]::new);

        // find map bounds
        var xBound = map[0].length;
        var yBound = map.length;

        // find antennas
        var antennas = new ArrayList<Antenna>();
        for (var y = 0; y < yBound; y++) {
            for (var x = 0; x < xBound; x++) {
                if (map[y][x] != '.') {
                    antennas.add(new Antenna(map[y][x], x, y));
                }
            }
        }

        // find antinodes
        var antinodes = new HashSet<Antinode>();
        for (var antenna : antennas) {
            var tmpAntinode = antennas.stream()
                    .filter(a -> a != antenna)
                    .filter(a -> a.frequency == antenna.frequency)
                    .map(antenna::antinode)
                    .filter(a -> a.x >= 0 && a.x < xBound && a.y >= 0 && a.y < yBound)
                    .toList();

            System.out.println(antenna + " -> " + tmpAntinode);

            antinodes.addAll(tmpAntinode);
        }

        print(xBound, yBound, antennas, antinodes);

        return antinodes.stream().map(a -> new Location(a.x, a.y)).distinct().count() + "";
    }

    private void print(int xBound, int yBound, ArrayList<Antenna> antennas, HashSet<Antinode> antinodes) {
        for (var y = 0; y < yBound; y++) {
            for (var x = 0; x < xBound; x++) {
                var finalY = y;
                var finalX = x;

//                var antenna = antennas.stream().filter(a -> a.x == finalX && a.y == finalY).findFirst();
//                if (antenna.isPresent()) {
//                    System.out.print(antenna.get().frequency);
//                    continue;
//                }

                var antinode = antinodes.stream().filter(a -> a.x == finalX && a.y == finalY).findFirst();

                System.out.print(antinode.isPresent() ? '#' : '.');
            }
            System.out.println();
        }
    }

    @Override
    public String partTwo(List<String> input) {
        return null;
    }

    record Antenna(char frequency, int x, int y) {
        public Antinode antinode(Antenna other) {
            if (other.frequency != frequency) {
                throw new IllegalArgumentException("Wrong frequency " + other.frequency + ", expected " + frequency);
            }

            return new Antinode(frequency, x + (x - other.x), y + (y - other.y));
        }
    }

    record Antinode(char frequency, int x, int y) {
    }

    record Location(int x, int y) {
    }
}
