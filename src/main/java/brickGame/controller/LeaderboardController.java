package brickGame.controller;

import brickGame.LoadSave;
import brickGame.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class LeaderboardController {
    public Pane backgroundPane;
    public Button goToMenuButton;
    public Label thirdBestScore;
    public Label secondBestScore;
    public Label bestScore;
    private Main mainApp; // Reference to the main application instance

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        // Read leaderboard data
        LoadSave loadSave = new LoadSave();
        loadSave.readLeaderboard();

        // Update the labels with the leaderboard scores
        bestScore.setText(String.valueOf(loadSave.bestScore));
        secondBestScore.setText(String.valueOf(loadSave.secondBestScore));
        thirdBestScore.setText(String.valueOf(loadSave.thirdBestScore));
    }
    @FXML
    private void goToMenu() throws IOException {
        // Assuming MainApplication has a method to switch scenes
        mainApp.switchToMenu();
    }
}
