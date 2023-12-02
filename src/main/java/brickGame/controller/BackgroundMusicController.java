package brickGame.controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class BackgroundMusicController {
    private final String filePath;
    private MediaPlayer mediaPlayer;

    public BackgroundMusicController(String filePath) {
        this.filePath = filePath;
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        Media media = new Media(new File(filePath).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // For looping
    }

    public void playMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void muteMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.setMute(true);
        }
    }

    public void unMuteMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.setMute(false);
        }
    }

    public boolean isMusicMuted() {
        return mediaPlayer != null && mediaPlayer.isMute();
    }
}
