package com.company;

import javax.swing.*;

public class MovementThread implements Runnable {
    private final GamePanel gamePanel;

    MovementThread(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        while (true) {
            int pacmanX = this.gamePanel.getPacman().getX();
            int pacmanY = this.gamePanel.getPacman().getY();
            if (this.gamePanel.isUpPressed()) {
                this.gamePanel.getPacman().move(pacmanX, pacmanY - 1, this.gamePanel);
            } else if (this.gamePanel.isDownPressed()) {
                this.gamePanel.getPacman().move(pacmanX, pacmanY + 1, this.gamePanel);
            } else if (this.gamePanel.isLeftPressed()) {
                this.gamePanel.getPacman().move(pacmanX - 1, pacmanY, this.gamePanel);
            } else if (this.gamePanel.isRightPressed()) {
                this.gamePanel.getPacman().move(pacmanX + 1, pacmanY, this.gamePanel);
            }
            boolean isPacmanOnGhost = this.gamePanel.getGhosts().stream()
                    .anyMatch(ghost -> ghost.isOnSameFieldAsPacman(gamePanel));
            if (isPacmanOnGhost) {
                this.gamePanel.setRunning(false);
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(gamePanel, "Game Over!"));
                break;
            }

            SwingUtilities.invokeLater(this.gamePanel::repaint);

            try {
                Thread.sleep(GamePanel.THREAD_SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}