package brickGame.controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

/**
 * The BackgroundMusicController class manages background music playback in the game.
 * It handles initialization, control, and state management of the music.
 */
public class BackgroundMusicController {

    // File path for the music file.
    private final String filePath;
    // MediaPlayer instance to play the music.
    private MediaPlayer mediaPlayer;

    /**
     * Constructor to initialize the controller with a file path for the music.
     *
     * @param filePath The file path of the music file to be played.
     */
    public BackgroundMusicController(String filePath) {
        this.filePath = filePath;
        initMediaPlayer();
    }

    /**
     * Checks whether the background music is currently muted.
     *
     * @return True if the music is muted, false otherwise.
     */
    public boolean isMusicMuted() {
        return getMediaPlayer() != null && getMediaPlayer().isMute();
    }

    /**
     * Retrieves the file path of the music file.
     *
     * @return The file path of the music file.
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Retrieves the MediaPlayer instance used for playing the music.
     *
     * @return The MediaPlayer instance.
     */
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    /**
     * Sets the MediaPlayer instance for music playback.
     *
     * @param mediaPlayer The MediaPlayer instance to be set.
     */
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    /**
     * Initializes the MediaPlayer object with the music file specified by filePath.
     */
    private void initMediaPlayer() {
        Media media = new Media(new File(getFilePath()).toURI().toString());
        setMediaPlayer(new MediaPlayer(media));
        getMediaPlayer().setCycleCount(MediaPlayer.INDEFINITE); // For looping
    }

    /**
     * Starts playing the background music. The music will loop indefinitely.
     */
    public void playMusic() {
        if (getMediaPlayer() != null) {
            getMediaPlayer().play();
        }
    }

    /**
     * Stops the background music.
     */
    public void stopMusic() {
        if (getMediaPlayer() != null) {
            getMediaPlayer().stop();
        }
    }

    /**
     * Mutes the background music.
     */
    public void muteMusic() {
        if (getMediaPlayer() != null) {
            getMediaPlayer().setMute(true);
        }
    }

    /**
     * Unmutes the background music.
     */
    public void unMuteMusic() {
        if (getMediaPlayer() != null) {
            getMediaPlayer().setMute(false);
        }
    }
}
