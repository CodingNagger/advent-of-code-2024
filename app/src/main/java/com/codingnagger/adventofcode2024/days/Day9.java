package com.codingnagger.adventofcode2024.days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.LongStream;

public class Day9 implements Day {
    @Override
    public String partOne(List<String> input) {
        // generate file blocks ; null for empty space - IDs for non empty.
        var diskSections = getDiskSections(input);

        // flatten disk section to blocks
        var diskBlocks = diskSections.stream().flatMap(d -> Arrays.stream(d.ids)).toArray(Long[]::new);

        // move blocks
        var compressedBlocks = compress(diskBlocks);

        // calculate checksum
        return "" + calculateChecksum(compressedBlocks);
    }

    private static ArrayList<DiskSection> getDiskSections(List<String> input) {
        var diskRepresentation = input.getFirst().chars()
                .map(Character::getNumericValue)
                .toArray();

        var isFileBlock = true;
        var index = 0;
        var diskSections = new ArrayList<DiskSection>();

        for (var size : diskRepresentation) {
            diskSections.add(isFileBlock ? DiskSection.fromFileBlock(index++, size) : DiskSection.empty(size));
            isFileBlock = !isFileBlock;
        }
        return diskSections;
    }

    private long calculateChecksum(Long[] compressedBlocks) {
        var index = 0;
        var sum = 0L;

        while (index < compressedBlocks.length && compressedBlocks[index] != null) {
            sum += compressedBlocks[index] * index++;
        }

        return sum;
    }

    private Long[] compress(Long[] diskBlocks) {
        var resultingBlocks = Arrays.copyOf(diskBlocks, diskBlocks.length);

        var leftCursor = 0;
        var rightCursor = diskBlocks.length - 1;

        while (leftCursor < rightCursor) {
            while (resultingBlocks[leftCursor] != null) {
                leftCursor++;
            }

            while (resultingBlocks[rightCursor] == null) {
                rightCursor--;
            }

            if (leftCursor >= rightCursor) {
                continue;
            }
            var tmp = resultingBlocks[leftCursor];
            resultingBlocks[leftCursor] = resultingBlocks[rightCursor];
            resultingBlocks[rightCursor] = tmp;
        }

        return resultingBlocks;
    }

    @Override
    public String partTwo(List<String> input) {
        return "";
    }

    record DiskSection(Long[] ids) {
        public static DiskSection fromFileBlock(int id, int size) {
            return new DiskSection(LongStream.range(0, size).mapToObj(_ -> (long) id).toArray(Long[]::new));
        }

        public static DiskSection empty(int size) {
            return new DiskSection(new Long[size]);
        }
    }
}
