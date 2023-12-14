package com.company;

import java.awt.*;

public class Pacman extends Creature {
    public void move(int newX, int newY, GamePanel gamePanel) {
        if (!gamePanel.isAbleToMove(newX, newY)) {
            return;
        }

        this.setX(newX);
        this.setY(newY);

        if (gamePanel.getLevelData()[this.getY()][this.getX()] == Maze.DOT) {
            gamePanel.setLevelData(this.getY(), this.getX(), Maze.EMPTY);
            int newScore = gamePanel.getScore() + 1;
            gamePanel.setScore(newScore);
        }
    }

    public void draw(Graphics g, int blockSize) {
        int xPixel = this.getX() * blockSize;
        int yPixel = this.getY() * blockSize;

        g.setColor(Color.YELLOW);
        g.fillOval(xPixel, yPixel, blockSize, blockSize);
    }
}
