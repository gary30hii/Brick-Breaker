package brickGame.controller;

import brickGame.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MenuController {
    private Main mainApp; // Reference to the main application instance
    public Pane backgroundPane;
    public Button startGameButton;
    public Button loadGameButton;
    public Button scoreButton;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void startGame() throws IOException {
        // Assuming MainApplication has a method to switch scenes
        mainApp.switchToGameScene();
    }

    @FXML
    private void loadGame() throws IOException {
        mainApp.loadFromSave = true;
        startGame();
    }

}