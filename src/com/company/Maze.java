package com.company;

import java.util.Random;

public class Maze {
    private final int width;
    private final int height;
    private final int[][] maze;

    public static final int WALL = 1;
    public static final int PACMAN = 2;
    public static final int EMPTY = 0;
    public static final int DOT = 3;


    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        this.maze = new int[height][width];
    }

    public int[][] generateMaze() {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                this.maze[y][x] = WALL;
            }
        }

        Random random = new Random();
        int startX = random.nextInt(this.width);
        int startY = random.nextInt(this.height);

        generatePath(startX, startY);
        addRandomRoads();
        return this.maze;
    }


    private void addRandomRoads() {
        Random random = new Random();
        int maxRoads = this.width * this.height / 14;
        int roadsAdded = 0;

        while (roadsAdded < maxRoads) {
            int randomX = random.nextInt(this.width);
            int randomY = random.nextInt(this.height);

            if (this.maze[randomY][randomX] == WALL) {
                this.maze[randomY][randomX] = EMPTY;
                roadsAdded++;
            }
        }
    }

    /*public void addRandomDots(int[][] maze) {
        Random random = new Random();
        int maxRoads = width * height / 20;
        int dotsAdded = 0;

        while (dotsAdded < maxRoads) {
            int randomX = random.nextInt(width);
            int randomY = random.nextInt(height);

            if (maze[randomY][randomX] == EMPTY) {
                maze[randomY][randomX] = DOT;
                dotsAdded++;
            }
        }
    }*/

    private void generatePath(int x, int y) {
        this.maze[y][x] = EMPTY;

        int[] directions = {1, 2, 3, 4};
        shuffleArray(directions);

        for (int direction : directions) {
            int newX = x;
            int newY = y;

            switch (direction) {
                case 1 -> newY -= 2;
                case 2 -> newX += 2;
                case 3 -> newY += 2;
                case 4 -> newX -= 2;
            }

            boolean isInsideBorder = (newX >= 0 && newX < width) && (newY >= 0 && newY < height);

            if (isInsideBorder) {
                boolean currentFieldIsWall = (maze[newY][newX] == WALL);
                if (currentFieldIsWall) {
                    int wallX = x + (newX - x) / 2;
                    int wallY = y + (newY - y) / 2;
                    this.maze[newY][newX] = EMPTY;
                    this.maze[wallY][wallX] = EMPTY;
                    this.generatePath(newX, newY);
                }
            }
        }
    }


    private void shuffleArray(int[] array) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
}
