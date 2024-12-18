package com.codingnagger.adventofcode2024.days;

import java.util.ArrayList;
import java.util.List;

public class Day17 implements Day {
    @Override
    public String partOne(List<String> input) {
        var registryA = numericValue(input.get(0));
        var registryB = numericValue(input.get(1));
        var registryC = numericValue(input.get(2));
        var program = stringValue(input.get(4)).chars()
                .map(Character::getNumericValue)
                .filter(i -> i >= 0)
                .toArray();

        return run(registryA, registryB, registryC, program, 0, List.of());
    }

    private String run(int registryA, int registryB, int registryC, int[] program, int cursor, List<String> output) {
        if (cursor >= program.length) {
            return "A:" + registryA + ";B:" + registryB + ";C:" + registryC + "\n" + String.join(",", output);
        }

        var opcode = program[cursor];
        var literalOperand = program[cursor + 1];
        var comboOperand = switch (literalOperand) {
            case int operand when operand < 4 -> operand;
            case 4 -> registryA;
            case 5 -> registryB;
            case 6 -> registryC;
            case 7 -> -1;
            default -> throw new IllegalStateException("Unexpected value: " + literalOperand);
        };

        int newA = registryA, newB = registryB, newC = registryC, newCursor = cursor + 2;
        var newOutput = new ArrayList<>(output);
        switch (opcode) {
            case 0 -> newA = (int) (registryA / Math.pow(2, comboOperand));
            case 1 -> newB = registryB ^ literalOperand;
            case 2 -> newB = comboOperand % 8;
            case 3 -> {
                if (registryA != 0) {
                    newCursor = literalOperand;
                }
            }
            case 4 -> newB = registryB ^ registryC;
            case 5 -> newOutput.add(String.valueOf(comboOperand % 8));
            case 6 -> newB = (int) (registryA / Math.pow(2, comboOperand));
            case 7 -> newC = (int) (registryA / Math.pow(2, comboOperand));
        }

        return run(newA, newB, newC, program, newCursor, newOutput);
    }

    private static String stringValue(String line) {
        return line.split(":")[1].trim();
    }

    private static int numericValue(String line) {
        return Integer.parseInt(stringValue(line));
    }

    @Override
    public String partTwo(List<String> input) {
        return "";
    }
}
