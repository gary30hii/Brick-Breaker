package brickGame;

import brickGame.model.BlockSerializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;

public class LoadSave {
    private static final Logger logger = LoggerFactory.getLogger(LoadSave.class);
    private final String gameDataPath = "data/save.mdds";
    private final String leaderboardDataPath = "data/leaderboard.mdds";
    // Flags indicating game state
    public boolean isExistHeartBlock;
    public boolean isExistSplitBall;

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
    public long time;
    public long goldTime;
    public double vX;
    public ArrayList<BlockSerializable> blocks = new ArrayList<>();

    //leaderboard data
    public int bestScore = 0;
    public int secondBestScore = 0;
    public int thirdBestScore = 0;

    //getter
    public String getGameDataPath() {
        return gameDataPath;
    }
    public String getLeaderboardDataPath() {
        return leaderboardDataPath;
    }
    public String getSavePathDir() {
        return "data/";
    }

    // Read saved game data
    public void readGameData() {

        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(gameDataPath));

            level = inputStream.readInt();
            score = inputStream.readInt();
            heart = inputStream.readInt();
            destroyedBlockCount = inputStream.readInt();

            xBall = inputStream.readDouble();
            yBall = inputStream.readDouble();
            xPaddle = inputStream.readDouble();
            yPaddle = inputStream.readDouble();
            time = inputStream.readLong();
            goldTime = inputStream.readLong();
            vX = inputStream.readDouble();


            isExistHeartBlock = inputStream.readBoolean();
            isExistSplitBall = inputStream.readBoolean();
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

    public void readLeaderboard() {
        File file = new File(leaderboardDataPath);

        // Check if the file exists and is not empty
        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
                // Attempt to read the scores with additional checks
                if (inputStream.available() >= Integer.BYTES) {
                    bestScore = inputStream.readInt();
                }
                if (inputStream.available() >= Integer.BYTES) {
                    secondBestScore = inputStream.readInt();
                }
                if (inputStream.available() >= Integer.BYTES) {
                    thirdBestScore = inputStream.readInt();
                }
            } catch (EOFException e) {
                logger.warn("Reached end of file before reading all data in readLeaderboard(), file might be corrupt or incorrectly written.");
                // Set default values in case of incomplete data
                bestScore = 0;
                secondBestScore = 0;
                thirdBestScore = 0;
            } catch (IOException e) {
                logger.error("An error occurred in readLeaderboard() Method: " + e.getMessage(), e);
            }
        } else {
            // Initialize to default values if file does not exist or is empty
            bestScore = 0;
            secondBestScore = 0;
            thirdBestScore = 0;
        }
    }
}
