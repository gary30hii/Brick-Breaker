package brickGame;

import brickGame.model.BlockSerializable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadSave {
    private static final Logger logger = LoggerFactory.getLogger(LoadSave.class);

    // Flags indicating game state
    public boolean isExistHeartBlock;
    public boolean isGoldStatus;
    public boolean goDownBall;
    public boolean goRightBall;
    public boolean collideToPaddle;
    public boolean collideToPaddleAndMoveToRight;
    public boolean collideToRightWall;
    public boolean collideToLeftWall;
    public boolean collideToRightBlock;
    public boolean collideToBottomBlock;
    public boolean collideToLeftBlock;
    public boolean collideToTopBlock;

    // Game progression data
    public int level;
    public int score;
    public int heart;
    public int destroyedBlockCount;
    public double xBall;
    public double yBall;
    public double xPaddle;
    public double yPaddle;
    public double centerPaddleX;
    public long time;
    public long goldTime;
    public double vX;
    public ArrayList<BlockSerializable> blocks = new ArrayList<>();


    // Read saved game data
    public void read() {

        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(Main.SAVE_PATH));

            level = inputStream.readInt();
            score = inputStream.readInt();
            heart = inputStream.readInt();
            destroyedBlockCount = inputStream.readInt();

            xBall = inputStream.readDouble();
            yBall = inputStream.readDouble();
            xPaddle = inputStream.readDouble();
            yPaddle = inputStream.readDouble();
            centerPaddleX = inputStream.readDouble();
            time = inputStream.readLong();
            goldTime = inputStream.readLong();
            vX = inputStream.readDouble();


            isExistHeartBlock = inputStream.readBoolean();
            isGoldStatus = inputStream.readBoolean();
            goDownBall = inputStream.readBoolean();
            goRightBall = inputStream.readBoolean();
            collideToPaddle = inputStream.readBoolean();
            collideToPaddleAndMoveToRight = inputStream.readBoolean();
            collideToRightWall = inputStream.readBoolean();
            collideToLeftWall = inputStream.readBoolean();
            collideToRightBlock = inputStream.readBoolean();
            collideToBottomBlock = inputStream.readBoolean();
            collideToLeftBlock = inputStream.readBoolean();
            collideToTopBlock = inputStream.readBoolean();

            // Read and reconstruct blocks
            int blocksSize = inputStream.readInt();
            blocks = new ArrayList<>(blocksSize);
            for (int i = 0; i < blocksSize; i++) {
                int row = inputStream.readInt();
                int column = inputStream.readInt();
                int type = inputStream.readInt();
                boolean isDestroyed = inputStream.readBoolean();
                blocks.add(new BlockSerializable(row, column, type, isDestroyed));
            }
        } catch (IOException e) {
            logger.error("An error occurred in read() Method: " + e.getMessage(), e);
        }
    }
}
