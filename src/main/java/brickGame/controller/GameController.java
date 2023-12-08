package brickGame.controller;

import brickGame.Main;
import brickGame.engine.GameEngine;
import brickGame.model.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.ImagePattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Random;

/**
 * Main controller for the game, handling game logic and events.
 * Implements EventHandler for keyboard inputs and GameEngine.OnAction for game actions.
 */
public class GameController implements EventHandler<KeyEvent>, GameEngine.OnAction {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    // Constants for direction
    private static final int LEFT = 1;
    private static final int RIGHT = 2;

    // Main class reference, game state variables, and background music controller
    private final Main main;
    private int level;
    private int score;
    private int heart;
    private final BackgroundMusicController bgMusic = new BackgroundMusicController("src/main/resources/game-bgm.mp3");

    // Game elements and collections for blocks and bonuses
    private GameEngine engine;
    private Paddle paddle;
    private Ball ball;
    private SplitBall splitBall1;
    private SplitBall splitBall2;
    private final ArrayList<Block> blocks = new ArrayList<>();
    private final ArrayList<Bonus> bonuses = new ArrayList<>();

    // Game state flags and counters
    private boolean isExistHeartBlock = false;
    private boolean isExistSplitBall = false;
    private int destroyedBlockCount;
    private long time;
    private long goldTime;

    /**
     * Constructor to initialize the game controller with the main class and initial game state.
     *
     * @param main  The main class instance.
     * @param level Initial level of the game.
     * @param score Initial score of the game.
     * @param heart Initial heart count of the game.
     */
    public GameController(Main main, int level, int score, int heart) {
        this.main = main;
        this.setLevel(level);
        this.setScore(score);
        this.setHeart(heart);
    }

    /**
     * Retrieves the Logger instance for the GameController class.
     *
     * @return The Logger instance used in GameController.
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * Retrieves the constant value representing the left direction.
     *
     * @return The integer value representing left direction.
     */
    public static int getLEFT() {
        return LEFT;
    }

    /**
     * Retrieves the constant value representing the right direction.
     *
     * @return The integer value representing right direction.
     */
    public static int getRIGHT() {
        return RIGHT;
    }

    /**
     * Retrieves the current level of the game.
     *
     * @return The current game level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Retrieves the current score of the game.
     *
     * @return The current game score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Retrieves the current heart count in the game.
     *
     * @return The current number of hearts.
     */
    public int getHeart() {
        return heart;
    }

    /**
     * Retrieves the Paddle object currently used in the game.
     *
     * @return The Paddle object.
     */
    public Paddle getPaddle() {
        return paddle;
    }

    /**
     * Retrieves the Ball object currently in play.
     *
     * @return The Ball object.
     */
    public Ball getBall() {
        return ball;
    }

    /**
     * Retrieves the list of Block objects currently present in the game.
     *
     * @return The list of Block objects.
     */
    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    /**
     * Retrieves the list of Bonus objects currently present in the game.
     *
     * @return The list of Bonus objects.
     */
    public ArrayList<Bonus> getBonuses() {
        return bonuses;
    }

    /**
     * Checks whether a heart block exists in the game.
     *
     * @return True if a heart block exists, false otherwise.
     */
    public boolean isExistHeartBlock() {
        return isExistHeartBlock;
    }

    /**
     * Checks whether a split ball exists in the game.
     *
     * @return True if a split ball exists, false otherwise.
     */
    public boolean isExistSplitBall() {
        return isExistSplitBall;
    }

    /**
     * Retrieves the count of blocks destroyed in the current game session.
     *
     * @return The number of destroyed blocks.
     */
    public int getDestroyedBlockCount() {
        return destroyedBlockCount;
    }

    /**
     * Retrieves the current game time.
     *
     * @return The current game time in milliseconds.
     */
    public long getTime() {
        return time;
    }

    /**
     * Retrieves the time at which the last gold ball was activated.
     *
     * @return The time of the last gold ball activation.
     */
    public long getGoldTime() {
        return goldTime;
    }

    /**
     * Retrieves the Main class instance associated with the game controller.
     *
     * @return The Main class instance.
     */
    public Main getMain() {
        return main;
    }

    /**
     * Retrieves the BackgroundMusicController instance used in the game.
     *
     * @return The BackgroundMusicController instance.
     */
    public BackgroundMusicController getBgMusic() {
        return bgMusic;
    }

    /**
     * Retrieves the GameEngine object managing the game's mechanics.
     *
     * @return The GameEngine object.
     */
    public GameEngine getEngine() {
        return engine;
    }

    /**
     * Retrieves the first SplitBall object if it exists in the game.
     *
     * @return The first SplitBall object, or null if it doesn't exist.
     */
    public SplitBall getSplitBall1() {
        return splitBall1;
    }

    /**
     * Retrieves the second SplitBall object if it exists in the game.
     *
     * @return The second SplitBall object, or null if it doesn't exist.
     */
    public SplitBall getSplitBall2() {
        return splitBall2;
    }

    /**
     * Sets the current game level.
     *
     * @param level The new level to be set for the game.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Sets the current score of the game.
     *
     * @param score The new score to be set for the game.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Sets the current heart count for the game.
     *
     * @param heart The new heart count to be set for the game.
     */
    public void setHeart(int heart) {
        this.heart = heart;
    }

    /**
     * Sets the existence of the heart block in the game.
     *
     * @param existHeartBlock The boolean value to set for the existence of the heart block.
     */
    public void setExistHeartBlock(boolean existHeartBlock) {
        isExistHeartBlock = existHeartBlock;
    }

    /**
     * Sets the existence of the split ball in the game.
     *
     * @param existSplitBall The boolean value to set for the existence of the split ball.
     */
    public void setExistSplitBall(boolean existSplitBall) {
        isExistSplitBall = existSplitBall;
    }

    /**
     * Sets the count of destroyed blocks in the game.
     *
     * @param destroyedBlockCount The new count of destroyed blocks to be set.
     */
    public void setDestroyedBlockCount(int destroyedBlockCount) {
        this.destroyedBlockCount = destroyedBlockCount;
    }

    /**
     * Sets the current game time.
     *
     * @param time The new game time to be set in milliseconds.
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Sets the time of the last gold ball activation.
     *
     * @param goldTime The time to set for the last gold ball activation.
     */
    public void setGoldTime(long goldTime) {
        this.goldTime = goldTime;
    }

    /**
     * Sets the GameEngine object for the game.
     *
     * @param engine The new GameEngine object to be set for the game.
     */
    public void setEngine(GameEngine engine) {
        this.engine = engine;
    }

    /**
     * Sets the Paddle object for the game.
     *
     * @param paddle The new Paddle object to be set for the game.
     */
    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    /**
     * Sets the Ball object for the game.
     *
     * @param ball The new Ball object to be set for the game.
     */
    public void setBall(Ball ball) {
        this.ball = ball;
    }

    /**
     * Sets the first SplitBall object for the game.
     *
     * @param splitBall1 The new first SplitBall object to be set for the game.
     */
    public void setSplitBall1(SplitBall splitBall1) {
        this.splitBall1 = splitBall1;
    }

    /**
     * Sets the second SplitBall object for the game.
     *
     * @param splitBall2 The new second SplitBall object to be set for the game.
     */
    public void setSplitBall2(SplitBall splitBall2) {
        this.splitBall2 = splitBall2;
    }


    /**
     * Retrieves the final level of the game.
     *
     * @return The final level number.
     */
    public int getFinalLevel() {
        return 30;
    }

    /**
     * Initializes the game board with blocks based on the level and random generation.
     *
     * @param blocks The list of blocks to be initialized.
     */
    private void initBoard(ArrayList<Block> blocks) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < getLevel() / 2 + 1; j++) {
                int r = new Random().nextInt(500);
                if (r % 5 == 0) {
                    continue; // Empty block
                }
                int type = determineBlockType(r);
                blocks.add(new Block(j, i, type, false));
            }
        }
    }

    /**
     * Determines the type of block to be created based on a random value.
     *
     * @param randomValue The random value to determine the block type.
     * @return The type of block to be created.
     */
    private int determineBlockType(int randomValue) {
        // Logic to determine the block type
        if (randomValue % 15 == 1) {
            return Block.BLOCK_THREE;
        } else if (randomValue % 15 == 2) {
            if (!isExistHeartBlock()) {
                setExistHeartBlock(true);
                return Block.BLOCK_HEART;
            } else {
                return Block.BLOCK_NORMAL;
            }
        } else if (randomValue % 15 == 3) {
            return Block.BLOCK_STAR;
        } else if (randomValue % 15 == 7 && getLevel() > 10) {
            return Block.BLOCK_FOUL;
        } else if (randomValue % 15 == 11 && getLevel() > 15) {
            return Block.BLOCK_LOCK;
        } else if (randomValue % 15 == 13 && getLevel() > 25) {
            if (!isExistSplitBall()) {
                setExistSplitBall(true);
                return Block.BLOCK_BALL;
            } else {
                return Block.BLOCK_LOCK;
            }
        } else {
            if (getLevel() > 20) {
                return Block.BLOCK_LOCK;
            } else {
                return Block.BLOCK_NORMAL;
            }
        }
    }

    /**
     * Initializes the paddle for the game.
     *
     * @return The initialized Paddle object.
     */
    private Paddle initPaddle() {
        this.setPaddle(new Paddle());
        return getPaddle();
    }

    /**
     * Initializes the ball for the game, setting its initial position and movement.
     *
     * @return The initialized Ball object.
     */
    private Ball initBall() {
        Random random = new Random();
        int xBall = random.nextInt(Main.SCENE_WIDTH) + 1;
        int minY = Block.getPaddingTop() + (getLevel() / 2 + 1) * Block.getHeight() + Ball.BALL_RADIUS;
        int maxY = Main.SCENE_HEIGHT - Ball.BALL_RADIUS;
        int yBall = Math.max(minY, Math.min(random.nextInt(Main.SCENE_HEIGHT - 200) + minY, maxY));
        setBall(new Ball(xBall, yBall));
        return getBall();
    }

    /**
     * Handles key events for game controls such as paddle movement and game actions.
     *
     * @param event The KeyEvent to be handled.
     */
    @Override
    public void handle(KeyEvent event) {
        if (getEngine() != null) {
            switch (event.getCode()) {
                case LEFT:
                    getPaddle().movePaddle(getLEFT());
                    break;
                case RIGHT:
                    getPaddle().movePaddle(getRIGHT());
                    break;
                case S:
                    new FileController().saveCurrentGameState(getMain(), this, getBall(), getPaddle());
                    break;
                case M:
                    if (getBgMusic().isMusicMuted()) {
                        getBgMusic().unMuteMusic();
                        new GameUIController().showMessage("Unmuted", getMain());
                    } else {
                        getBgMusic().muteMusic();
                        new GameUIController().showMessage("Muted", getMain());
                    }
                    break;
            }
        }
    }

    /**
     * Performs actions when leveling up in the game, such as updating the level and score.
     *
     * @param main The main class instance.
     */
    private void levelUp(Main main) {
        setLevel(getLevel() + 1);

        if (getLevel() > 1 && getLevel() != getFinalLevel() + 1) {
            new GameUIController().showMessage("Level Up :)", main);
        }

        if (getLevel() == getFinalLevel() + 1) {
            setScore(getScore() + getHeart() * 5);
            resetGameToStart();
            main.showWin();
        }
    }

    /**
     * Updates the game frame, handling UI updates and game logic on each frame.
     */
    @Override
    public void updateGameFrame() {
        if (getEngine() != null) {
            Platform.runLater(() -> {

                getMain().updateGameData(getScore(), getHeart());
                getPaddle().setX(getPaddle().getXPaddle());
                getPaddle().setY(getPaddle().getYPaddle());
                getBall().setCenterX(getBall().getXBall());
                getBall().setCenterY(getBall().getYBall());
                if (getSplitBall1() != null) {
                    getSplitBall1().setCenterX(getSplitBall1().getXBall());
                    getSplitBall1().setCenterY(getSplitBall1().getYBall());
                }
                if (getSplitBall2() != null) {
                    getSplitBall2().setCenterX(getSplitBall2().getXBall());
                    getSplitBall2().setCenterY(getSplitBall2().getYBall());
                }

                for (Bonus choco : getBonuses()) {
                    choco.choco.setY(choco.y);
                }

                if (getDestroyedBlockCount() == getBlocks().size() && getLevel() < getFinalLevel() + 1) {
                    prepareForNextLevel();
                }
            });

            ballCollision(getBall());
            ballDisappear();
            if (getSplitBall1() != null) {
                ballCollision(getSplitBall1());
            }
            if (getSplitBall2() != null) {
                ballCollision(getSplitBall2());
            }

        }
    }

    /**
     * Initializes the game state and elements when the game starts or when a previous saved game is loaded.
     */
    @Override
    public void onInit() {
        getBgMusic().playMusic(); // To play the music

        if (!getMain().loadFromSave) {
            levelUp(getMain());
            initializeGameElements();
        } else {
            initializeGameElements();
            new FileController().loadSavedGameState(this, getBall(), getPaddle());
            getMain().loadFromSave = false;
        }
        initializeAndStartGameEngine();
        getMain().setUpGameUI();
        getMain().getScene().setOnKeyPressed(this);
    }

    /**
     * Performs physics calculations for the game, including ball movement and interaction with other game elements.
     */
    @Override
    public void performPhysicsCalculations() {
        if (getEngine() != null) {
            getBall().updateBallMovement(getMain(), this, getPaddle());
            if (getSplitBall1() != null) {
                getSplitBall1().updateBallMovement(getMain(), this, getPaddle());
            }
            if (getSplitBall2() != null) {
                getSplitBall2().updateBallMovement(getMain(), this, getPaddle());
            }

            if (getTime() - getGoldTime() > 5000) {
                getBall().setFill(new ImagePattern(new Image("ball.png")));
                getBall().setGoldStatus(false);
            }

            for (Bonus bonus : getBonuses()) {
                if (bonus.y > Main.SCENE_HEIGHT || bonus.taken) {
                    continue;
                }
                if (bonus.y >= getPaddle().getYPaddle() && bonus.y <= getPaddle().getYPaddle() + getPaddle().getPaddleHeight() && bonus.x >= getPaddle().getXPaddle() && bonus.x <= getPaddle().getXPaddle() + getPaddle().getPaddleWidth()) {
                    bonus.taken = true;
                    bonus.choco.setVisible(false);
                    if (bonus.getType() == Block.BLOCK_THREE) {
                        setScore(getScore() + 3);
                        new GameUIController().show(bonus.x, bonus.y, 3, getMain());
                    } else if (bonus.getType() == Block.BLOCK_FOUL) {
                        setScore(getScore() - 2);
                        new GameUIController().show(bonus.x, bonus.y, -2, getMain());
                    } else if (bonus.getType() == Block.BLOCK_BALL) {
                        setSplitBall1(new SplitBall((int) bonus.x, (int) bonus.y, true));
                        setSplitBall2(new SplitBall((int) bonus.x, (int) bonus.y, false));
                        Platform.runLater(() -> getMain().getRoot().getChildren().addAll(getSplitBall1(), getSplitBall2()));
                    }
                }
                bonus.y += ((getTime() - bonus.timeCreated) / 1000.000) + 1.000;
            }
        }
    }

    /**
     * Updates the game based on the elapsed time, handling time-based game logic.
     *
     * @param time The current time in the game.
     */
    @Override
    public void onTime(long time) {
        this.setTime(time);
    }

    /**
     * Prepares for the next game level by resetting variables and reinitializing game elements.
     */
    private void prepareForNextLevel() {
        try {
            // Reset variables for the next level
            getBall().setVX(1.000);
            setSplitBall1(null);
            setSplitBall2(null);
            getEngine().stop();
            getBall().resetCollisionStates();
            getBall().setGoDownBall(true);
            setExistHeartBlock(false);
            setExistSplitBall(false);

            setTime(0);
            getBlocks().clear();
            setDestroyedBlockCount(0);

            // Call the initialization logic directly
            setupNewGameLevel();

        } catch (Exception e) {
            getLogger().error("An error occurred in prepareForNextLevel() Method: " + e.getMessage(), e);
        }
    }

    /**
     * Initializes and starts the game engine.
     */
    private void initializeAndStartGameEngine() {
        setEngine(new GameEngine(120));
        getEngine().setOnAction(this);
        getEngine().start();
    }

    /**
     * Sets up a new game level by calling common initialization logic.
     */
    private void setupNewGameLevel() {
        // Add the initialization logic here
        levelUp(getMain());
        if (getEngine() != null) {
            initializeGameElements();
            initializeAndStartGameEngine();
            getMain().getRoot().getChildren().clear();
            getMain().setUpGameUI();
            getMain().getScene().setOnKeyPressed(this);
        }
    }

    /**
     * Initializes game elements such as the ball, paddle, and blocks.
     */
    private void initializeGameElements() {
        setBall(initBall());
        setPaddle(initPaddle());
        initBoard(getBlocks());
    }

    /**
     * Resets the game to its starting state, including stopping the game engine and resetting the score.
     */
    public void resetGameToStart() {
        try {
            // Reset game variables for a fresh start
            getEngine().stop();
            setEngine(null);
            getBgMusic().stopMusic();
            getMain().setFinalScore(getScore());
            getBlocks().clear();
            getBonuses().clear();
            setBall(null);
            setPaddle(null);
            setSplitBall1(null);
            setSplitBall2(null);
            setDestroyedBlockCount(0);
            setTime(0);
            setGoldTime(0);
        } catch (Exception e) {
            getLogger().error("An error occurred in resetGameToStart() Method: " + e.getMessage(), e);
        }
    }

    /**
     * Handle split ball disappearance after collision with the bottom scene
     */
    private void ballDisappear() {
        if (getSplitBall1() != null && getSplitBall1().getYBall() > Main.SCENE_HEIGHT) {
            getMain().getRoot().getChildren().remove(getSplitBall1());
            setSplitBall1(null);
        }
        if (getSplitBall2() != null && getSplitBall2().getYBall() > Main.SCENE_HEIGHT) {
            getMain().getRoot().getChildren().remove(getSplitBall2());
            setSplitBall2(null);
        }
    }

    /**
     * Handling ball collision with blocks
     */
    private void ballCollision(Ball theBall) {
        if (theBall.getYBall() >= Block.getPaddingTop() && theBall.getYBall() <= (Block.getHeight() * (getLevel() + 1)) + Block.getPaddingTop()) {
            for (final Block block : getBlocks()) {
                int hitCode = block.checkHitToBlock(theBall.getXBall(), theBall.getYBall(), Ball.BALL_RADIUS);
                if (hitCode != Block.NO_HIT) {
                    theBall.resetCollisionStates();

                    if (block.type == Block.BLOCK_LOCK) {
                        // Change block type to normal when hit
                        int r = new Random().nextInt(4) + 1;
                        block.changeType(determineBlockType(r));
                    } else {
                        // Award points for non-lock blocks
                        setScore(getScore() + 1);
                        new GameUIController().show(block.x, block.y, 1, getMain());

                        block.rect.setVisible(false);
                        block.isDestroyed = true;
                        setDestroyedBlockCount(getDestroyedBlockCount() + 1);
                    }

                    if (block.type == Block.BLOCK_THREE) {
                        final Bonus bonus = new Bonus(block.row, block.column, block.type);
                        bonus.timeCreated = getTime();
                        Platform.runLater(() -> getMain().getRoot().getChildren().add(bonus.choco));
                        getBonuses().add(bonus);
                    }

                    if (block.type == Block.BLOCK_STAR && theBall == getBall()) {
                        setGoldTime(getTime());
                        theBall.setFill(new ImagePattern(new Image("goldball.png")));
                        theBall.setGoldStatus(true);
                    }

                    if (block.type == Block.BLOCK_HEART) {
                        setHeart(getHeart() + 1);
                        new GameUIController().showMessage("Heart +1", getMain());
                    }

                    if (block.type == Block.BLOCK_FOUL) {
                        final Bonus foul = new Bonus(block.row, block.column, block.type);
                        foul.timeCreated = getTime();
                        Platform.runLater(() -> getMain().getRoot().getChildren().add(foul.choco));
                        getBonuses().add(foul);
                    }

                    if (block.type == Block.BLOCK_BALL) {
                        final Bonus fourBall = new Bonus(block.row, block.column, block.type);
                        fourBall.timeCreated = getTime();
                        Platform.runLater(() -> getMain().getRoot().getChildren().add(fourBall.choco));
                        getBonuses().add(fourBall);
                    }

                    if (hitCode == Block.HIT_RIGHT) {
                        theBall.setCollideToRightBlock(true);
                    } else if (hitCode == Block.HIT_BOTTOM) {
                        theBall.setCollideToBottomBlock(true);
                    } else if (hitCode == Block.HIT_LEFT) {
                        theBall.setCollideToLeftBlock(true);
                    } else if (hitCode == Block.HIT_TOP) {
                        theBall.setCollideToTopBlock(true);
                    }

                }

            }
        }
    }
}
