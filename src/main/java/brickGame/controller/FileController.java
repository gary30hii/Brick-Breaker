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

public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    private final LoadSave loadSave = new LoadSave();
    // Load a saved game state
    public void loadSavedGameState(GameController gameController, Ball ball, Paddle paddle) {

        loadSave.readGameData();
        // Load game state from the saved data
        gameController.setExistHeartBlock(loadSave.isExistHeartBlock);
        ball.setGoldStatus(loadSave.isGoldStatus);
        ball.setGoDownBall(loadSave.goDownBall);
        ball.setGoRightBall((loadSave.goRightBall));
        ball.setCollideToPaddle(loadSave.collideToPaddle);
        ball.setCollideToPaddleAndMoveToRight(loadSave.collideToPaddleAndMoveToRight);
        ball.setCollideToRightWall(loadSave.collideToRightWall);
        ball.setCollideToLeftWall(loadSave.collideToLeftWall);
        ball.setCollideToRightBlock(loadSave.collideToRightBlock);
        ball.setCollideToBottomBlock(loadSave.collideToBottomBlock);
        ball.setCollideToLeftBlock(loadSave.collideToLeftBlock);
        ball.setCollideToTopBlock(loadSave.collideToTopBlock);
        gameController.setLevel(loadSave.level);
        gameController.setScore(loadSave.score);
        gameController.setHeart(loadSave.heart);
        gameController.setDestroyedBlockCount(loadSave.destroyedBlockCount);
        ball.setXBall(loadSave.xBall);
        ball.setYBall(loadSave.yBall);
        paddle.setXPaddle(loadSave.xPaddle);
        paddle.setYPaddle(loadSave.yPaddle);
        gameController.setTime(loadSave.time);
        gameController.setGoldTime(loadSave.goldTime);
        ball.setVX(loadSave.vX);

        gameController.getBlocks().clear();
        gameController.getBonuses().clear();

        for (BlockSerializable ser : loadSave.blocks) {
            Block newBlock = new Block(ser.row, ser.column, ser.type, ser.isDestroyed);
            gameController.getBlocks().add(newBlock);
        }
    }

    // Save the game state to a file
    public void saveCurrentGameState(Main main, GameController gameController, Ball ball, Paddle paddle) {
        new Thread(() -> {
            new File(loadSave.getSavePathDir());
            File file = new File(loadSave.getGameDataPath());
            ObjectOutputStream outputStream = null;
            try {
                outputStream = new ObjectOutputStream(new FileOutputStream(file));

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
                logger.error("An error occurred in saveCurrentGameState() Method: " + e.getMessage(), e);
            } finally {
                try {
                    if (outputStream != null) {
                        outputStream.flush(); // Check for null before calling flush
                        outputStream.close();
                    }
                } catch (IOException e) {
                    logger.error("An error occurred in saveCurrentGameState() Method: " + e.getMessage(), e);
                }
            }
        }).start();

    }

    public void saveLeaderboard(int newScore) {
        // Ensure the directory exists
        File directory = new File(loadSave.getSavePathDir());
        if (!directory.exists()) {
            boolean isDirCreated = directory.mkdirs();
            if (!isDirCreated) {
                logger.error("Failed to create directory: " + directory.getPath());
                return; // Exit the method if the directory cannot be created
            }
        }

        File file = new File(loadSave.getLeaderboardDataPath());
        ObjectOutputStream outputStream = null;

        try {
            loadSave.readLeaderboard();
            // Load current high scores
            int highest = loadSave.bestScore;
            int secondHigh = loadSave.secondBestScore;
            int thirdHigh = loadSave.thirdBestScore;

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
            logger.error("An error occurred in saveLeaderboard() Method: " + e.getMessage(), e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    logger.error("An error occurred while closing stream in saveLeaderboard(): " + e.getMessage(), e);
                }
            }
        }
    }

}
