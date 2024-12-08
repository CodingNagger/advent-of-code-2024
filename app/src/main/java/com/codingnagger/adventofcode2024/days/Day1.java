package com.codingnagger.adventofcode2024.days;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class Day1 implements Day {
    @Override
    public String partOne(List<String> input) {
        var leftList = new PriorityQueue<Long>();
        var rightList = new PriorityQueue<Long>();

        for (String s : input) {
            var values = Arrays.stream(s.split(" +")).mapToLong(Long::parseLong).toArray();
            leftList.add(values[0]);
            rightList.add(values[1]);
        }

        var sum = 0L;

        while (!leftList.isEmpty() && !rightList.isEmpty()) {
            Long leftNext = leftList.poll();
            Long rightNext = rightList.poll();
            sum += Math.abs(leftNext - rightNext);
            System.out.println("Pairing " + leftNext + " and " + rightNext + ": " + Math.abs(leftNext - rightNext));
        }
        return sum + "";
    }

    @Override
    public String partTwo(List<String> input) {
        var leftList = new PriorityQueue<Long>();
        var rightList = new PriorityQueue<Long>();

        for (String s : input) {
            var values = Arrays.stream(s.split(" +")).mapToLong(Long::parseLong).toArray();
            leftList.add(values[0]);
            rightList.add(values[1]);
        }

        return leftList.stream()
                .mapToLong(l -> rightList.stream().filter(r -> r.equals(l)).count() * l)
                .sum() + "";
    }
}
