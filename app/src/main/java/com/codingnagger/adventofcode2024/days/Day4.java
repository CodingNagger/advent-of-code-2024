package com.codingnagger.adventofcode2024.days;

import java.util.List;

public class Day4 implements Day {
    final static char[] XMAS = "XMAS".toCharArray();

    @Override
    public String partOne(List<String> input) {
        var map = input.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);

        var result = 0;

        for (var y = 0; y < map.length; y++) {
            for (var x = 0; x < map[y].length; x++) {
                result += countHorizontalXmas(map, x, y);
                result += countVerticalXmas(map, x, y);
                result += countDiagonalXmas(map, x, y);
            }
        }

        return result + "";
    }

    @Override
    public String partTwo(List<String> input) {
        var map = input.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);

        var result = 0;

        var display = new boolean[map.length][map[0].length];
        for (int i = 0; i < display.length; i++) {
            for (int j = 0; j < display[0].length; j++) {
                display[i][j] = false;
            }
        }

        for (var y = 1; y < map.length - 1; y++) {
            for (var x = 1; x < map[y].length - 1; x++) {
                if (isXmas(map, x, y)) {
                    result++;
                    display[y][x] = true;
                    int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

                    for (int[] direction : directions) {
                        var newX = x + direction[0];
                        int newY = y + direction[1];
                        display[newY][newX] = true;
                    }
                }
            }
        }

        print(map, display);

        return result + "";
    }

    private void print(char[][] map, boolean[][] display) {
        for (var y = 0; y < map.length; y++) {
            for (var x = 0; x < map[y].length; x++) {
                if (display[y][x]) {
                    System.out.print(map[y][x]);
                } else {
                    System.out.print('.');
                }
            }
            System.out.println();
        }
    }

    private boolean isXmas(char[][] map, int x, int y) {
        if (map[y][x] != 'A') {
            return false;
        }

        var mCount = 0;
        var sCount = 0;

        int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        for (int[] direction : directions) {
            var newX = x + direction[0];
            int newY = y + direction[1];

            if (map[newY][newX] == 'M') {
                mCount++;
            } else if (map[newY][newX] == 'S') {
                sCount++;
            }
        }

        return map[y - 1][x - 1] != map[y + 1][x + 1] && mCount == 2 && sCount == 2;
    }

    private int countDiagonalXmas(char[][] map, int x, int y) {
        var found = 0;

        // bottomRight to topLeft
        var bottomRightToTopLeftCursor = 0;
        for (; bottomRightToTopLeftCursor < XMAS.length; bottomRightToTopLeftCursor++) {
            if (y - bottomRightToTopLeftCursor < 0 || x - bottomRightToTopLeftCursor < 0) {
                break;
            }

            if (map[y - bottomRightToTopLeftCursor][x - bottomRightToTopLeftCursor] != XMAS[bottomRightToTopLeftCursor]) {
                break;
            }
        }

        if (bottomRightToTopLeftCursor == XMAS.length) {
            found++;
        }

        // bottomLeft to topRight
        var bottomLeftToTopRightCursor = 0;
        for (; bottomLeftToTopRightCursor < XMAS.length; bottomLeftToTopRightCursor++) {
            if (y - bottomLeftToTopRightCursor < 0 || x + bottomLeftToTopRightCursor >= map[y].length) {
                break;
            }

            if (map[y - bottomLeftToTopRightCursor][x + bottomLeftToTopRightCursor] != XMAS[bottomLeftToTopRightCursor]) {
                break;
            }
        }

        if (bottomLeftToTopRightCursor == XMAS.length) {
            found++;
        }

        // topLeft to bottomRight
        var topLeftToBottomRightCursor = 0;
        for (; topLeftToBottomRightCursor < XMAS.length; topLeftToBottomRightCursor++) {
            if (y + topLeftToBottomRightCursor >= map.length || x + topLeftToBottomRightCursor >= map[y].length) {
                break;
            }

            if (map[y + topLeftToBottomRightCursor][x + topLeftToBottomRightCursor] != XMAS[topLeftToBottomRightCursor]) {
                break;
            }
        }

        if (topLeftToBottomRightCursor == XMAS.length) {
            found++;
        }

        // topRight to bottomLeft
        var topRightToBottomLeftCursor = 0;
        for (; topRightToBottomLeftCursor < XMAS.length; topRightToBottomLeftCursor++) {
            if (y + topRightToBottomLeftCursor >= map.length || x - topRightToBottomLeftCursor < 0) {
                break;
            }

            if (map[y + topRightToBottomLeftCursor][x - topRightToBottomLeftCursor] != XMAS[topRightToBottomLeftCursor]) {
                break;
            }
        }

        if (topRightToBottomLeftCursor == XMAS.length) {
            found++;
        }

        return found;
    }

    private int countVerticalXmas(char[][] map, int x, int y) {
        var found = 0;

        var upToDownCursor = 0;
        for (; upToDownCursor < XMAS.length; upToDownCursor++) {
            if (y + upToDownCursor >= map.length) {
                break;
            }

            if (map[y + upToDownCursor][x] != XMAS[upToDownCursor]) {
                break;
            }
        }

        if (upToDownCursor == XMAS.length) {
            found++;
        }

        var downToUpCursor = 0;
        for (; downToUpCursor < XMAS.length; downToUpCursor++) {
            if (y - downToUpCursor < 0) {
                break;
            }

            if (map[y - downToUpCursor][x] != XMAS[downToUpCursor]) {
                break;
            }
        }

        if (downToUpCursor == XMAS.length) {
            found++;
        }

        return found;
    }

    private int countHorizontalXmas(char[][] map, int x, int y) {
        var found = 0;

        var leftToRightCursor = 0;
        for (; leftToRightCursor < XMAS.length; leftToRightCursor++) {
            if (x + leftToRightCursor >= map[y].length) {
                break;
            }

            if (map[y][x + leftToRightCursor] != XMAS[leftToRightCursor]) {
                break;
            }
        }

        if (leftToRightCursor == XMAS.length) {
            found++;
        }

        var rightToLeftCursor = 0;
        for (; rightToLeftCursor < XMAS.length; rightToLeftCursor++) {
            if (x - rightToLeftCursor < 0) {
                break;
            }

            if (map[y][x - rightToLeftCursor] != XMAS[rightToLeftCursor]) {
                break;
            }
        }

        if (rightToLeftCursor == XMAS.length) {
            found++;
        }

        return found;
    }
}
