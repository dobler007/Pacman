package com.company;

public class EnemyThread implements Runnable {
    private final GamePanel gamePanel;

    EnemyThread(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        // 500 Milliseconds delay before the ghosts start moving
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (this.gamePanel.isRunning()) {
            for (Ghost ghost : this.gamePanel.getGhosts()) {
                ghost.moveGhostRandomly(this.gamePanel);
            }
            try {
                Thread.sleep(GamePanel.THREAD_SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}