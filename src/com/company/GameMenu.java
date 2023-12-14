package com.company;

import javax.swing.*;
import java.awt.*;

public class GameMenu extends JFrame {
    public GameMenu() {
        this.setTitle("Pacman");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        JButton newGame = new JButton("New Game");
        panel.add(newGame);
        JButton highScore = new JButton("Highscores");
        panel.add(highScore);
        JButton exit = new JButton("Exit");
        panel.add(exit);

        this.add(panel);
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.getContentPane().setBackground(Color.BLUE);

        exit.addActionListener((event) -> System.exit(0));

        newGame.addActionListener((event) -> {
            int width = InputUtil.getCorrectInput(this, "Enter the width of the map:",
                    "Please enter a valid width.");
            int height = InputUtil.getCorrectInput(this, "Enter the height of the map:",
                    "Please enter a valid height.");

            this.getContentPane().removeAll();

            GamePanel gamePanel = new GamePanel(width, height);
            this.getContentPane().add(gamePanel);

            this.revalidate();
            this.repaint();

            this.setSize(width * GamePanel.BLOCK_SIZE, height * GamePanel.BLOCK_SIZE);
            gamePanel.requestFocus();
        });

        setFocusable(true);
    }
}
