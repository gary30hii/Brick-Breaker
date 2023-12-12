package brickGame.controller;

import brickGame.model.LoadSave;
import brickGame.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Controller class for the leaderboard scene, responsible for managing the display of the top scores
 * and providing navigation back to the main menu.
 */
public class LeaderboardController {
    public Pane backgroundPane;
    public Button goToMenuButton;
    public Label thirdBestScore;
    public Label secondBestScore;
    public Label bestScore;
    @FXML
    private Button muteButton;
    @FXML
    private Button unmuteButton;
    private Main mainApp; // Reference to the main application instance
    private final BackgroundMusicController bgm = new BackgroundMusicController("src/main/resources/leaderboard-music.mp3");


    /**
     * Sets the main application instance and updates the leaderboard scores by reading them
     * from the persistent storage. It also initiates the background music for the leaderboard scene.
     *
     * @param mainApp The main application instance which controls the game flow.
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        // Read leaderboard data
        LoadSave loadSave = new LoadSave();
        loadSave.readLeaderboard();

        // Update the labels with the leaderboard scores
        bestScore.setText(String.valueOf(loadSave.getBestScore()));
        secondBestScore.setText(String.valueOf(loadSave.getSecondBestScore()));
        thirdBestScore.setText(String.valueOf(loadSave.getThirdBestScore()));
        bgm.playMusic();
        if (mainApp.isMuted()) {
            bgm.muteMusic();
        }
        updateButtonVisibility();
    }

    /**
     * Handles the action to navigate back to the main menu. Stops the music before switching scenes.
     *
     * @throws IOException If there is an error during scene transition.
     */
    @FXML
    private void goToMenu() throws IOException {
        bgm.stopMusic();
        mainApp.switchToMenu();
    }

    /**
     * Mutes the background music and updates the visibility of the mute and unmute buttons accordingly.
     */
    @FXML
    private void muteAction() {
        bgm.muteMusic();
        mainApp.setMuted(true); // Set the boolean to true when mute button is clicked
        updateButtonVisibility();
    }

    /**
     * Unmutes the background music and updates the visibility of the mute and unmute buttons accordingly.
     */
    @FXML
    private void unmuteAction() {
        bgm.unMuteMusic();
        mainApp.setMuted(false); // Set the boolean to false when unmute button is clicked
        updateButtonVisibility();
    }

    /**
     * Updates the visibility of the mute and unmute buttons based on the muted state of the application.
     * This is called when the mute state is changed.
     */
    private void updateButtonVisibility() {
        muteButton.setVisible(!mainApp.isMuted()); // Show muteButton if not muted
        unmuteButton.setVisible(mainApp.isMuted()); // Show unmuteButton if muted
    }
}
