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

/**
 * Controller class responsible for managing the User Interface in a JavaFX application.
 * It handles the initialization and switching of different scenes like the menu, game, win, and game over scenes.
 */
public class UIController {
    private static final Logger logger = LoggerFactory.getLogger(UIController.class);
    private final Main main;

    /**
     * Constructor initializing with the main application instance.
     *
     * @param main The main application instance used throughout the game.
     */
    public UIController(Main main) {
        this.main = main;
    }

    /**
     * Initializes UI elements and sets up the primary stage.
     * This method is responsible for loading the initial menu scene.
     *
     * @param primaryStage The primary stage of the application.
     * @throws IOException If the FXML file for the menu scene cannot be loaded.
     */
    public void start(Stage primaryStage) throws IOException {
        // Load the menu scene.
        main.setLoader(new FXMLLoader(main.getClass().getResource("menu.fxml")));
        main.setRoot(main.getLoader().load());

        // Get and set the controller for the menu scene.
        MenuController menuController = main.getLoader().getController(); // Get the controller instance
        menuController.setMainApp(main);

        // Set up the primary stage with title, scene, and stylesheet.
        primaryStage.setTitle("Brick Breaker");
        main.setScene(new Scene(main.getRoot()));
        main.getScene().getStylesheets().add("style.css");
        primaryStage.setScene(main.getScene());
        primaryStage.show();
    }

    /**
     * Logic to switch to the game scene. This method loads the game scene and initializes necessary game components.
     *
     * @throws IOException If the FXML file for the game scene cannot be loaded.
     */
    public void switchToGameScene() throws IOException {
        // Load the game scene and set the root.
        main.setLoader(new FXMLLoader(main.getClass().getResource("game.fxml")));
        Parent gameRoot = main.getLoader().load();
        main.getScene().setRoot(gameRoot);
        main.getScene().setOnKeyPressed(main.getGameController());
        main.setRoot((Pane) gameRoot);

        // Initialize and start the game controller.
        main.setGameController(new GameController(main, 0, 0, 5));
        main.getGameController().onInit();
    }

    /**
     * Displays the win scene after a successful game completion.
     * This method is responsible for handling the transition to the win scene and saving the game score.
     */
    public void showWin() {
        try {
            // Reset game state, save leaderboard, and load win scene.
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

    /**
     * Displays the game over scene. This method is called when the game ends and the player loses.
     * It handles the transition to the game over scene and saves the game score.
     */
    public void showGameOver() {
        try {
            // Reset game state, save leaderboard, and load game over scene.
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

    /**
     * Switches to the leaderboard scene. This method is responsible for loading and displaying the leaderboard.
     *
     * @throws IOException If the FXML file for the leaderboard scene cannot be loaded.
     */
    public void switchToLeaderboard() throws IOException {
        main.setLoader(new FXMLLoader(main.getClass().getResource("leaderboard.fxml")));
        Parent gameRoot = main.getLoader().load();
        LeaderboardController leaderboardController = main.getLoader().getController(); // Get the controller instance
        leaderboardController.setMainApp(main);
        main.getScene().setRoot(gameRoot);
    }

    /**
     * Switches back to the main menu scene. This method handles the transition back to the main menu.
     *
     * @throws IOException If the FXML file for the menu scene cannot be loaded.
     */
    public void switchToMenu() throws IOException {
        main.setLoader(new FXMLLoader(main.getClass().getResource("menu.fxml")));
        Parent gameRoot = main.getLoader().load();
        MenuController menuController = main.getLoader().getController(); // Get the controller instance
        menuController.setMainApp(main);
        main.getScene().setRoot(gameRoot);
    }
}