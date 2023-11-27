package brickGame;

import brickGame.controller.FileController;
import brickGame.controller.GameController;
import brickGame.controller.MenuController;
import brickGame.controller.Score;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
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
    private Button loadButton, newGameButton;

    private GameEngine engine;
    private GameController gameController;
    private FileController fileController;
    public final ArrayList<Block> blocks = new ArrayList<>();
    public final ArrayList<Bonus> bonuses = new ArrayList<>();
    public final Color[] COLORS = initializeColors();

    // Initialize the game and UI elements
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Parent root = loader.load();

        MenuController menuController = loader.getController(); // Get the controller instance
        menuController.setMainApp(this);

        primaryStage.setTitle("Brick Breaker");
        scene = new Scene(root);
        scene.getStylesheets().add("style.css");

        primaryStage.setScene(scene);
        primaryStage.show();

//        initializeGameStage();
    }
    public void switchToGameScene() throws IOException {
        // Logic to switch to the game scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        Parent gameRoot = loader.load();
        scene.setRoot(gameRoot);

        // Casting the root to Pane and adding the circle
        if (gameRoot instanceof Pane) {
            root = (Pane) gameRoot;

            initializeGameStage();
        } else {
            // Handle if the root is not a Pane
            System.out.println("Root is not a Pane!");
        }
    }
    private void initializeGameStage() {
        paddle = new Rectangle();
        fileController = new FileController();
        if (!loadFromSave) {
            gameController = new GameController(0, 0, 100, root);
            levelUp();
            initializeGameElements();
            setupGameButtons();
        }

        setUpGameUI();
        initializeGameWindow();

        if (!loadFromSave) {
            handleGameControls();
        } else {
            initializeAndStartGameEngine();
            loadFromSave = false;
        }

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
        initializeGameElements();
        setupGameButtons();

        root = new Pane();

        setUpGameUI();
        initializeGameWindow();

        if (!loadFromSave) {
            handleGameControls();
        } else {
            initializeAndStartGameEngine();
            loadFromSave = false;
        }
    }

    private void initializeGameElements() {
        ball = gameController.initBall();
        gameController.initPaddle(paddle);
        gameController.initBoard(blocks);
    }

    private void setupGameButtons() {
        loadButton = new Button("Load Game");
        newGameButton = new Button("Start New Game");
        loadButton.setTranslateX(220);
        loadButton.setTranslateY(300);
        newGameButton.setTranslateX(220);
        newGameButton.setTranslateY(340);
    }

    private void setUpGameUI() {
        scoreLabel = new Label("Score: " + gameController.getScore());
        levelLabel = new Label("Level: " + gameController.getLevel());
        levelLabel.setTranslateY(20);
        heartLabel = new Label("Heart : " + gameController.getHeart());
        heartLabel.setTranslateX(SCENE_WIDTH - 70);

        if (!loadFromSave) {
            root.getChildren().addAll(paddle, ball, scoreLabel, heartLabel, levelLabel, loadButton, newGameButton);
        } else {
            root.getChildren().addAll(paddle, ball, scoreLabel, heartLabel, levelLabel);
        }

        for (Block block : blocks) {
            if (block.rect != null && !block.isDestroyed) {
                root.getChildren().add(block.rect);
            }
        }
    }

    private void initializeGameWindow() {
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.getStylesheets().add("style.css");
        scene.setOnKeyPressed(this);

        primaryStage.setTitle("Brick Breaker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleGameControls() {
        if (gameController.getLevel() > 1 && gameController.getLevel() < 18) {
            loadButton.setVisible(false);
            newGameButton.setVisible(false);
            initializeAndStartGameEngine();
        }

        loadButton.setOnAction(event -> {
            fileController.loadSavedGameState(this, gameController, ball);
            try {
                loadFromSave = true;
                start(primaryStage);
                // Reinitialize the paddle and add it to the root pane
                paddle = new Rectangle();
                gameController.initPaddle(paddle);
                ball = gameController.initBall();
                root.getChildren().addAll(paddle, ball);
            } catch (Exception e) {
                logger.error("An error occurred in loadSavedGameState() Method: " + e.getMessage(), e);
            }

            loadButton.setVisible(false);
            newGameButton.setVisible(false);
        });

        newGameButton.setOnAction(event -> {
            initializeAndStartGameEngine();

            loadButton.setVisible(false);
            newGameButton.setVisible(false);
        });
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

        if (gameController.getLevel() == 18) {
            new Score().showWin(this);
        }
    }

    // Method to restart the game from the beginning
    public void resetGameToStart() {
        try {
            // Reset game variables for a fresh start
            gameController.setLevel(0);
            gameController.setHeart(1000);
            gameController.setScore(0);
            ball.setVX(1.000);
            destroyedBlockCount = 0;
            ball.resetCollisionStates();
            ball.setGoDownBall(true);
            ball.setGoldStatus(false);
            gameController.setExistHeartBlock(false);
            time = 0;
            goldTime = 0;

            blocks.clear();
            bonuses.clear();

            start(primaryStage);
        } catch (Exception e) {
            logger.error("An error occurred in resetGameToStart() Method: " + e.getMessage(), e);
        }
    }

    // Method to handle game updates
    @Override
    public void updateGameFrame() {
        Platform.runLater(() -> {

            scoreLabel.setText("Score: " + gameController.getScore());
            heartLabel.setText("Heart : " + gameController.getHeart());

            paddle.setX(X_PADDLE);
            paddle.setY(Y_PADDLE);
            ball.setCenterX(ball.getXBall());
            ball.setCenterY(ball.getYBall());

            for (Bonus choco : bonuses) {
                choco.choco.setY(choco.y);
            }

            if (destroyedBlockCount == blocks.size()) {
                prepareForNextLevel();
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

    private Color[] initializeColors() {
        return new Color[]{
                Color.MAGENTA,
                Color.RED,
                Color.GOLD,
                Color.CORAL,
                Color.AQUA,
                Color.VIOLET,
                Color.GREENYELLOW,
                Color.ORANGE,
                Color.PINK,
                Color.SLATEGREY,
                Color.YELLOW,
                Color.TOMATO,
                Color.TAN,
        };
    }
}
