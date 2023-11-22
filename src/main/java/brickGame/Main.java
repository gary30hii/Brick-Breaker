package brickGame;

import brickGame.controller.GameController;
import brickGame.controller.Score;
import brickGame.engine.GameEngine;
import brickGame.model.Block;
import brickGame.model.BlockSerializable;
import brickGame.model.Bonus;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
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

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    // Constants
    public static final int LEFT = 1, RIGHT = 2, SCENE_WIDTH = 500, SCENE_HEIGHT = 700;
    private static final int PADDLE_WIDTH = 130, PADDLE_HEIGHT = 30, BALL_RADIUS = 10;
    public static final String SAVE_PATH = "data/save.mdds", SAVE_PATH_DIR = "data/";

    // Game variables
    private double xPaddle = (double) Main.SCENE_WIDTH / 2 - (double) PADDLE_WIDTH / 2, yPaddle = 640.0, centerPaddleX, vX = 1.0, vY = 3.0;
    private long time, goldTime;
    private int destroyedBlockCount;
    private boolean loadFromSave, goDownBall = true, goRightBall = true, isGoldStatus;
    private boolean collideToPaddle, collideToPaddleAndMoveToRight, collideToRightWall, collideToLeftWall;
    private boolean collideToRightBlock, collideToBottomBlock, collideToLeftBlock, collideToTopBlock;

    private Circle ball;
    private Rectangle paddle;
    public Pane root;
    private Label scoreLabel, heartLabel, levelLabel;
    private Stage primaryStage;
    private Button loadButton, newGameButton;

    private GameEngine engine;
    private GameController gameController;
    private ArrayList<Block> blocks = new ArrayList<>();
    private ArrayList<Bonus> bonuses = new ArrayList<>();
    private final Color[] COLORS = initializeColors();
    // Initialize the game and UI elements
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initializeGameStage();
    }

    private void initializeGameStage() {
        root = new Pane();
        paddle = new Rectangle();
        ball = new Circle();
        if (!loadFromSave) {
            gameController = new GameController(0, 0, 100, root);
            gameController.setLevel(gameController.getLevel() + 1);
            if (gameController.getLevel() > 1) {
                new Score().showMessage("Level Up :)", this);
            }
            if (gameController.getLevel() == 18) {
                new Score().showWin(this);
                return;
            }
            gameController.initBall(ball);
            gameController.initPaddle(paddle);
            gameController.initBoard(blocks);

            loadButton = new Button("Load Game");
            newGameButton = new Button("Start New Game");
            loadButton.setTranslateX(220);
            loadButton.setTranslateY(300);
            newGameButton.setTranslateX(220);
            newGameButton.setTranslateY(340);
        }

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

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.getStylesheets().add("style.css");
        scene.setOnKeyPressed(this);

        primaryStage.setTitle("Brick Breaker");
        primaryStage.setScene(scene);
        primaryStage.show();

        if (!loadFromSave) {
            if (gameController.getLevel() > 1 && gameController.getLevel() < 18) {
                loadButton.setVisible(false);
                newGameButton.setVisible(false);
                engine = new GameEngine();
                engine.setOnAction(this);
                engine.setFps(120);
                engine.start();
            }

            loadButton.setOnAction(event -> {
                loadSavedGameState();

                loadButton.setVisible(false);
                newGameButton.setVisible(false);
            });

            newGameButton.setOnAction(event -> {
                engine = new GameEngine();
                engine.setOnAction(Main.this);
                engine.setFps(120);
                engine.start();

                loadButton.setVisible(false);
                newGameButton.setVisible(false);
            });
        } else {
            engine = new GameEngine();
            engine.setOnAction(this);
            engine.setFps(120);
            engine.start();
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
                saveCurrentGameState();
                break;
        }
    }

    // Move the paddle left or right
    private void movePaddle(int direction) {
        new Thread(() -> {
            int sleepTime = 1;
            for (int i = 0; i < 30; i++) {
                if (xPaddle == (SCENE_WIDTH - PADDLE_WIDTH) && direction == RIGHT) {
                    return; //paddle stop moving to the right when it touches the right wall
                }
                if (xPaddle == 0 && direction == LEFT) {
                    return; //paddle stop moving to the left when it touch the left wall
                }
                if (direction == RIGHT) {
                    xPaddle++;
                } else {
                    xPaddle--;
                }
                centerPaddleX = xPaddle + (double) PADDLE_WIDTH / 2;
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

    // Reset collision flags
    private void resetCollisionStates() {

        collideToPaddle = false;
        collideToPaddleAndMoveToRight = false;
        collideToRightWall = false;
        collideToLeftWall = false;

        collideToRightBlock = false;
        collideToBottomBlock = false;
        collideToLeftBlock = false;
        collideToTopBlock = false;
    }

    // Handle physics for the game ball
    private void updateBallMovement() {

        if (goDownBall) {
            gameController.setYBall(gameController.getYBall() + vY);
        } else {
            gameController.setYBall(gameController.getYBall() - vY);
        }

        if (goRightBall) {
            gameController.setXBall(gameController.getXBall() + vX);
        } else {
            gameController.setXBall(gameController.getXBall() - vX);
        }

        if (gameController.getYBall() <= 0) {
            vX = 1.000;
            resetCollisionStates();
            goDownBall = true;
            return;
        }
        if (gameController.getYBall() >= SCENE_HEIGHT) {
            resetCollisionStates();
            goDownBall = false;
            if (!isGoldStatus) {
                //TODO game-over
                gameController.setHeart(gameController.getHeart() - 1);
                new Score().show((double) SCENE_WIDTH / 2, (double) SCENE_HEIGHT / 2, -1, this);

                if (gameController.getHeart() == 0) {
                    new Score().showGameOver(this);
                    engine.stop();
                }
            }
        }

        if (gameController.getYBall() >= yPaddle - BALL_RADIUS) {
            if (gameController.getXBall() >= xPaddle && gameController.getXBall() <= xPaddle + PADDLE_WIDTH) {
                resetCollisionStates();
                collideToPaddle = true;
                goDownBall = false;

                double relation = (gameController.getXBall() - centerPaddleX) / ((double) PADDLE_WIDTH / 2);

                if (Math.abs(relation) <= 0.3) {
                    vX = Math.abs(relation);
                } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                    vX = (Math.abs(relation) * 1.5) + (gameController.getLevel() / 3.500);
                } else {
                    vX = (Math.abs(relation) * 2) + (gameController.getLevel() / 3.500);
                }

                collideToPaddleAndMoveToRight = gameController.getXBall() - centerPaddleX > 0;
            }
        }

        if (gameController.getXBall() >= SCENE_WIDTH) {
            resetCollisionStates();
            collideToRightWall = true;
        }

        if (gameController.getXBall() <= 0) {
            resetCollisionStates();
            collideToLeftWall = true;
        }

        if (collideToPaddle) {
            goRightBall = collideToPaddleAndMoveToRight;
        }

        //Wall Collide
        if (collideToRightWall) {
            goRightBall = false;
        }
        if (collideToLeftWall) {
            goRightBall = true;
        }

        //Block Collide
        if (collideToRightBlock) {
            resetCollisionStates();
            goRightBall = true;
        }

        if (collideToLeftBlock) {
            resetCollisionStates();
            goRightBall = false;
        }

        if (collideToTopBlock) {
            resetCollisionStates();
            goDownBall = false;
        }

        if (collideToBottomBlock) {
            resetCollisionStates();
            goDownBall = true;
        }
    }

    // Save the game state to a file
    private void saveCurrentGameState() {
        new Thread(() -> {
            new File(SAVE_PATH_DIR).mkdirs();
            File file = new File(SAVE_PATH);
            ObjectOutputStream outputStream = null;
            try {
                outputStream = new ObjectOutputStream(new FileOutputStream(file));

                outputStream.writeInt(gameController.getLevel());
                outputStream.writeInt(gameController.getScore());
                outputStream.writeInt(gameController.getHeart());
                outputStream.writeInt(destroyedBlockCount);

                outputStream.writeDouble(gameController.getXBall());
                outputStream.writeDouble(gameController.getYBall());
                outputStream.writeDouble(xPaddle);
                outputStream.writeDouble(yPaddle);
                outputStream.writeDouble(centerPaddleX);
                outputStream.writeLong(time);
                outputStream.writeLong(goldTime);
                outputStream.writeDouble(vX);

                outputStream.writeBoolean(gameController.isExistHeartBlock());
                outputStream.writeBoolean(isGoldStatus);
                outputStream.writeBoolean(goDownBall);
                outputStream.writeBoolean(goRightBall);
                outputStream.writeBoolean(collideToPaddle);
                outputStream.writeBoolean(collideToPaddleAndMoveToRight);
                outputStream.writeBoolean(collideToRightWall);
                outputStream.writeBoolean(collideToLeftWall);
                outputStream.writeBoolean(collideToRightBlock);
                outputStream.writeBoolean(collideToBottomBlock);
                outputStream.writeBoolean(collideToLeftBlock);
                outputStream.writeBoolean(collideToTopBlock);

                // Save blocks
                outputStream.writeInt(blocks.size()); // Write the size of the block list
                for (Block block : blocks) {
                    outputStream.writeInt(block.row);
                    outputStream.writeInt(block.column);
                    outputStream.writeInt(block.type);
                    outputStream.writeBoolean(block.isDestroyed);
                }

                new Score().showMessage("Game Saved", Main.this);

            } catch (IOException e) {
                logger.error("An error occurred in saveGame() Method: " + e.getMessage(), e);
            } finally {
                try {
                    if (outputStream != null) {
                        outputStream.flush(); // Check for null before calling flush
                        outputStream.close();
                    }
                } catch (IOException e) {
                    logger.error("An error occurred in saveGame() Method: " + e.getMessage(), e);
                }
            }
        }).start();

    }

    // Load a saved game state
    private void loadSavedGameState() {

        LoadSave loadSave = new LoadSave();
        loadSave.read();

        // Load game state from the saved data
        gameController.setExistHeartBlock(loadSave.isExistHeartBlock);
        isGoldStatus = loadSave.isGoldStatus;
        goDownBall = loadSave.goDownBall;
        goRightBall = loadSave.goRightBall;
        collideToPaddle = loadSave.collideToBreak;
        collideToPaddleAndMoveToRight = loadSave.collideToBreakAndMoveToRight;
        collideToRightWall = loadSave.collideToRightWall;
        collideToLeftWall = loadSave.collideToLeftWall;
        collideToRightBlock = loadSave.collideToRightBlock;
        collideToBottomBlock = loadSave.collideToBottomBlock;
        collideToLeftBlock = loadSave.collideToLeftBlock;
        collideToTopBlock = loadSave.collideToTopBlock;
        gameController.setLevel(loadSave.level);
        gameController.setScore(loadSave.score);
        gameController.setHeart(loadSave.heart);
        destroyedBlockCount = loadSave.destroyedBlockCount;
        gameController.setXBall(loadSave.xBall);
        gameController.setYBall(loadSave.yBall);
        xPaddle = loadSave.xBreak;
        yPaddle = loadSave.yBreak;
        centerPaddleX = loadSave.centerBreakX;
        time = loadSave.time;
        goldTime = loadSave.goldTime;
        vX = loadSave.vX;

        blocks.clear();
        bonuses.clear();

        for (BlockSerializable ser : loadSave.blocks) {
            int r = new Random().nextInt(200); // Example random color selection
            Block newBlock = new Block(ser.row, ser.column, COLORS[r % COLORS.length], ser.type, ser.isDestroyed);
            blocks.add(newBlock);
            System.out.println(ser.row + " " + ser.column + " " + ser.type + " " + ser.isDestroyed);
        }

        try {
            loadFromSave = true;
            start(primaryStage);
            // Reinitialize the paddle and add it to the root pane
            paddle = new Rectangle();
            gameController.initPaddle(paddle);
            ball = new Circle();
            gameController.initBall(ball);
            root.getChildren().addAll(paddle, ball);
        } catch (Exception e) {
            logger.error("An error occurred in loadSavedGameState() Method: " + e.getMessage(), e);
        }

    }

    // Method to prepare for the next game level
    private void prepareForNextLevel() {
        Platform.runLater(() -> {
            try {
                // Reset variables for the next level
                vX = 1.000;
                engine.stop();
                resetCollisionStates();
                goDownBall = true;
                isGoldStatus = false;
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
        gameController.setLevel(gameController.getLevel() + 1);
        if (gameController.getLevel() > 1) {
            new Score().showMessage("Level Up :)", this);
        }
        if (gameController.getLevel() == 18) {
            new Score().showWin(this);
            return;
        }

        gameController.initBall(ball);
        gameController.initPaddle(paddle);
        gameController.initBoard(blocks);

        loadButton = new Button("Load Game");
        newGameButton = new Button("Start New Game");
        loadButton.setTranslateX(220);
        loadButton.setTranslateY(300);
        newGameButton.setTranslateX(220);
        newGameButton.setTranslateY(340);

        root = new Pane();
        scoreLabel = new Label("Score: " + gameController.getScore());
        levelLabel = new Label("Level: " + gameController.getLevel());
        levelLabel.setTranslateY(20);
        heartLabel = new Label("Heart : " + gameController.getHeart());
        heartLabel.setTranslateX(SCENE_WIDTH - 70);
        if (!loadFromSave) {
            root.getChildren().addAll(paddle, ball, scoreLabel, heartLabel, levelLabel, newGameButton, loadButton);
        } else {
            root.getChildren().addAll(paddle, ball, scoreLabel, heartLabel, levelLabel);
        }
        for (Block block : blocks) {
            root.getChildren().add(block.rect);
        }
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.getStylesheets().add("style.css");
        scene.setOnKeyPressed(this);

        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        if (!loadFromSave) {
            if (gameController.getLevel() > 1 && gameController.getLevel() < 18) {
                loadButton.setVisible(false);
                newGameButton.setVisible(false);
                engine = new GameEngine();
                engine.setOnAction(this);
                engine.setFps(120);
                engine.start();
            }

            loadButton.setOnAction(event -> {
                loadSavedGameState();
                loadButton.setVisible(false);
                newGameButton.setVisible(false);
            });

            newGameButton.setOnAction(event -> {
                engine = new GameEngine();
                engine.setOnAction(Main.this);
                engine.setFps(120);
                engine.start();
                loadButton.setVisible(false);
                newGameButton.setVisible(false);
            });
        } else {
            engine = new GameEngine();
            engine.setOnAction(this);
            engine.setFps(120);
            engine.start();
            loadFromSave = false;
        }
    }

    // Method to restart the game from the beginning
    public void resetGameToStart() {
        try {
            // Reset game variables for a fresh start
            gameController.setLevel(0);
            gameController.setHeart(1000);
            gameController.setScore(0);
            vX = 1.000;
            destroyedBlockCount = 0;
            resetCollisionStates();
            goDownBall = true;

            isGoldStatus = false;
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

            paddle.setX(xPaddle);
            paddle.setY(yPaddle);
            ball.setCenterX(gameController.getXBall());
            ball.setCenterY(gameController.getYBall());

            for (Bonus choco : bonuses) {
                choco.choco.setY(choco.y);
            }

            if (destroyedBlockCount == blocks.size()) {
                prepareForNextLevel();
            }
        });


        if (gameController.getYBall() >= Block.getPaddingTop() && gameController.getYBall() <= (Block.getHeight() * (gameController.getLevel() + 1)) + Block.getPaddingTop()) {
            for (final Block block : blocks) {
                int hitCode = block.checkHitToBlock(gameController.getXBall(), gameController.getYBall(), BALL_RADIUS);
                if (hitCode != Block.NO_HIT) {
                    gameController.setScore(gameController.getScore() + 1);

                    new Score().show(block.x, block.y, 1, this);

                    block.rect.setVisible(false);
                    block.isDestroyed = true;
                    destroyedBlockCount++;
                    resetCollisionStates();

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
                        isGoldStatus = true;
                    }

                    if (block.type == Block.BLOCK_HEART) {
                        gameController.setHeart(gameController.getHeart() + 1);
                    }

                    if (hitCode == Block.HIT_RIGHT) {
                        collideToRightBlock = true;
                    } else if (hitCode == Block.HIT_BOTTOM) {
                        collideToBottomBlock = true;
                    } else if (hitCode == Block.HIT_LEFT) {
                        collideToLeftBlock = true;
                    } else if (hitCode == Block.HIT_TOP) {
                        collideToTopBlock = true;
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
        updateBallMovement();

        if (time - goldTime > 5000) {
            ball.setFill(new ImagePattern(new Image("ball.png")));
            root.getStyleClass().remove("goldRoot");
            isGoldStatus = false;
        }

        for (Bonus choco : bonuses) {
            if (choco.y > SCENE_HEIGHT || choco.taken) {
                continue;
            }
            if (choco.y >= yPaddle && choco.y <= yPaddle + PADDLE_HEIGHT && choco.x >= xPaddle && choco.x <= xPaddle + PADDLE_WIDTH) {
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
