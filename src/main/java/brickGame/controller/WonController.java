package brickGame.controller;

import brickGame.model.LoadSave;
import brickGame.Main;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Controller class for the "Game Won" scene. It handles the display and functionality of the game's victory screen,
 * allowing the player to view their score, start a new game, return to the main menu, or view the leaderboard.
 */
public class WonController {
    public Pane backgroundPane;
    public Label score;
    public Button scoreButton;
    public Label bestScore;
    private Main mainApp; // Reference to the main application instance
    public Button startGameButton;
    public Button goToMenuButton;
    @FXML
    private Button muteButton;
    @FXML
    private Button unmuteButton;
    private final IntegerProperty finalScoreProperty = new SimpleIntegerProperty(this, "finalScore", 0);
    private final BackgroundMusicController bgm = new BackgroundMusicController("src/main/resources/won-music.mp3");


    /**
     * Sets the main application instance and updates the score display.
     * Initializes the scene with the final score and best score, and starts the background music.
     *
     * @param mainApp    The main application instance.
     * @param totalScore The total score achieved by the player.
     */
    public void setMainApp(Main mainApp, int totalScore) {
        this.mainApp = mainApp;
        score.setText(String.valueOf(totalScore));
        updateFinalScore();
        bestScore.textProperty().bind(finalScoreProperty.asString("Best Score:                                 %d"));
        bgm.playMusic();
        if (mainApp.isMuted()) {
            bgm.muteMusic();
        }
        updateButtonVisibility();
    }

    /**
     * Triggers a scene switch to start a new game.
     * Stops the background music and switches to the game scene.
     *
     * @throws IOException If an I/O error occurs during scene switching.
     */
    @FXML
    private void startGame() throws IOException {
        bgm.stopMusic();
        mainApp.switchToGameScene();
    }

    /**
     * Triggers a scene switch to go to the main menu.
     * Stops the background music and switches to the main menu scene.
     *
     * @throws IOException If an I/O error occurs during scene switching.
     */
    @FXML
    private void goToMenu() throws IOException {
        bgm.stopMusic();
        mainApp.switchToMenu();
    }

    /**
     * Triggers a scene switch to go to the leaderboard.
     * Stops the background music and switches to the leaderboard scene.
     *
     * @throws IOException If an I/O error occurs during scene switching.
     */
    @FXML
    private void goToLeaderboard() throws IOException {
        bgm.stopMusic();
        mainApp.switchToLeaderboard();
    }

    /**
     * Updates the final score from the leaderboard.
     * Reads the best score from the leaderboard and updates the UI accordingly.
     */
    public void updateFinalScore() {
        LoadSave score = new LoadSave();
        score.readLeaderboard();
        finalScoreProperty.set(score.getBestScore());
    }

    /**
     * Mutes the background music and updates the visibility of the mute and unmute buttons.
     */
    @FXML
    private void muteAction() {
        bgm.muteMusic();
        mainApp.setMuted(true); // Set the boolean to true when mute button is clicked
        updateButtonVisibility();
    }

    /**
     * Unmutes the background music and updates the visibility of the mute and unmute buttons.
     */
    @FXML
    private void unmuteAction() {
        bgm.unMuteMusic();
        mainApp.setMuted(false); // Set the boolean to false when unmute button is clicked
        updateButtonVisibility();
    }

    /**
     * Updates the visibility of the mute and unmute buttons based on the muted state of the application.
     */
    private void updateButtonVisibility() {
        muteButton.setVisible(!mainApp.isMuted()); // Show muteButton if not muted
        unmuteButton.setVisible(mainApp.isMuted()); // Show unmuteButton if muted
    }
}
