package brickGame.controller;

import brickGame.LoadSave;
import brickGame.Main;
import brickGame.model.Ball;
import brickGame.model.Block;
import brickGame.model.BlockSerializable;
import brickGame.model.Paddle;
import javafx.scene.paint.Color;
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
    public void loadSavedGameState(Main main, GameController gameController, Ball ball, Paddle paddle) {

        loadSave.read();
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
        main.destroyedBlockCount = loadSave.destroyedBlockCount;
        ball.setXBall(loadSave.xBall);
        ball.setYBall(loadSave.yBall);
        paddle.setXPaddle(loadSave.xPaddle);
        paddle.setYPaddle(loadSave.yPaddle);
        main.time = loadSave.time;
        main.goldTime = loadSave.goldTime;
        ball.setVX(loadSave.vX);

        main.blocks.clear();
        main.bonuses.clear();

        for (BlockSerializable ser : loadSave.blocks) {
            Block newBlock = new Block(ser.row, ser.column, Color.BLUE, ser.type, ser.isDestroyed);
            main.blocks.add(newBlock);
        }
    }

    // Save the game state to a file
    public void saveCurrentGameState(Main main, GameController gameController, Ball ball, Paddle paddle) {
        new Thread(() -> {
            new File(loadSave.getSavePathDir());
            File file = new File(loadSave.getSavePath());
            ObjectOutputStream outputStream = null;
            try {
                outputStream = new ObjectOutputStream(new FileOutputStream(file));

                outputStream.writeInt(gameController.getLevel());
                outputStream.writeInt(gameController.getScore());
                outputStream.writeInt(gameController.getHeart());
                outputStream.writeInt(main.destroyedBlockCount);

                outputStream.writeDouble(ball.getXBall());
                outputStream.writeDouble(ball.getYBall());
                outputStream.writeDouble(paddle.getXPaddle());
                outputStream.writeDouble(paddle.getYPaddle());
                outputStream.writeLong(main.time);
                outputStream.writeLong(main.goldTime);
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
                outputStream.writeInt(main.blocks.size()); // Write the size of the block list
                for (Block block : main.blocks) {
                    outputStream.writeInt(block.row);
                    outputStream.writeInt(block.column);
                    outputStream.writeInt(block.type);
                    outputStream.writeBoolean(block.isDestroyed);
                }

                new GameUIController().showMessage("Game Saved", main);

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
}
