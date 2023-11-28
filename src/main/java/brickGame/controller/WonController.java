package brickGame.controller;

import brickGame.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class WonController {
    public Pane backgroundPane;
    public Label score;
    private Main mainApp; // Reference to the main application instance
    public Button startGameButton;
    public Button goToMenuButton;

    private int finalScore;

    public void setMainApp(Main mainApp, int totalScore) {
        this.mainApp = mainApp;
        this.finalScore = totalScore;
        // Update the score label here if needed
        score.setText(String.valueOf(finalScore));
    }

    @FXML
    private void startGame() throws IOException {
        // Assuming MainApplication has a method to switch scenes
        mainApp.switchToGameScene();
    }

    @FXML
    private void goToMenu() throws IOException {
        // Assuming MainApplication has a method to switch scenes
        mainApp.switchToMenu();
    }
}
