package brickGame;

import brickGame.model.BlockSerializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;

/**
 * The LoadSave class is responsible for handling the saving and loading of game state data,
 * including game progression and leaderboard scores. It manages serialization and deserialization
 * of game data to and from files.
 */
public class LoadSave {
    private static final Logger logger = LoggerFactory.getLogger(LoadSave.class);
    private final String gameDataPath = "data/save.mdds";
    private final String leaderboardDataPath = "data/leaderboard.mdds";

    // Flags indicating game state
    private boolean isExistHeartBlock;
    private boolean isExistSplitBall;
    private boolean isGoldStatus;
    private boolean goDownBall;
    private boolean goRightBall;
    private boolean collideToPaddle;
    private boolean collideToPaddleAndMoveToRight;
    private boolean collideToRightWall;
    private boolean collideToLeftWall;
    private boolean collideToRightBlock;
    private boolean collideToBottomBlock;
    private boolean collideToLeftBlock;
    private boolean collideToTopBlock;

    // Game progression data
    private int level;
    private int score;
    private int heart;
    private int destroyedBlockCount;
    private double xBall;
    private double yBall;
    private double xPaddle;
    private double yPaddle;
    private long time;
    private long goldTime;
    private double vX;
    private ArrayList<BlockSerializable> blocks = new ArrayList<>();

    //leaderboard data
    private int bestScore = 0;
    private int secondBestScore = 0;
    private int thirdBestScore = 0;

    //getter
    /**
     * Checks if a heart block exists in the game.
     *
     * @return True if a heart block exists, false otherwise.
     */
    public boolean isExistHeartBlock() {
        return isExistHeartBlock;
    }

    /**
     * Checks if a split ball exists in the game.
     *
     * @return True if a split ball exists, false otherwise.
     */
    public boolean isExistSplitBall() {
        return isExistSplitBall;
    }

    /**
     * Checks if the ball is in gold status.
     *
     * @return True if the ball is in gold status, false otherwise.
     */
    public boolean isGoldStatus() {
        return isGoldStatus;
    }

    /**
     * Checks if the ball is moving downwards.
     *
     * @return True if the ball is moving downwards, false otherwise.
     */
    public boolean isGoDownBall() {
        return goDownBall;
    }

    /**
     * Checks if the ball is moving to the right.
     *
     * @return True if the ball is moving to the right, false otherwise.
     */
    public boolean isGoRightBall() {
        return goRightBall;
    }

    /**
     * Checks if the ball has collided with the paddle.
     *
     * @return True if the ball has collided with the paddle, false otherwise.
     */
    public boolean isCollideToPaddle() {
        return collideToPaddle;
    }

    /**
     * Checks if the ball has collided with the paddle and is moving to the right.
     *
     * @return True if the ball has collided with the paddle and is moving to the right, false otherwise.
     */
    public boolean isCollideToPaddleAndMoveToRight() {
        return collideToPaddleAndMoveToRight;
    }

    /**
     * Checks if the ball has collided with the right wall.
     *
     * @return True if the ball has collided with the right wall, false otherwise.
     */
    public boolean isCollideToRightWall() {
        return collideToRightWall;
    }

    /**
     * Checks if the ball has collided with the left wall.
     *
     * @return True if the ball has collided with the left wall, false otherwise.
     */
    public boolean isCollideToLeftWall() {
        return collideToLeftWall;
    }

    /**
     * Checks if the ball has collided with a right block.
     *
     * @return True if the ball has collided with a right block, false otherwise.
     */
    public boolean isCollideToRightBlock() {
        return collideToRightBlock;
    }

    /**
     * Checks if the ball has collided with a bottom block.
     *
     * @return True if the ball has collided with a bottom block, false otherwise.
     */
    public boolean isCollideToBottomBlock() {
        return collideToBottomBlock;
    }

    /**
     * Checks if the ball has collided with a left block.
     *
     * @return True if the ball has collided with a left block, false otherwise.
     */
    public boolean isCollideToLeftBlock() {
        return collideToLeftBlock;
    }

    /**
     * Checks if the ball has collided with a top block.
     *
     * @return True if the ball has collided with a top block, false otherwise.
     */
    public boolean isCollideToTopBlock() {
        return collideToTopBlock;
    }

    /**
     * Retrieves the current game level.
     *
     * @return The current game level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Retrieves the current game score.
     *
     * @return The current game score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Retrieves the current heart count in the game.
     *
     * @return The current heart count.
     */
    public int getHeart() {
        return heart;
    }

    /**
     * Retrieves the count of destroyed blocks in the game.
     *
     * @return The number of destroyed blocks.
     */
    public int getDestroyedBlockCount() {
        return destroyedBlockCount;
    }

    /**
     * Retrieves the X position of the ball.
     *
     * @return The X coordinate of the ball.
     */
    public double getXBall() {
        return xBall;
    }

    /**
     * Retrieves the Y position of the ball.
     *
     * @return The Y coordinate of the ball.
     */
    public double getYBall() {
        return yBall;
    }

    /**
     * Retrieves the X position of the paddle.
     *
     * @return The X coordinate of the paddle.
     */
    public double getXPaddle() {
        return xPaddle;
    }

    /**
     * Retrieves the Y position of the paddle.
     *
     * @return The Y coordinate of the paddle.
     */
    public double getYPaddle() {
        return yPaddle;
    }

    /**
     * Retrieves the current game time.
     *
     * @return The current game time.
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
     * Retrieves the horizontal velocity of the ball.
     *
     * @return The horizontal velocity of the ball.
     */
    public double getVX() {
        return vX;
    }

    /**
     * Retrieves the list of serializable blocks.
     *
     * @return The list of serializable blocks.
     */
    public ArrayList<BlockSerializable> getBlocks() {
        return blocks;
    }

    /**
     * Retrieves the best score on the leaderboard.
     *
     * @return The best score.
     */
    public int getBestScore() {
        return bestScore;
    }

    /**
     * Retrieves the second-best score on the leaderboard.
     *
     * @return The second-best score.
     */
    public int getSecondBestScore() {
        return secondBestScore;
    }

    /**
     * Retrieves the third-best score on the leaderboard.
     *
     * @return The third-best score.
     */
    public int getThirdBestScore() {
        return thirdBestScore;
    }

    /**
     * Retrieves the file path for saved game data.
     *
     * @return The file path for saved game data.
     */
    public String getGameDataPath() {
        return gameDataPath;
    }

    /**
     * Retrieves the file path for saved leaderboard data.
     *
     * @return The file path for saved leaderboard data.
     */
    public String getLeaderboardDataPath() {
        return leaderboardDataPath;
    }

    /**
     * Retrieves the directory path for save files.
     *
     * @return The directory path for save files.
     */
    public String getSavePathDir() {
        return "data/";
    }

    /**
     * Reads and loads the saved game data from save.mdds.
     * It reconstructs the game state including level, score, heart, and block positions.
     */
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

    /**
     * Reads and loads the leaderboard data from leaderboard.mdds.
     * It updates the top three scores from the saved leaderboard data.
     */
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
