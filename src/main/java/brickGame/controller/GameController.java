package brickGame.controller;

import brickGame.Main;
import brickGame.engine.GameEngine;
import brickGame.model.Ball;
import brickGame.model.Block;
import brickGame.model.Bonus;
import brickGame.model.Paddle;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Random;

public class GameController implements EventHandler<KeyEvent>, GameEngine.OnAction {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    private final Main main;
    private int level;
    private int score;
    private int heart;
    private GameEngine engine;
    private Paddle paddle;
    private Ball ball;
    private final ArrayList<Block> blocks = new ArrayList<>();
    private final ArrayList<Bonus> bonuses = new ArrayList<>();

    private boolean isExistHeartBlock = false;
    private int destroyedBlockCount;

    private final int finalLevel = 18;
    private long time;
    private long goldTime;

    public GameController(Main main, int level, int score, int heart) {
        this.main =main;
        this.level = level;
        this.score = score;
        this.heart = heart;
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public int getHeart() {
        return heart;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public Ball getBall() {
        return ball;
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    public ArrayList<Bonus> getBonuses() {
        return bonuses;
    }

    public boolean isExistHeartBlock() {
        return isExistHeartBlock;
    }

    public int getDestroyedBlockCount() {
        return destroyedBlockCount;
    }

    public long getTime() {
        return time;
    }

    public long getGoldTime() {
        return goldTime;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public void setExistHeartBlock(boolean existHeartBlock) {
        isExistHeartBlock = existHeartBlock;
    }

    public void setDestroyedBlockCount(int destroyedBlockCount) {
        this.destroyedBlockCount = destroyedBlockCount;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setGoldTime(long goldTime) {
        this.goldTime = goldTime;
    }

    // Initialize the game board
    private void initBoard(ArrayList<Block> blocks) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < getLevel() + 1; j++) {
                int r = new Random().nextInt(500);
                if (r % 5 == 0) {
                    continue; // Empty block
                }
                int type = determineBlockType(r);
                Color color = Color.BLUE;
                blocks.add(new Block(j, i, color, type, false));
            }
        }
    }

    private int determineBlockType(int randomValue) {
        // Logic to determine the block type
        if (randomValue % 10 == 1) {
            return Block.BLOCK_CHOCO;
        } else if (randomValue % 10 == 2) {
            if (!isExistHeartBlock) {
                isExistHeartBlock = true;
                return Block.BLOCK_HEART;
            } else {
                return Block.BLOCK_NORMAL;
            }
        } else if (randomValue % 10 == 3) {
            return Block.BLOCK_STAR;
        } else {
            return Block.BLOCK_NORMAL;
        }
    }

    private Paddle initPaddle() {
        this.paddle = new Paddle();
        return paddle;
    }

    // Initialize the game ball
    private Ball initBall() {
        Random random = new Random();
        int xBall = random.nextInt(Main.SCENE_WIDTH) + 1;
        int minY = Block.getPaddingTop() + (getLevel() + 1) * Block.getHeight() + Ball.BALL_RADIUS;
        int maxY = Main.SCENE_HEIGHT - Ball.BALL_RADIUS;
        int yBall = Math.max(minY, Math.min(random.nextInt(Main.SCENE_HEIGHT - 200) + minY, maxY));
        ball = new Ball(xBall, yBall);
        return ball;
    }

    // Handle key events for game controls
    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                paddle.movePaddle(LEFT);
                break;
            case RIGHT:
                paddle.movePaddle(RIGHT);
                break;
            case S:
                new FileController().saveCurrentGameState(main, this, ball, paddle);
                break;
        }
    }

    private void levelUp(Main main) {
        setLevel(getLevel() + 1);

        if (getLevel() > 1 && getLevel() != finalLevel) {
            new GameUIController().showMessage("Level Up :)", main);
        }

        if (getLevel() == finalLevel) {
            resetGameToStart();
            main.showWin();
        }
    }

    // Method to handle game updates
    @Override
    public void updateGameFrame() {
        Platform.runLater(() -> {

            main.updateGameData(getScore(), getHeart());
            paddle.setX(paddle.getXPaddle());
            paddle.setY(paddle.getYPaddle());
            ball.setCenterX(ball.getXBall());
            ball.setCenterY(ball.getYBall());

            for (Bonus choco : bonuses) {
                choco.choco.setY(choco.y);
            }

            if (destroyedBlockCount == blocks.size() && getLevel() < finalLevel) {
                prepareForNextLevel();
            }
        });

        if (ball.getYBall() >= Block.getPaddingTop() && ball.getYBall() <= (Block.getHeight() * (getLevel() + 1)) + Block.getPaddingTop()) {
            for (final Block block : blocks) {
                int hitCode = block.checkHitToBlock(ball.getXBall(), ball.getYBall(), Ball.BALL_RADIUS);
                if (hitCode != Block.NO_HIT) {
                    setScore(getScore() + 1);

                    new GameUIController().show(block.x, block.y, 1, main);

                    block.rect.setVisible(false);
                    block.isDestroyed = true;
                    destroyedBlockCount++;
                    ball.resetCollisionStates();

                    if (block.type == Block.BLOCK_CHOCO) {
                        final Bonus choco = new Bonus(block.row, block.column);
                        choco.timeCreated = time;
                        Platform.runLater(() -> main.root.getChildren().add(choco.choco));
                        bonuses.add(choco);
                    }

                    if (block.type == Block.BLOCK_STAR) {
                        goldTime = time;
                        ball.setFill(new ImagePattern(new Image("goldball.png")));
                        ball.setGoldStatus(true);
                    }

                    if (block.type == Block.BLOCK_HEART) {
                        setHeart(getHeart() + 1);
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

            }
        }
    }

    @Override
    public void onInit() {
        if (!main.loadFromSave) {
            levelUp(main);
            initializeGameElements();
        } else {
            initializeGameElements();
            new FileController().loadSavedGameState(this, ball, paddle);
            main.loadFromSave = false;
        }
        initializeAndStartGameEngine();
        main.setUpGameUI();
        main.scene.setOnKeyPressed(this);
    }

    @Override
    public void performPhysicsCalculations() {
        ball.updateBallMovement(main, this, paddle);

        if (time - goldTime > 5000) {
            ball.setFill(new ImagePattern(new Image("ball.png")));
            ball.setGoldStatus(false);
        }

        for (Bonus choco : bonuses) {
            if (choco.y > Main.SCENE_HEIGHT || choco.taken) {
                continue;
            }
            if (choco.y >= paddle.getYPaddle() && choco.y <= paddle.getYPaddle() + paddle.getPaddleHeight() && choco.x >= paddle.getXPaddle() && choco.x <= paddle.getXPaddle() + paddle.getPaddleWidth()) {
                choco.taken = true;
                choco.choco.setVisible(false);
                setScore(getScore() + 3);
                new GameUIController().show(choco.x, choco.y, 3, main);
            }
            choco.y += ((time - choco.timeCreated) / 1000.000) + 1.000;
        }

    }

    @Override
    public void onTime(long time) {
        this.time = time;
    }

    // Method to prepare for the next game level
    private void prepareForNextLevel() {
        try {
            // Reset variables for the next level
            ball.setVX(1.000);
            engine.stop();
            ball.resetCollisionStates();
            ball.setGoDownBall(true);
            setExistHeartBlock(false);
            time = 0;
            blocks.clear();
            destroyedBlockCount = 0;

            // Call the initialization logic directly
            setupNewGameLevel();

        } catch (Exception e) {
            logger.error("An error occurred in prepareForNextLevel() Method: " + e.getMessage(), e);
        }
    }

    private void initializeAndStartGameEngine() {
        engine = new GameEngine(120);
        engine.setOnAction(this);
        engine.start();
    }

    // Extract common initialization logic into a separate method
    private void setupNewGameLevel() {
        // Add the initialization logic here
        levelUp(main);
        if (engine != null) {
            initializeGameElements();
            initializeAndStartGameEngine();
            main.root.getChildren().clear();
            main.setUpGameUI();
            main.scene.setOnKeyPressed(this);
        }
    }

    private void initializeGameElements() {
        ball = initBall();
        paddle = initPaddle();
        initBoard(blocks);
    }

    // Method to restart the game from the beginning
    public void resetGameToStart() {
        try {
            // Reset game variables for a fresh start
            engine.stop();
            engine = null;
            main.finalScore = getScore();
            blocks.clear();
            bonuses.clear();
            ball = null;
            paddle = null;
            destroyedBlockCount = 0;
            time = 0;
            goldTime = 0;
        } catch (Exception e) {
            logger.error("An error occurred in resetGameToStart() Method: " + e.getMessage(), e);
        }
    }

}
