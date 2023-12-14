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
import java.util.List;
import java.util.Random;



public class GamePanel extends JPanel implements KeyListener {
    static final int BLOCK_SIZE = 24;
    int numBlocksWidth;
    int numBlocksHeight;
    int[][] levelData;
    int score;
    boolean running;
    Pacman pacman;
    private final List<Ghost> ghosts;
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

        int halfWidth = numBlocksWidth / 2;
        int halfHeight = numBlocksHeight / 2;

        pacman = new Pacman(this);
        ghosts = List.of(new Ghost(halfWidth, halfHeight),
                new Ghost(halfWidth - 1, halfHeight));
    }

    public boolean isAbleToMove(int newX, int newY) {
        boolean xInsideBorder = (newX >= 0 && newX < this.numBlocksWidth);
        boolean yInsideBorder = (newY >= 0 && newY < this.numBlocksHeight);
        boolean isNotWall = (this.levelData[newY][newX] != Maze.WALL);

        return ((xInsideBorder) && (yInsideBorder) && (isNotWall));
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
                    this.pacman.setX(col);
                    this.pacman.setY(row);
                    return;
                }
            }
        }
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
        this.pacman.move(newX, newY, this);
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

        int pacmanXPixel = this.pacman.getX() * BLOCK_SIZE;
        int pacmanYPixel = this.pacman.getY() * BLOCK_SIZE;

        //pacman
        g.setColor(Color.YELLOW);
        g.fillArc(pacmanXPixel, pacmanYPixel, BLOCK_SIZE, BLOCK_SIZE, 45, 270);
    }


    private void moveGhostsRandomly() {
        ghosts.forEach(ghost -> ghost.moveGhostRandomly(this));
        repaint();
    }

    private void drawGhosts(Graphics g) {
        this.ghosts.forEach(ghost -> ghost.draw(g, Color.RED, BLOCK_SIZE));
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
                movePacman(pacman.getX(), pacman.getY() - 1);
            } else if (keyCode == KeyEvent.VK_DOWN) {
                movePacman(pacman.getX(), pacman.getY() + 1);
            } else if (keyCode == KeyEvent.VK_LEFT) {
                movePacman(pacman.getX() - 1, pacman.getY());
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                movePacman(pacman.getX() + 1, pacman.getY());
            }

            boolean isPacmanOnGhost = ghosts
                    .stream()
                    .anyMatch(ghost -> ghost.isOnSameFieldAsPacman(this));
            if (isPacmanOnGhost) {
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