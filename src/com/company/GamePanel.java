package com.company;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;



import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;



public class GamePanel extends JPanel implements KeyListener {
    static final int BLOCK_SIZE = 24;
    int numBlocksWidth;
    int numBlocksHeight;
    int[][] levelData;
    int pacmanX;
    int pacmanY;
    int score;
    boolean running;

    private int ghost1X;
    private int ghost1Y;
    private int ghost2X;
    private int ghost2Y;

    private String nickname = "";


    public GamePanel(int numBlocksWidth, int numBlocksHeight) {
        this.numBlocksWidth = numBlocksWidth;
        this.numBlocksHeight = numBlocksHeight;

        levelData = new int[numBlocksHeight][numBlocksWidth];
        generateMaze();
        initializePacmanPosition();
        initializeScore();

        int panelWidth = numBlocksWidth * BLOCK_SIZE;
        int panelHeight = numBlocksHeight * BLOCK_SIZE;
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setBackground(Color.BLACK);

        setFocusable(true);
        addKeyListener(this);
        running = true;
        Timer ghostTimer = new Timer(500, e -> moveGhostsRandomly());
        ghostTimer.start();
    }

    private void generateMaze() {
        Maze mazeGenerator = new Maze(numBlocksWidth, numBlocksHeight);
        levelData = mazeGenerator.generateMaze();
        addDotsToMaze();
        levelData[0][0] = 0;
        levelData[0][1] = 0;
        levelData[1][0] = 0;
        levelData[1][1] = 0;
    }



    private void initializePacmanPosition() {
        for (int row = 0; row < numBlocksHeight; row++) {
            for (int col = 0; col < numBlocksWidth; col++) {
                if (levelData[row][col] == Maze.PACMAN) {
                    pacmanX = col;
                    pacmanY = row;
                    return;
                }
            }
        }

        ghost1X = numBlocksWidth / 2;
        ghost1Y = numBlocksHeight / 2;
        ghost2X = numBlocksWidth / 2 - 1;
        ghost2Y = numBlocksHeight / 2;
    }

    private void initializeScore() {
        score = 0;
    }

    private void saveScoreToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("score.txt", true))) {
            String scoreData = nickname + ": " + score + "\n";
            writer.write(scoreData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addDotsToMaze() {
        Random random = new Random();
        int maxDots = numBlocksWidth * numBlocksHeight / 10;
        int dotsAdded = 0;

        while (dotsAdded < maxDots) {
            int randomX = random.nextInt(numBlocksWidth);
            int randomY = random.nextInt(numBlocksHeight);

            if (levelData[randomY][randomX] == Maze.EMPTY) {
                levelData[randomY][randomX] = Maze.DOT;
                dotsAdded++;
            }
        }
    }

    public void movePacman(int newX, int newY) {
        if (newX >= 0 && newX < numBlocksWidth && newY >= 0 && newY < numBlocksHeight && levelData[newY][newX] != Maze.WALL) {
            if (levelData[newY][newX] == Maze.DOT) {
                score++;
                levelData[newY][newX] = Maze.EMPTY;
            }

            levelData[pacmanY][pacmanX] = Maze.EMPTY;
            pacmanX = newX;
            pacmanY = newY;
            levelData[pacmanY][pacmanX] = Maze.PACMAN;
            repaint();
        }
    }

    private void drawScore(Graphics g) {
        String scoreText = "Score: " + score;
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fontMetrics = g.getFontMetrics();
        int x = (getWidth() - fontMetrics.stringWidth(scoreText)) / 2;
        int y = BLOCK_SIZE;
        g.drawString(scoreText, x, y);


        g.setColor(Color.WHITE);


        for (int row = 0; row < numBlocksHeight; row++) {
            for (int col = 0; col < numBlocksWidth; col++) {
                if (levelData[row][col] == Maze.DOT) {
                    int dotX = col * BLOCK_SIZE + BLOCK_SIZE / 2 - 3;
                    int dotY = row * BLOCK_SIZE + BLOCK_SIZE / 2 - 3;
                    g.fillOval(dotX, dotY, 6, 6);
                }
            }
        }

        int pacmanXPixel = pacmanX * BLOCK_SIZE;
        int pacmanYPixel = pacmanY * BLOCK_SIZE;

        //pacman
        g.setColor(Color.YELLOW);
        g.fillArc(pacmanXPixel, pacmanYPixel, BLOCK_SIZE, BLOCK_SIZE, 45, 270);
    }

    private void drawGhosts(Graphics g) {

        int ghost1XPixel = ghost1X * BLOCK_SIZE;
        int ghost1YPixel = ghost1Y * BLOCK_SIZE;
        g.setColor(Color.RED);
        g.fillRect(ghost1XPixel, ghost1YPixel, BLOCK_SIZE, BLOCK_SIZE);

        int ghost2XPixel = ghost2X * BLOCK_SIZE;
        int ghost2YPixel = ghost2Y * BLOCK_SIZE;
        g.setColor(Color.PINK);
        g.fillRect(ghost2XPixel, ghost2YPixel, BLOCK_SIZE, BLOCK_SIZE);
    }

    private void moveGhostsRandomly() {

        Random random = new Random();

        // Ghost 1
        int ghost1Direction = random.nextInt(4) + 1;
        int ghost1NewX = ghost1X;
        int ghost1NewY = ghost1Y;

        switch (ghost1Direction) {
            case 1:
                ghost1NewY -= 1;
                break;
            case 2:
                ghost1NewX += 1;
                break;
            case 3:
                ghost1NewY += 1;
                break;
            case 4:
                ghost1NewX -= 1;
                break;
        }

        if (ghost1NewX >= 0 && ghost1NewX < numBlocksWidth && ghost1NewY >= 0 && ghost1NewY < numBlocksHeight
                && levelData[ghost1NewY][ghost1NewX] != Maze.WALL) {
            ghost1X = ghost1NewX;
            ghost1Y = ghost1NewY;
        }

        // Ghost 2
        int ghost2Direction = random.nextInt(4) + 1;
        int ghost2NewX = ghost2X;
        int ghost2NewY = ghost2Y;

        switch (ghost2Direction) {
            case 1:
                ghost2NewY -= 1;
                break;
            case 2:
                ghost2NewX += 1;
                break;
            case 3:
                ghost2NewY += 1;
                break;
            case 4:
                ghost2NewX -= 1;
                break;
        }

        if (ghost2NewX >= 0 && ghost2NewX < numBlocksWidth && ghost2NewY >= 0 && ghost2NewY < numBlocksHeight
                && levelData[ghost2NewY][ghost2NewX] != Maze.WALL) {
            ghost2X = ghost2NewX;
            ghost2Y = ghost2NewY;
        }

        if (pacmanX == ghost1X && pacmanY == ghost1Y || pacmanX == ghost2X && pacmanY == ghost2Y) {
            JOptionPane.showMessageDialog(this, "Game Over", "Game Over", JOptionPane.INFORMATION_MESSAGE);

        }

        repaint();
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int row = 0; row < numBlocksHeight; row++) {
            for (int col = 0; col < numBlocksWidth; col++) {
                int tile = levelData[row][col];
                int x = col * BLOCK_SIZE;
                int y = row * BLOCK_SIZE;

                if (tile == Maze.WALL) {
                    g.setColor(Color.BLUE);
                    g.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
        drawGhosts(g);
        drawScore(g);
    }

    @Override
    public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_UP) {
                movePacman(pacmanX, pacmanY - 1);
            } else if (keyCode == KeyEvent.VK_DOWN) {
                movePacman(pacmanX, pacmanY + 1);
            } else if (keyCode == KeyEvent.VK_LEFT) {
                movePacman(pacmanX - 1, pacmanY);
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                movePacman(pacmanX + 1, pacmanY);
            }
            if (pacmanX == ghost1X && pacmanY == ghost1Y || pacmanX == ghost2X && pacmanY == ghost2Y) {
                running = false;
                JOptionPane.showMessageDialog(this, "Game Over!");
            }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}