package brickGame.model;


import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.Serializable;

public class Block implements Serializable {

    public int row;
    public int column;
    public boolean isDestroyed;
    public int type;

    public int x;
    public int y;

    public static final int width = 100;
    public static final int height = 30;
    public static final int paddingTop = height * 2;
    public static final int paddingH = 50;
    public Rectangle rect;

    public static int NO_HIT = -1;
    public static int HIT_RIGHT = 0;
    public static int HIT_BOTTOM = 1;
    public static int HIT_LEFT = 2;
    public static int HIT_TOP = 3;

    public static int BLOCK_NORMAL = 99;
    public static int BLOCK_THREE = 100;
    public static int BLOCK_STAR = 101;
    public static int BLOCK_HEART = 102;
    public static int BLOCK_FOUL = 103;
    public static int BLOCK_LOCK = 104;



    public Block(int row, int column, int type, boolean isDestroyed) {
        this.row = row;
        this.column = column;
        this.type = type;
        this.isDestroyed = isDestroyed;
        draw();
    }

    //Draws the block based on its position and type.
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

    private void setBlock(){
        if (type == BLOCK_THREE || type == BLOCK_FOUL) {
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

    public void changeType(int type) {
        PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
        pause.setOnFinished(event -> {
            this.type = type;
            setBlock();
        });
        pause.play();
    }

    public static int getPaddingTop() {
        return Block.paddingTop;
    }

    public static int getPaddingH() {
        return Block.paddingH;
    }

    public static int getHeight() {
        return Block.height;
    }

    public static int getWidth() {
        return Block.width;
    }

}
