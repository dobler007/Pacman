package com.company;

import java.awt.*;
import java.util.Random;

public class Ghost extends Creature {
    public Ghost(int initialX, int initialY) {
        this.setX(initialX);
        this.setY(initialY);
    }

    public void draw(Graphics g, Color colorInput, int blockSize) {
        int xPixel = this.getX() * blockSize;
        int yPixel = this.getY() * blockSize;

        g.setColor(colorInput);
        g.fillRect(xPixel, yPixel, blockSize, blockSize);
    }

    public void moveGhostRandomly(GamePanel gamePanel) {
        Random random = new Random();

        int randomInt = random.nextInt(4) + 1;
        int newX = this.getX();
        int newY = this.getY();

        switch (randomInt) {
            case 1 -> newY -= 1;
            case 2 -> newX += 1;
            case 3 -> newY += 1;
            case 4 -> newX -= 1;
        }

        if (gamePanel.isAbleToMove(newX, newY)) {
            this.setX(newX);
            this.setY(newY);
        }
    }

    public boolean isOnSameFieldAsPacman(GamePanel gamePanel) {
        return (this.getX() == gamePanel.getPacman().getX()) && (this.getY() == gamePanel.getPacman().getY());
    }
}
