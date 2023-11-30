package brickGame;

import brickGame.controller.*;
import brickGame.engine.GameEngine;
import brickGame.model.Ball;
import brickGame.model.Block;
import brickGame.model.Paddle;
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

    // Game variables
    public long time, goldTime;
    public int destroyedBlockCount;
    public boolean loadFromSave;

    private Ball ball;
    private Paddle paddle;
    public Scene scene;
    public Pane root;
    private Label scoreLabel, heartLabel, levelLabel;
    private Stage primaryStage;
    FXMLLoader loader;
    private GameEngine engine;
    private GameController gameController;
    private FileController fileController;

    private final int finalLevel = 10;
    private int totalScore = 0;

    // Initialize the game and UI elements
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
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

    // Method to prepare for the next game level
    public void prepareForNextLevel() {
        try {
            // Reset variables for the next level
            ball.setVX(1.000);
            engine.stop();
            ball.resetCollisionStates();
            ball.setGoDownBall(true);
            gameController.setExistHeartBlock(false);
            time = 0;
            gameController.blocks.clear();
            gameController.bonuses.clear();
            destroyedBlockCount = 0;

            // Call the initialization logic directly
            setupNewGameLevel();

        } catch (Exception e) {
            logger.error("An error occurred in prepareForNextLevel() Method: " + e.getMessage(), e);
        }
    }

    // Extract common initialization logic into a separate method
    private void setupNewGameLevel() {
        // Add the initialization logic here
        gameController.levelUp(this);
        if (gameController != null) {
            initializeGameElements();
            initializeAndStartGameEngine();
            root.getChildren().clear();
            setUpGameUI();
            scene.setOnKeyPressed(gameController);
        }
    }

    public void initializeGameElements() {
        ball = gameController.initBall();
        paddle = gameController.initPaddle();
        gameController.initBoard(gameController.blocks);
    }

    public void setUpGameUI() {
        scoreLabel = new Label("Score: " + gameController.getScore());
        levelLabel = new Label("Level: " + gameController.getLevel());
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

        root.getChildren().addAll(paddle, ball, scoreLabel, heartLabel, levelLabel);

        for (Block block : gameController.blocks) {
            if (block.rect != null && !block.isDestroyed) {
                root.getChildren().add(block.rect);
            }
        }
    }

    public void initializeAndStartGameEngine() {
        engine = new GameEngine(120);
        engine.setOnAction(gameController);
        engine.start();
    }

    // Method to restart the game from the beginning
    public void resetGameToStart() {
        try {
            // Reset game variables for a fresh start
            engine.stop();
            totalScore = gameController.getScore();
            gameController.blocks.clear();
            gameController.bonuses.clear();
            ball = null;
            gameController = null;
            destroyedBlockCount = 0;
            time = 0;
            goldTime = 0;
        } catch (Exception e) {
            logger.error("An error occurred in resetGameToStart() Method: " + e.getMessage(), e);
        }
    }


    public void showWin() {
        try {
            loader = new FXMLLoader(getClass().getResource("won.fxml"));
            Parent gameRoot = loader.load();
            WonController wonController = loader.getController(); // Get the controller instance
            wonController.setMainApp(this, totalScore);
            scene.setRoot(gameRoot);
            scene.setOnKeyPressed(gameController);
        } catch (IOException e) {
            // Log the exception details or handle it appropriately
            logger.error("An error occurred in showWin() method: " + e.getMessage(), e);
        }
    }

    public void showGameOver() {
        try {
            resetGameToStart();
            loader = new FXMLLoader(getClass().getResource("gameover.fxml"));
            Parent gameRoot = loader.load();
            GameOverController gameOverController = loader.getController(); // Get the controller instance
            gameOverController.setMainApp(this, totalScore);
            scene.setRoot(gameRoot);
            scene.setOnKeyPressed(gameController);
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
