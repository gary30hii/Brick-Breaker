package brickGame.controller;

import brickGame.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class UIController {
    private static final Logger logger = LoggerFactory.getLogger(UIController.class);
    private final Main main;

    public UIController(Main main) {
        this.main = main;
    }

    // Initialize UI elements
    public void start(Stage primaryStage) throws IOException {
        main.setLoader(new FXMLLoader(main.getClass().getResource("menu.fxml")));
        main.setRoot(main.getLoader().load());

        MenuController menuController = main.getLoader().getController(); // Get the controller instance
        menuController.setMainApp(main);

        primaryStage.setTitle("Brick Breaker");
        main.setScene(new Scene(main.getRoot()));
        main.getScene().getStylesheets().add("style.css");

        primaryStage.setScene(main.getScene());
        primaryStage.show();
    }

    public void switchToGameScene() throws IOException {
        // Logic to switch to the game scene
        main.setLoader(new FXMLLoader(main.getClass().getResource("game.fxml")));
        Parent gameRoot = main.getLoader().load();
        main.getScene().setRoot(gameRoot);
        main.getScene().setOnKeyPressed(main.getGameController());

        // Casting the root to Pane and adding the circle
        main.setRoot((Pane) gameRoot);

        main.setGameController(new GameController(main, 0, 0, 99));
        main.getGameController().onInit();
    }

    public void showWin() {
        try {
            main.setGameController(null);
            new FileController().saveLeaderboard(main.getFinalScore());
            main.setLoader(new FXMLLoader(main.getClass().getResource("won.fxml")));
            Parent gameRoot = main.getLoader().load();
            WonController wonController = main.getLoader().getController(); // Get the controller instance
            wonController.setMainApp(main, main.getFinalScore());
            main.getScene().setRoot(gameRoot);
        } catch (IOException e) {
            // Log the exception details or handle it appropriately
            logger.error("An error occurred in showWin() method: " + e.getMessage(), e);
        }
    }

    public void showGameOver() {
        try {
            main.getGameController().resetGameToStart();
            main.setGameController(null);
            new FileController().saveLeaderboard(main.getFinalScore());
            main.setLoader(new FXMLLoader(main.getClass().getResource("gameover.fxml")));
            Parent gameRoot = main.getLoader().load();
            GameOverController gameOverController = main.getLoader().getController(); // Get the controller instance
            gameOverController.setMainApp(main, main.getFinalScore());
            main.getScene().setRoot(gameRoot);
        } catch (IOException e) {
            // Log the exception details or handle it appropriately
            logger.error("An error occurred in showGameOver() method: " + e.getMessage(), e);
        }
    }

    public void switchToLeaderboard() throws IOException {
        main.setLoader(new FXMLLoader(main.getClass().getResource("leaderboard.fxml")));
        Parent gameRoot = main.getLoader().load();
        LeaderboardController leaderboardController = main.getLoader().getController(); // Get the controller instance
        leaderboardController.setMainApp(main);
        main.getScene().setRoot(gameRoot);
    }

    public void switchToMenu() throws IOException {
        main.setLoader(new FXMLLoader(main.getClass().getResource("menu.fxml")));
        Parent gameRoot = main.getLoader().load();
        MenuController menuController = main.getLoader().getController(); // Get the controller instance
        menuController.setMainApp(main);
        main.getScene().setRoot(gameRoot);
    }
}