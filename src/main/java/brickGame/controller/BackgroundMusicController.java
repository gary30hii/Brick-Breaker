package brickGame.controller;

import javazoom.jl.player.Player;
import java.io.FileInputStream;
import java.util.concurrent.atomic.AtomicBoolean;

public class BackgroundMusicController {
    private final String filePath;
    private Player player;
    private AtomicBoolean isMuted = new AtomicBoolean(false);
    private AtomicBoolean gameRunning = new AtomicBoolean(true);
    private Thread musicThread;

    public BackgroundMusicController(String filePath) {
        this.filePath = filePath;
    }

    public void playMusic() {
        gameRunning.set(true);
        if (musicThread == null || !musicThread.isAlive()) {
            musicThread = new Thread(() -> {
                try {
                    while (gameRunning.get()) {
                        if (!isMuted.get()) {
                            FileInputStream fis = new FileInputStream(filePath);
                            player = new Player(fis);
                            player.play();
                            fis.close(); // Close the stream after playing
                        }

                        if (isMuted.get() || !gameRunning.get()) {
                            break; // Break out of the loop if muted or game stopped
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Problem playing file " + filePath);
                } finally {
                    closePlayer();
                }
            });

            musicThread.start();
        }
    }

    private synchronized void closePlayer() {
        if (player != null) {
            player.close();
            player = null; // Nullify the player to avoid reuse
        }
    }

    public synchronized void stopMusic() {
        gameRunning.set(false);
        closePlayer();
        if (musicThread != null) {
            musicThread.interrupt(); // Interrupt the thread
        }
    }

    public synchronized void muteMusic() {
        isMuted.set(true);
        closePlayer();
    }

    public synchronized void unmuteMusic() {
        isMuted.set(false);
        playMusic(); // Restart music when unmuted
    }

    public void setGameRunning(boolean running) {
        gameRunning.set(running);
        if (!running) {
            stopMusic(); // Stop music when game is not running
        } else {
            playMusic(); // Resume music when game restarts
        }
    }
}
