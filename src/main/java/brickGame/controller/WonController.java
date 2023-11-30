package brickGame.controller;

import brickGame.LoadSave;
import brickGame.Main;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class WonController {
    public Pane backgroundPane;
    public Label score;
    public Button scoreButton;
    public Label bestScore;
    private Main mainApp; // Reference to the main application instance
    public Button startGameButton;
    public Button goToMenuButton;
    private final IntegerProperty finalScoreProperty = new SimpleIntegerProperty(this, "finalScore", 0);


    public void setMainApp(Main mainApp, int totalScore) {
        this.mainApp = mainApp;
        score.setText(String.valueOf(totalScore));
        updateFinalScore();
        bestScore.textProperty().bind(finalScoreProperty.asString("Best Score:                                 %d"));
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

    @FXML
    private void goToLeaderboard() throws IOException {
        // Assuming MainApplication has a method to switch scenes
        mainApp.switchToLeaderboard();
    }

    public void updateFinalScore() {
        // Call this method whenever you want to update the score
        LoadSave score = new LoadSave();
        score.readLeaderboard();
        finalScoreProperty.set(score.bestScore);
    }
}
