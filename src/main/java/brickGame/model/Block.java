package brickGame.model;


import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.Serializable;

/**
 * The Block class represents a block in the game. Each block has a position, type,
 * and can be destroyed or changed. This class handles the block's appearance and interactions.
 */
public class Block implements Serializable {

    // Block attributes
    public int row;
    public int column;
    public boolean isDestroyed;
    public int type;

    // Position and dimensions
    public int x;
    public int y;

    // Collision constants
    public static final int width = 100;
    public static final int height = 30;
    public static final int paddingTop = height * 2;
    public static final int paddingH = 50;
    public Rectangle rect;

    // Block type constants
    public static int NO_HIT = -1;
    public static int HIT_RIGHT = 0;
    public static int HIT_BOTTOM = 1;
    public static int HIT_LEFT = 2;
    public static int HIT_TOP = 3;

    // Constructor to initialize and draw the block
    public static int BLOCK_NORMAL = 99;
    public static int BLOCK_THREE = 100;
    public static int BLOCK_STAR = 101;
    public static int BLOCK_HEART = 102;
    public static int BLOCK_FOUL = 103;
    public static int BLOCK_LOCK = 104;
    public static int BLOCK_BALL = 105;


    /**
     * Constructor for Block class, initializing the block's position, type, and drawing it.
     *
     * @param row The row position of the block.
     * @param column The column position of the block.
     * @param type The type of the block.
     * @param isDestroyed The initial destruction state of the block.
     */
    public Block(int row, int column, int type, boolean isDestroyed) {
        this.row = row;
        this.column = column;
        this.type = type;
        this.isDestroyed = isDestroyed;
        draw();
    }

    /**
     * Draws the block based on its position and type. This method initializes the block's rectangle
     * and sets its position and dimensions on the game board.
     */
    private void draw() {
        x = (column * width) + paddingH;
        y = (row * height) + paddingTop;

        rect = new Rectangle();
        rect.setVisible(true);
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setX(x);
        rect.setY(y);

        // Set the border color and width
        rect.setStroke(javafx.scene.paint.Color.web("#f6c64b"));
        rect.setStrokeWidth(2);

        setBlock();
    }

    /**
     * Sets the appearance of the block based on its type. This method updates the block's visual
     * representation depending on the type of block, such as normal, star, heart, etc.
     */
    private void setBlock(){
        if (type == BLOCK_THREE || type == BLOCK_FOUL || type == BLOCK_BALL) {
            Image image = new Image("mysterious-block.png");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_HEART) {
            Image image = new Image("heart.png");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_STAR) {
            Image image = new Image("star.png");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_LOCK) {
            Image image = new Image("lock.png");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else {
            Image image = new Image("normal.png");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        }
    }

    /**
     * Checks if the ball hits the block and returns the hit direction.
     *
     * @param xBall The x-coordinate of the ball.
     * @param yBall The y-coordinate of the ball.
     * @return The hit direction: HIT_BOTTOM, HIT_TOP, HIT_RIGHT, HIT_LEFT, or NO_HIT.
     */
    public int checkHitToBlock(double xBall, double yBall, double ballRadius) {
        if (isDestroyed) {
            return NO_HIT;
        }

        double ballCenterX = xBall + ballRadius;
        double ballCenterY = yBall + ballRadius;
        double blockCenterX = x + ((double) width / 2);
        double blockCenterY = y + ((double) height / 2);

        double dx = ballCenterX - blockCenterX;
        double dy = ballCenterY - blockCenterY;

        double combinedHalfWidths = ballRadius + (double) width / 2;
        double combinedHalfHeights = ballRadius + (double) height / 2;

        if (Math.abs(dx) <= combinedHalfWidths && Math.abs(dy) <= combinedHalfHeights) {
            double overlapX = combinedHalfWidths - Math.abs(dx);
            double overlapY = combinedHalfHeights - Math.abs(dy);

            if (overlapX >= overlapY) {
                if (dy > 0) {
                    return HIT_BOTTOM; // Ball hit the bottom of the block.
                } else {
                    return HIT_TOP; // Ball hit the top of the block.
                }
            } else {
                if (dx > 0) {
                    return HIT_RIGHT; // Ball hit the right side of the block.
                } else {
                    return HIT_LEFT; // Ball hit the left side of the block.
                }
            }
        }

        return NO_HIT;
    }

    /**
     * Changes the type of the block with a brief pause for visual effect and also allow ball to move away from the block
     * (or the game logic will let the block disappear as it has collision with ball).
     * This method updates the block's appearance based on the new type after a short delay.
     *
     * @param type The new type of the block to be set.
     */
    public void changeType(int type) {
        PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
        pause.setOnFinished(event -> {
            this.type = type;
            setBlock();
        });
        pause.play();
    }

    // getter
    /**
     * Retrieves the padding from the top for all blocks.
     *
     * @return The top padding for blocks.
     */
    public static int getPaddingTop() {
        return Block.paddingTop;
    }

    /**
     * Retrieves the horizontal padding for all blocks.
     *
     * @return The horizontal padding for blocks.
     */
    public static int getPaddingH() {
        return Block.paddingH;
    }

    /**
     * Retrieves the height for all blocks.
     *
     * @return The height of blocks.
     */
    public static int getHeight() {
        return Block.height;
    }

    /**
     * Retrieves the width for all blocks.
     *
     * @return The width of blocks.
     */
    public static int getWidth() {
        return Block.width;
    }

}
