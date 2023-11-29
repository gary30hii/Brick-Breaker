package brickGame;

import brickGame.controller.*;
import brickGame.engine.GameEngine;
import brickGame.model.Ball;
import brickGame.model.Block;
import brickGame.model.Bonus;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    // Constants
    public static final int LEFT = 1, RIGHT = 2, SCENE_WIDTH = 500, SCENE_HEIGHT = 700;
    public static final int PADDLE_WIDTH = 130, PADDLE_HEIGHT = 30, BALL_RADIUS = 10;
    public static final String SAVE_PATH = "data/save.mdds", SAVE_PATH_DIR = "data/";

    // Game variables
    public static double X_PADDLE = (double) SCENE_WIDTH / 2 - (double) PADDLE_WIDTH / 2;
    public static double Y_PADDLE = 640.0;
    public static double X_PADDLE_CENTER;
    public long time, goldTime;
    public int destroyedBlockCount;
    public boolean loadFromSave;

    private Ball ball;
    private Rectangle paddle;
    private Scene scene;
    public Pane root;
    private Label scoreLabel, heartLabel, levelLabel;
    private Stage primaryStage;
    FXMLLoader loader;
    private GameEngine engine;
    private GameController gameController;
    private FileController fileController;
    public final ArrayList<Block> blocks = new ArrayList<>();
    public final ArrayList<Bonus> bonuses = new ArrayList<>();
    private final int finalLevel = 2;
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
        scene.setOnKeyPressed(this);

        // Casting the root to Pane and adding the circle
        root = (Pane) gameRoot;

        initializeGameStage();
    }

    private void initializeGameStage() {
        paddle = new Rectangle();
        fileController = new FileController();
        gameController = new GameController(0, 0, 3, root);
        if (!loadFromSave) {
            levelUp();
            initializeGameElements();
        } else {
            initializeGameElements();
            loadGame();
            loadFromSave = false;
        }
        initializeAndStartGameEngine();
        setUpGameUI();
        initializeGameWindow();

    }

    // Main method to launch the application
    public static void main(String[] args) {
        launch(args);
    }

    // Handle key events for game controls
    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                movePaddle(LEFT);
                break;
            case RIGHT:
                movePaddle(RIGHT);
                break;
            case S:
                fileController.saveCurrentGameState(this, gameController, ball);
                break;
        }
    }

    // Move the paddle left or right
    private void movePaddle(int direction) {
        new Thread(() -> {
            int sleepTime = 1;
            for (int i = 0; i < 30; i++) {
                if (X_PADDLE == (SCENE_WIDTH - PADDLE_WIDTH) && direction == RIGHT) {
                    return; //paddle stop moving to the right when it touches the right wall
                }
                if (X_PADDLE == 0 && direction == LEFT) {
                    return; //paddle stop moving to the left when it touch the left wall
                }
                if (direction == RIGHT) {
                    X_PADDLE++;
                } else {
                    X_PADDLE--;
                }
                X_PADDLE_CENTER = X_PADDLE + (double) PADDLE_WIDTH / 2;
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    logger.error("An error occurred in move() Method: " + e.getMessage(), e);
                }
                if (i >= 20) {
                    sleepTime = i;
                }
            }
        }).start();
    }

    // Method to prepare for the next game level
    private void prepareForNextLevel() {
        Platform.runLater(() -> {
            try {
                // Reset variables for the next level
                ball.setVX(1.000);
                engine.stop();
                ball.resetCollisionStates();
                ball.setGoDownBall(true);
                ball.setGoldStatus(false);
                gameController.setExistHeartBlock(false);
                time = 0;
                goldTime = 0;
                blocks.clear();
                bonuses.clear();
                destroyedBlockCount = 0;

                // Call the initialization logic directly
                setupNewGameLevel();

            } catch (Exception e) {
                logger.error("An error occurred in prepareForNextLevel() Method: " + e.getMessage(), e);
            }
        });
    }

    // Extract common initialization logic into a separate method
    private void setupNewGameLevel() {
        // Add the initialization logic here
        levelUp();
        if (gameController != null) {
            initializeGameElements();
            initializeAndStartGameEngine();
            root.getChildren().clear();
            setUpGameUI();
            initializeGameWindow();
        }
    }

    private void initializeGameElements() {
        ball = gameController.initBall();
        gameController.initPaddle(paddle);
        gameController.initBoard(blocks);
    }

    private void setUpGameUI() {
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
        heartLabel.setTranslateX(SCENE_WIDTH - 120);
        heartLabel.setTranslateY(5);

        root.getChildren().addAll(paddle, ball, scoreLabel, heartLabel, levelLabel);

        for (Block block : blocks) {
            if (block.rect != null && !block.isDestroyed) {
                root.getChildren().add(block.rect);
            }
        }
    }

    private void initializeGameWindow() {
        // Logic to switch to the game scene
        scene.setOnKeyPressed(this);
    }

    public void loadGame() {
        fileController.loadSavedGameState(this, gameController, ball);
    }

    private void initializeAndStartGameEngine() {
        engine = new GameEngine();
        engine.setOnAction(this);
        engine.setFps(120);
        engine.start();
    }

    private void levelUp() {
        gameController.setLevel(gameController.getLevel() + 1);

        if (gameController.getLevel() > 1) {
            new Score().showMessage("Level Up :)", this);
        }

        if (gameController.getLevel() == finalLevel) {
            resetGameToStart();
            showWin();
        }
    }

    // Method to restart the game from the beginning
    public void resetGameToStart() {
        try {
            // Reset game variables for a fresh start
            totalScore = gameController.getScore();
            ball = null;
            gameController = null;
            destroyedBlockCount = 0;
            time = 0;
            goldTime = 0;
            blocks.clear();
            bonuses.clear();
        } catch (Exception e) {
            logger.error("An error occurred in resetGameToStart() Method: " + e.getMessage(), e);
        }
    }

    // Method to handle game updates
    @Override
    public void updateGameFrame() {
        Platform.runLater(() -> {

            if (gameController != null) {
                scoreLabel.setText("Score: " + gameController.getScore());
                heartLabel.setText("Heart : " + gameController.getHeart());
                paddle.setX(X_PADDLE);
                paddle.setY(Y_PADDLE);
                ball.setCenterX(ball.getXBall());
                ball.setCenterY(ball.getYBall());

                for (Bonus choco : bonuses) {
                    choco.choco.setY(choco.y);
                }

                if (destroyedBlockCount == blocks.size() && gameController.getLevel() < finalLevel) {
                    prepareForNextLevel();
                }
            }
        });


        if (ball.getYBall() >= Block.getPaddingTop() && ball.getYBall() <= (Block.getHeight() * (gameController.getLevel() + 1)) + Block.getPaddingTop()) {
            for (final Block block : blocks) {
                int hitCode = block.checkHitToBlock(ball.getXBall(), ball.getYBall(), BALL_RADIUS);
                if (hitCode != Block.NO_HIT) {
                    gameController.setScore(gameController.getScore() + 1);

                    new Score().show(block.x, block.y, 1, this);

                    block.rect.setVisible(false);
                    block.isDestroyed = true;
                    destroyedBlockCount++;
                    ball.resetCollisionStates();

                    if (block.type == Block.BLOCK_CHOCO) {
                        final Bonus choco = new Bonus(block.row, block.column);
                        choco.timeCreated = time;
                        Platform.runLater(() -> root.getChildren().add(choco.choco));
                        bonuses.add(choco);
                    }

                    if (block.type == Block.BLOCK_STAR) {
                        goldTime = time;
                        ball.setFill(new ImagePattern(new Image("goldball.png")));
                        root.getStyleClass().add("goldRoot");
                        ball.setGoldStatus(true);
                    }

                    if (block.type == Block.BLOCK_HEART) {
                        gameController.setHeart(gameController.getHeart() + 1);
                    }

                    if (hitCode == Block.HIT_RIGHT) {
                        ball.setCollideToRightBlock(true);
                    } else if (hitCode == Block.HIT_BOTTOM) {
                        ball.setCollideToBottomBlock(true);
                    } else if (hitCode == Block.HIT_LEFT) {
                        ball.setCollideToLeftBlock(true);
                    } else if (hitCode == Block.HIT_TOP) {
                        ball.setCollideToTopBlock(true);
                    }

                }

                //TODO hit to break and some work here....
                //System.out.println("Break in row:" + block.row + " and column:" + block.column + " hit");
            }
        }
    }

    @Override
    public void onInit() {

    }

    @Override
    public void performPhysicsCalculations() {
        ball.updateBallMovement(this, gameController, engine);

        if (time - goldTime > 5000) {
            ball.setFill(new ImagePattern(new Image("ball.png")));
            root.getStyleClass().remove("goldRoot");
            ball.setGoldStatus(false);
        }

        for (Bonus choco : bonuses) {
            if (choco.y > SCENE_HEIGHT || choco.taken) {
                continue;
            }
            if (choco.y >= Y_PADDLE && choco.y <= Y_PADDLE + PADDLE_HEIGHT && choco.x >= X_PADDLE && choco.x <= X_PADDLE + PADDLE_WIDTH) {
                choco.taken = true;
                choco.choco.setVisible(false);
                gameController.setScore(gameController.getScore() + 3);
                new Score().show(choco.x, choco.y, 3, this);
            }
            choco.y += ((time - choco.timeCreated) / 1000.000) + 1.000;
        }

    }

    @Override
    public void onTime(long time) {
        this.time = time;
    }

    public void showWin() {
        try {
            loader = new FXMLLoader(getClass().getResource("won.fxml"));
            Parent gameRoot = loader.load();
            WonController wonController = loader.getController(); // Get the controller instance
            wonController.setMainApp(this, totalScore);
            scene.setRoot(gameRoot);
            scene.setOnKeyPressed(this);
        } catch (IOException e) {
            // Log the exception details or handle it appropriately
            logger.error("An error occurred in showWin() method: " + e.getMessage(), e);
        }
    }

    public void showGameOver() {
        try {
            engine.stop();
            resetGameToStart();
            loader = new FXMLLoader(getClass().getResource("gameover.fxml"));
            Parent gameRoot = loader.load();
            GameOverController gameOverController = loader.getController(); // Get the controller instance
            gameOverController.setMainApp(this, totalScore);
            scene.setRoot(gameRoot);
            scene.setOnKeyPressed(this);
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
        scene.setOnKeyPressed(this);
    }


    public void switchToMenu() throws IOException {
        loader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Parent gameRoot = loader.load();
        MenuController menuController = loader.getController(); // Get the controller instance
        menuController.setMainApp(this);
        scene.setRoot(gameRoot);
        scene.setOnKeyPressed(this);
    }
}
