package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JFrame {
    private JButton newGame = new JButton("New Game");
    private JButton highScore = new JButton("Highscores");
    private JButton exit = new JButton("Exit");



    public Game() {
        setTitle("Pacman");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        panel.add(newGame);
        panel.add(highScore);
        panel.add(exit);

        add(panel);
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        getContentPane().setBackground(Color.BLUE);

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String widthInput = JOptionPane.showInputDialog("Enter the width of the map:");
                String heightInput = JOptionPane.showInputDialog("Enter the height of the map:");
                int width = Integer.parseInt(widthInput);
                int height = Integer.parseInt(heightInput);

                getContentPane().removeAll();

                GamePanel gamePanel = new GamePanel(width, height);
                getContentPane().add(gamePanel);

                revalidate();
                repaint();

                setSize(width * GamePanel.BLOCK_SIZE, height * GamePanel.BLOCK_SIZE);
                gamePanel.requestFocus();
            }
        });

        setFocusable(true);
    }



}
