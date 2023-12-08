package brickGame.controller;

import brickGame.LoadSave;
import brickGame.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;

/**
 * Controller class for the game over screen. It manages the UI elements displayed when the game ends,
 * including the final score, best score, and buttons for starting a new game or returning to the menu.
 */
public class GameOverController {
    public Pane backgroundPane;
    public Label score;
    public Label bestScore;
    private Main mainApp; // Reference to the main application instance
    public Button startGameButton;
    public Button goToMenuButton;
    @FXML
    private Button muteButton;
    @FXML
    private Button unmuteButton;
    private final IntegerProperty finalScoreProperty = new SimpleIntegerProperty(this, "finalScore", 0);
    private final BackgroundMusicController bgm = new BackgroundMusicController("src/main/resources/game-over-music.mp3");


    /**
     * Sets the main application instance and updates the score display. Initializes music control and button visibility.
     *
     * @param mainApp The main application instance.
     * @param totalScore The final score achieved in the game.
     */
    public void setMainApp(Main mainApp, int totalScore) {
        this.mainApp = mainApp;
        score.setText(String.valueOf(totalScore));
        updateFinalScore();
        bestScore.textProperty().bind(finalScoreProperty.asString("Best Score:                                 %d"));
        bgm.playMusic();
        if(mainApp.isMuted()){
            bgm.muteMusic();
        }
        updateButtonVisibility();
    }

    /**
     * Handles the action to start a new game. Stops the music and triggers the scene switch to the game scene.
     *
     * @throws IOException If there is an error switching scenes.
     */
    @FXML
    private void startGame() throws IOException {
        bgm.stopMusic();
        mainApp.switchToGameScene();
    }

    /**
     * Handles the action to go to the main menu. Stops the music and triggers the scene switch to the menu.
     *
     * @throws IOException If there is an error switching scenes.
     */

    @FXML
    private void goToMenu() throws IOException {
        bgm.stopMusic();
        mainApp.switchToMenu();
    }

    /**
     * Updates the final score display based on the best score from the leaderboard.
     */
    public void updateFinalScore() {
        LoadSave score = new LoadSave();
        score.readLeaderboard(); // Read leaderboard data
        finalScoreProperty.set(score.getBestScore()); // Update the best score property
    }

    /**
     * Mutes the game music and updates the visibility of the mute and unmute buttons.
     */
    @FXML
    private void muteAction() {
        bgm.muteMusic();
        mainApp.setMuted(true); // Set the boolean to true when mute button is clicked
        updateButtonVisibility();
    }

    /**
     * Unmutes the game music and updates the visibility of the mute and unmute buttons.
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
