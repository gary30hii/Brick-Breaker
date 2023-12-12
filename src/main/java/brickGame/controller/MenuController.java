package brickGame.controller;

import brickGame.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Controller class for the main menu. It manages the interactions on the main menu screen,
 * such as starting a new game, loading a saved game, navigating to the leaderboard,
 * and controlling the game's music.
 */
public class MenuController {
    private Main mainApp; // Reference to the main application instance
    public Pane backgroundPane;
    public Button startGameButton;
    public Button loadGameButton;
    public Button scoreButton;
    @FXML
    private Button muteButton;
    @FXML
    private Button unmuteButton;
    private final BackgroundMusicController menuMusic = new BackgroundMusicController("src/main/resources/menu-music.mp3");


    /**
     * Sets the main application instance. Initializes and plays the menu music,
     * and updates the visibility of the mute/unmute buttons based on the application's current state.
     *
     * @param mainApp The main application instance to be used by the menu controller.
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        menuMusic.playMusic();
        if (mainApp.isMuted()) {
            menuMusic.muteMusic();
        }
        updateButtonVisibility();
    }

    /**
     * Starts a new game. Stops the menu music and switches to the game scene.
     *
     * @throws IOException If an I/O error occurs during scene switching.
     */
    @FXML
    private void startGame() throws IOException {
        menuMusic.stopMusic();
        mainApp.switchToGameScene();
    }

    /**
     * Loads a saved game. Stops the menu music and delegates to the startGame method to switch scenes.
     *
     * @throws IOException If an I/O error occurs during scene switching.
     */
    @FXML
    private void loadGame() throws IOException {
        menuMusic.stopMusic();
        mainApp.loadFromSave = true;
        startGame();
    }

    /**
     * Navigates to the leaderboard. Stops the menu music and switches to the leaderboard scene.
     *
     * @throws IOException If an I/O error occurs during scene switching.
     */
    @FXML
    private void goToLeaderboard() throws IOException {
        menuMusic.stopMusic();
        mainApp.switchToLeaderboard();
    }

    /**
     * Mutes the game music and updates the visibility of the mute and unmute buttons.
     */
    @FXML
    private void muteAction() {
        menuMusic.muteMusic();
        mainApp.setMuted(true); // Set the boolean to true when mute button is clicked
        updateButtonVisibility();
    }

    /**
     * Unmutes the game music and updates the visibility of the mute and unmute buttons.
     */
    @FXML
    private void unmuteAction() {
        menuMusic.unMuteMusic();
        mainApp.setMuted(false); // Set the boolean to false when unmute button is clicked
        updateButtonVisibility();
    }

    /**
     * Updates the visibility of the mute and unmute buttons based on the muted state of the application.
     */
    private void updateButtonVisibility() {
        if (mainApp != null) {
            muteButton.setVisible(!mainApp.isMuted()); // Show muteButton if not muted
            unmuteButton.setVisible(mainApp.isMuted()); // Show unmuteButton if muted
        }
    }
}