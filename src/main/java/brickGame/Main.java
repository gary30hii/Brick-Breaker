package brickGame;

import brickGame.controller.*;
import brickGame.model.Block;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main extends Application {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    // Constants
    public static final int SCENE_WIDTH = 500, SCENE_HEIGHT = 700;

    //UI variable
    private Label scoreLabel;
    private Label heartLabel;
    public Scene scene;
    public Pane root;
    private FXMLLoader loader;

    // Game variables
    private GameController gameController;
    public boolean loadFromSave;
    public int finalScore = 0;

    // Initialize UI elements
    @Override
    public void start(Stage primaryStage) throws IOException {
        loader = new FXMLLoader(getClass().getResource("menu.fxml"));
        root = loader.load();

        MenuController menuController = loader.getController(); // Get the controller instance
        menuController.setMainApp(this);

        primaryStage.setTitle("Brick Breaker");
        scene = new Scene(root);
        scene.getStylesheets().add("style.css");

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void switchToGameScene() throws IOException {
        // Logic to switch to the game scene
        loader = new FXMLLoader(getClass().getResource("game.fxml"));
        Parent gameRoot = loader.load();
        scene.setRoot(gameRoot);
        scene.setOnKeyPressed(gameController);

        // Casting the root to Pane and adding the circle
        root = (Pane) gameRoot;

        gameController = new GameController(this, 0, 0, 5);
        gameController.onInit();
    }

    // Main method to launch the application
    public static void main(String[] args) {
        launch(args);
    }

    public void updateGameData(int score, int heart){
        scoreLabel.setText("Score: " + score);
        heartLabel.setText("Heart : " + heart);
    }

    public void setUpGameUI() {
        scoreLabel = new Label("Score: " + gameController.getScore());
        Label levelLabel = new Label("Level: " + gameController.getLevel());
        heartLabel = new Label("Heart : " + gameController.getHeart());

        // Add a style class to each label
        scoreLabel.getStyleClass().add("label-style");
        levelLabel.getStyleClass().add("label-style");
        heartLabel.getStyleClass().add("label-style");

        scoreLabel.setTranslateX(45);
        scoreLabel.setTranslateY(5);
        levelLabel.setTranslateX(45);
        levelLabel.setTranslateY(25);
        heartLabel.setTranslateX(380);
        heartLabel.setTranslateY(5);

        root.getChildren().addAll(gameController.getPaddle(), gameController.getBall(), scoreLabel, heartLabel, levelLabel);

        for (Block block : gameController.getBlocks()) {
            if (block.rect != null && !block.isDestroyed) {
                root.getChildren().add(block.rect);
            }
        }
    }

    public void showWin() {
        try {
            gameController = null;
            new FileController().saveLeaderboard(finalScore);
            loader = new FXMLLoader(getClass().getResource("won.fxml"));
            Parent gameRoot = loader.load();
            WonController wonController = loader.getController(); // Get the controller instance
            wonController.setMainApp(this, finalScore);
            scene.setRoot(gameRoot);
        } catch (IOException e) {
            // Log the exception details or handle it appropriately
            logger.error("An error occurred in showWin() method: " + e.getMessage(), e);
        }
    }

    public void showGameOver() {
        try {
            gameController.resetGameToStart();
            gameController = null;
            new FileController().saveLeaderboard(finalScore);
            loader = new FXMLLoader(getClass().getResource("gameover.fxml"));
            Parent gameRoot = loader.load();
            GameOverController gameOverController = loader.getController(); // Get the controller instance
            gameOverController.setMainApp(this, finalScore);
            scene.setRoot(gameRoot);
        } catch (IOException e) {
            // Log the exception details or handle it appropriately
            logger.error("An error occurred in showGameOver() method: " + e.getMessage(), e);
        }
    }

    public void switchToLeaderboard() throws IOException {
        loader = new FXMLLoader(getClass().getResource("leaderboard.fxml"));
        Parent gameRoot = loader.load();
        LeaderboardController leaderboardController = loader.getController(); // Get the controller instance
        leaderboardController.setMainApp(this);
        scene.setRoot(gameRoot);
    }


    public void switchToMenu() throws IOException {
        loader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Parent gameRoot = loader.load();
        MenuController menuController = loader.getController(); // Get the controller instance
        menuController.setMainApp(this);
        scene.setRoot(gameRoot);
    }
}
