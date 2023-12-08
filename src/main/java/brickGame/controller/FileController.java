package brickGame.controller;

import brickGame.LoadSave;
import brickGame.Main;
import brickGame.model.Ball;
import brickGame.model.Block;
import brickGame.model.BlockSerializable;
import brickGame.model.Paddle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * The FileController class is responsible for handling file operations related to saving and loading
 * the game state and leaderboard data. It includes methods for serialization and deserialization of game data.
 */
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    private final LoadSave loadSave = new LoadSave();

    /**
     * Retrieves the logger instance for the FileController class.
     *
     * @return The Logger instance.
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * Retrieves the LoadSave instance used for managing game data serialization and deserialization.
     *
     * @return The LoadSave instance.
     */
    public LoadSave getLoadSave() {
        return loadSave;
    }

    /**
     * Loads the saved game state from a file and updates the gameController, ball, and paddle with the loaded data.
     *
     * @param gameController The game controller to be updated with the loaded data.
     * @param ball The ball object to be updated with the loaded data.
     * @param paddle The paddle object to be updated with the loaded data.
     */
    public void loadSavedGameState(GameController gameController, Ball ball, Paddle paddle) {

        getLoadSave().readGameData(); // Load game data from storage.

        // Update the game state based on loaded data
        gameController.setExistHeartBlock(getLoadSave().isExistHeartBlock());
        gameController.setExistSplitBall(getLoadSave().isExistSplitBall());
        ball.setGoldStatus(getLoadSave().isGoldStatus());
        ball.setGoDownBall(getLoadSave().isGoDownBall());
        ball.setGoRightBall((getLoadSave().isGoRightBall()));
        ball.setCollideToPaddle(getLoadSave().isCollideToPaddle());
        ball.setCollideToPaddleAndMoveToRight(getLoadSave().isCollideToPaddleAndMoveToRight());
        ball.setCollideToRightWall(getLoadSave().isCollideToRightWall());
        ball.setCollideToLeftWall(getLoadSave().isCollideToLeftWall());
        ball.setCollideToRightBlock(getLoadSave().isCollideToRightBlock());
        ball.setCollideToBottomBlock(getLoadSave().isCollideToBottomBlock());
        ball.setCollideToLeftBlock(getLoadSave().isCollideToLeftBlock());
        ball.setCollideToTopBlock(getLoadSave().isCollideToTopBlock());
        gameController.setLevel(getLoadSave().getLevel());
        gameController.setScore(getLoadSave().getScore());
        gameController.setHeart(getLoadSave().getHeart());
        gameController.setDestroyedBlockCount(getLoadSave().getDestroyedBlockCount());
        ball.setXBall(getLoadSave().getXBall());
        ball.setYBall(getLoadSave().getYBall());
        paddle.setXPaddle(getLoadSave().getXPaddle());
        paddle.setYPaddle(getLoadSave().getYPaddle());
        gameController.setTime(getLoadSave().getTime());
        gameController.setGoldTime(getLoadSave().getGoldTime());
        ball.setVX(getLoadSave().getVX());

        // Clear existing game objects and reload from saved data
        gameController.getBlocks().clear();
        gameController.getBonuses().clear();

        for (BlockSerializable ser : getLoadSave().getBlocks()) {
            Block newBlock = new Block(ser.row, ser.column, ser.type, ser.isDestroyed);
            gameController.getBlocks().add(newBlock);
        }
    }

    /**
     * Saves the current game state to a file asynchronously. This includes game progression data and the states of game objects.
     *
     * @param main The main class instance of the application.
     * @param gameController The game controller containing the current game state.
     * @param ball The ball object with its current state.
     * @param paddle The paddle object with its current state.
     */
    public void saveCurrentGameState(Main main, GameController gameController, Ball ball, Paddle paddle) {
        new Thread(() -> {
            new File(getLoadSave().getSavePathDir()); // Consider checking if directory creation is required here.
            File file = new File(getLoadSave().getGameDataPath());
            ObjectOutputStream outputStream = null;
            try {
                outputStream = new ObjectOutputStream(new FileOutputStream(file));

                // Write game state to file
                outputStream.writeInt(gameController.getLevel());
                outputStream.writeInt(gameController.getScore());
                outputStream.writeInt(gameController.getHeart());
                outputStream.writeInt(gameController.getDestroyedBlockCount());

                outputStream.writeDouble(ball.getXBall());
                outputStream.writeDouble(ball.getYBall());
                outputStream.writeDouble(paddle.getXPaddle());
                outputStream.writeDouble(paddle.getYPaddle());
                outputStream.writeLong(gameController.getTime());
                outputStream.writeLong(gameController.getGoldTime());
                outputStream.writeDouble(ball.getVX());

                outputStream.writeBoolean(gameController.isExistHeartBlock());
                outputStream.writeBoolean(gameController.isExistSplitBall());
                outputStream.writeBoolean(ball.isGoldStatus());
                outputStream.writeBoolean(ball.isGoDownBall());
                outputStream.writeBoolean(ball.isGoRightBall());
                outputStream.writeBoolean(ball.isCollideToPaddle());
                outputStream.writeBoolean(ball.isCollideToPaddleAndMoveToRight());
                outputStream.writeBoolean(ball.isCollideToRightWall());
                outputStream.writeBoolean(ball.isCollideToLeftWall());
                outputStream.writeBoolean(ball.isCollideToRightBlock());
                outputStream.writeBoolean(ball.isCollideToBottomBlock());
                outputStream.writeBoolean(ball.isCollideToLeftBlock());
                outputStream.writeBoolean(ball.isCollideToTopBlock());

                // Save blocks
                outputStream.writeInt(gameController.getBlocks().size()); // Write the size of the block list
                for (Block block : gameController.getBlocks()) {
                    outputStream.writeInt(block.row);
                    outputStream.writeInt(block.column);
                    outputStream.writeInt(block.type);
                    outputStream.writeBoolean(block.isDestroyed);
                }

                new GameUIController().showMessage("Game Saved", main);

            } catch (IOException e) {
                getLogger().error("An error occurred in saveCurrentGameState() Method: " + e.getMessage(), e);
            } finally {
                try {
                    // Close the stream safely
                    if (outputStream != null) {
                        outputStream.flush(); // Check for null before calling flush
                        outputStream.close();
                    }
                } catch (IOException e) {
                    getLogger().error("An error occurred in saveCurrentGameState() Method: " + e.getMessage(), e);
                }
            }
        }).start(); // Start the thread to save the game state.

    }

    /**
     * Saves the leaderboard data to a file, updating the top three scores with a new score if applicable.
     *
     * @param newScore The new score to be considered for the leaderboard.
     */
    public void saveLeaderboard(int newScore) {
        // Ensure the directory exists
        File directory = new File(getLoadSave().getSavePathDir());
        if (!directory.exists()) {
            boolean isDirCreated = directory.mkdirs();
            if (!isDirCreated) {
                getLogger().error("Failed to create directory: " + directory.getPath());
                return; // Exit the method if the directory cannot be created
            }
        }

        File file = new File(getLoadSave().getLeaderboardDataPath());
        ObjectOutputStream outputStream = null;

        try {
            getLoadSave().readLeaderboard();
            // Load current high scores
            int highest = getLoadSave().getBestScore();
            int secondHigh = getLoadSave().getSecondBestScore();
            int thirdHigh = getLoadSave().getThirdBestScore();

            // Compare and update scores
            if (newScore > highest) {
                thirdHigh = secondHigh;
                secondHigh = highest;
                highest = newScore;
            } else if (newScore > secondHigh) {
                thirdHigh = secondHigh;
                secondHigh = newScore;
            } else if (newScore > thirdHigh) {
                thirdHigh = newScore;
            }

            // Write the updated scores
            outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeInt(highest);
            outputStream.writeInt(secondHigh);
            outputStream.writeInt(thirdHigh);
        } catch (IOException e) {
            getLogger().error("An error occurred in saveLeaderboard() Method: " + e.getMessage(), e);
        } finally {
            // Close the stream safely
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    getLogger().error("An error occurred while closing stream in saveLeaderboard(): " + e.getMessage(), e);
                }
            }
        }
    }


}
