package brickGame.controller;

import brickGame.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class LeaderboardController {
    public Pane backgroundPane;
    public Button goToMenuButton;
    private Main mainApp; // Reference to the main application instance

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        // Update the score label here if needed
    }
    @FXML
    private void goToMenu() throws IOException {
        // Assuming MainApplication has a method to switch scenes
        mainApp.switchToMenu();
    }
}
