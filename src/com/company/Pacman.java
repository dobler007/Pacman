package com.company;

import java.awt.*;

public class Pacman extends Creature {
    public Pacman(GamePanel gamePanel) {
        int[] pacmanInitialCords = findPacmanInitialCords(gamePanel);
        this.setX(pacmanInitialCords[1]);
        this.setY(pacmanInitialCords[0]);
    }

    public void move(int newX, int newY, GamePanel gamePanel) {
        if (!gamePanel.isAbleToMove(newX, newY)) {
            return;
        }

        this.setX(newX);
        this.setY(newY);

        if (gamePanel.levelData[this.getY()][this.getX()] == Maze.DOT) {
            gamePanel.levelData[this.getY()][this.getX()] = Maze.EMPTY;
            gamePanel.score += 1;
        }
    }

    public void draw(Graphics g, int blockSize) {
        int xPixel = this.getX() * blockSize;
        int yPixel = this.getY() * blockSize;

        g.setColor(Color.YELLOW);
        g.fillOval(xPixel, yPixel, blockSize, blockSize);
    }

    private int[] findPacmanInitialCords(GamePanel gamePanel) {
        for (int row = 0; row < gamePanel.numBlocksHeight; row++) {
            for (int col = 0; col < gamePanel.numBlocksWidth; col++) {
                if (gamePanel.levelData[row][col] == Maze.PACMAN) {
                    return new int[]{col, row};
                }
            }
        }
        return new int[] {gamePanel.numBlocksWidth / 2, gamePanel.numBlocksHeight / 2};
    }
}
