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
    static final int THREAD_SLEEP_TIME = 100;
    static final int BLOCK_SIZE = 24;
    private final int numBlocksWidth;
    private final int numBlocksHeight;
    private int[][] levelData;
    private int score;
    private boolean running;
    private Pacman pacman;
    private final List<Ghost> ghosts;
    private String nickname = "";
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;


    public GamePanel(int numBlocksWidth, int numBlocksHeight) {
        this.numBlocksWidth = numBlocksWidth;
        this.numBlocksHeight = numBlocksHeight;

        this.levelData = new int[numBlocksHeight][numBlocksWidth];
        this.generateMaze();
        this.initializePacmanPosition();
        this.score = 0;

        int panelWidth = numBlocksWidth * BLOCK_SIZE;
        int panelHeight = numBlocksHeight * BLOCK_SIZE;
        this.setPreferredSize(new Dimension(panelWidth, panelHeight));
        this.setBackground(Color.BLACK);

        this.setFocusable(true);
        this.addKeyListener(this);
        this.running = true;

        int halfWidth = numBlocksWidth / 2;
        int halfHeight = numBlocksHeight / 2;

        this.pacman = new Pacman();
        this.ghosts = List.of(new Ghost(halfWidth, halfHeight),
                new Ghost(halfWidth - 1, halfHeight));

        Thread movementThread = new Thread(new MovementThread(this));
        movementThread.start();

        Thread enemyThread = new Thread(new EnemyThread(this));
        enemyThread.start();
    }

    public boolean isAbleToMove(int newX, int newY) {
        boolean xInsideBorder = (newX >= 0 && newX < this.numBlocksWidth);
        boolean yInsideBorder = (newY >= 0 && newY < this.numBlocksHeight);

        if (xInsideBorder && yInsideBorder) {
            return (this.levelData[newY][newX] != Maze.WALL);
        }

        return false;
    }

    private void generateMaze() {
        Maze mazeGenerator = new Maze(this.numBlocksWidth, this.numBlocksHeight);
        this.levelData = mazeGenerator.generateMaze();
        this.addDotsToMaze();
        this.levelData[0][0] = 0;
        this.levelData[0][1] = 0;
        this.levelData[1][0] = 0;
        this.levelData[1][1] = 0;
    }

    private void initializePacmanPosition() {
        for (int row = 0; row < this.numBlocksHeight; row++) {
            for (int col = 0; col < this.numBlocksWidth; col++) {
                if (this.levelData[row][col] == Maze.PACMAN) {
                    this.pacman.setX(col);
                    this.pacman.setY(row);
                    return;
                }
            }
        }
    }

    private void saveScoreToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("score.txt", true))) {
            String scoreData = this.nickname + ": " + this.score + "\n";
            writer.write(scoreData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addDotsToMaze() {
        Random random = new Random();
        int maxDots = this.numBlocksWidth * this.numBlocksHeight / 10;
        int dotsAdded = 0;

        while (dotsAdded < maxDots) {
            int randomX = random.nextInt(this.numBlocksWidth);
            int randomY = random.nextInt(this.numBlocksHeight);

            if (this.levelData[randomY][randomX] == Maze.EMPTY) {
                this.levelData[randomY][randomX] = Maze.DOT;
                dotsAdded++;
            }
        }
    }

    private void drawScore(Graphics g) {
        String scoreText = "Score: " + this.score;
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fontMetrics = g.getFontMetrics();
        int x = (getWidth() - fontMetrics.stringWidth(scoreText)) / 2;
        int y = BLOCK_SIZE;
        g.drawString(scoreText, x, y);


        g.setColor(Color.WHITE);


        for (int row = 0; row < this.numBlocksHeight; row++) {
            for (int col = 0; col < this.numBlocksWidth; col++) {
                if (this.levelData[row][col] == Maze.DOT) {
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

    private void drawGhosts(Graphics g) {
        this.ghosts.forEach(ghost -> ghost.draw(g, Color.RED, BLOCK_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int row = 0; row < this.numBlocksHeight; row++) {
            for (int col = 0; col < this.numBlocksWidth; col++) {
                int tile = this.levelData[row][col];
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

        switch (keyCode) {
            case KeyEvent.VK_UP, KeyEvent.VK_W -> this.upPressed = true;
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> this.downPressed = true;
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> this.leftPressed = true;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> this.rightPressed = true;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_UP, KeyEvent.VK_W -> this.upPressed = false;
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> this.downPressed = false;
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> this.leftPressed = false;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> this.rightPressed = false;
        }
    }

    public int getNumBlocksWidth() {
        return this.numBlocksWidth;
    }

    public int getNumBlocksHeight() {
        return this.numBlocksHeight;
    }

    public int[][] getLevelData() {
        return this.levelData;
    }

    public int getScore() {
        return this.score;
    }

    public boolean isRunning() {
        return this.running;
    }

    public Pacman getPacman() {
        return this.pacman;
    }

    public List<Ghost> getGhosts() {
        return this.ghosts;
    }

    public String getNickname() {
        return this.nickname;
    }

    public boolean isUpPressed() {
        return this.upPressed;
    }

    public boolean isDownPressed() {
        return this.downPressed;
    }

    public boolean isLeftPressed() {
        return this.leftPressed;
    }

    public boolean isRightPressed() {
        return this.rightPressed;
    }

    public void setLevelData(int firstIndex, int secondIndex, int value) {
        this.levelData[firstIndex][secondIndex] = value;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setPacman(Pacman pacman) {
        this.pacman = pacman;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
    }

    public void setDownPressed(boolean downPressed) {
        this.downPressed = downPressed;
    }

    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }

    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
    }
}