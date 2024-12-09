package com.codingnagger.adventofcode2024.days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.LongStream;

public class Day9 implements Day {
    @Override
    public String partOne(List<String> input) {
        var diskSections = getDiskSections(input);
        var diskBlocks = flattenDiskSections(diskSections);
        var compressedBlocks = compress(diskBlocks);

        return "" + calculateChecksum(compressedBlocks);
    }

    @Override
    public String partTwo(List<String> input) {
        var diskSections = getDiskSections(input);
        var diskBlocks = flattenDiskSections(diskSections);

//        print(diskBlocks);

        var compressedBlocks = compressByIds(diskBlocks);

        // 00992111777.44.333....5555.6666.....8888..
//        print(compressedBlocks);

        return "" + calculateChecksum(compressedBlocks);
    }

    private Long[] compressByIds(Long[] diskBlocks) {
        var resultingBlocks = Arrays.copyOf(diskBlocks, diskBlocks.length);
        var rightCursor = diskBlocks.length - 1;

        while (0 < rightCursor) {
            // grab further right spot that fits in left space
            while (resultingBlocks[rightCursor] == null) {
                rightCursor--;
            }

            var spaceNeeded = 0;

            var idToMove = resultingBlocks[rightCursor];
            do {
                spaceNeeded++;
            } while (rightCursor - spaceNeeded > 0 && (idToMove.equals(resultingBlocks[rightCursor - spaceNeeded])));

//            // System.out.println(idToMove + " - SpaceNeeded: " + spaceNeeded);

            // find space on the left
            var leftCursor = 0;
            var availableLeftSpace = 0;

            while (leftCursor < rightCursor && spaceNeeded > availableLeftSpace) {
                while (resultingBlocks[leftCursor] != null) {
                    leftCursor++;
                }

                while (leftCursor + availableLeftSpace < resultingBlocks.length && resultingBlocks[leftCursor + availableLeftSpace] == null) {
                    availableLeftSpace++;
                }

                if (availableLeftSpace < spaceNeeded || leftCursor + availableLeftSpace >= resultingBlocks.length) {
                    leftCursor++;
                    availableLeftSpace = 0;
                }
            }

            if (leftCursor >= rightCursor) {
                rightCursor -= spaceNeeded;
                continue;
            }

            for (var i = 0; i < spaceNeeded; i++) {
                // System.out.println(availableLeftSpace + " -  Left cursor: blocks[" + (leftCursor + i) + "]=" + resultingBlocks[leftCursor + i] + " - Right cursor: " + (rightCursor - i) + "]=" + resultingBlocks[rightCursor - i]);

                resultingBlocks[leftCursor + i] = resultingBlocks[rightCursor - i];
                resultingBlocks[rightCursor - i] = null;
            }

            // System.out.println("Left cursor: blocks[" + leftCursor + "]=" + resultingBlocks[leftCursor] + " - Right cursor: " + rightCursor + "]=" + resultingBlocks[rightCursor]);

//            print(resultingBlocks);
        }

        return resultingBlocks;
    }

    private void print(Long[] diskBlocks) {
        for (var diskBlock : diskBlocks) {
            System.out.print(diskBlock == null ? "." : diskBlock);
        }
        // System.out.println();
    }

    private static Long[] flattenDiskSections(List<DiskSection> diskSections) {
        return diskSections.stream().flatMap(d -> Arrays.stream(d.ids)).toArray(Long[]::new);
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

        while (index < compressedBlocks.length) {
            sum += index * (compressedBlocks[index] != null ? compressedBlocks[index] : 0L);
            index++;
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

    record DiskSection(Long[] ids) {
        public static DiskSection fromFileBlock(int id, int size) {
            return new DiskSection(LongStream.range(0, size).mapToObj(_ -> (long) id).toArray(Long[]::new));
        }

        public static DiskSection empty(int size) {
            return new DiskSection(new Long[size]);
        }

        public boolean isEmpty() {
            return Arrays.stream(ids).allMatch(Objects::isNull);
        }

        public boolean canAccept(DiskSection other) {
            var availableSpace = 0;
            var cursor = ids.length - 1 - availableSpace;

            while (cursor >= 0 && ids[cursor] == null) {
                availableSpace++;
                cursor = ids.length - 1 - availableSpace;
            }

            return availableSpace >= other.size();
        }

        private int size() {
            return ids.length;
        }

        public DiskSection emptied() {
            return empty(ids.length);
        }

        public DiskSection merge(DiskSection other) {
            var newIds = Arrays.copyOf(ids, ids.length);
            var cursor = 0;

            while (ids[cursor++] != null) ;

            for (var id : other.ids) {
                newIds[cursor++] = id;
            }

            return new DiskSection(newIds);
        }
    }
}
