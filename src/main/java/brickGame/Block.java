package brickGame;


import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

public class Block implements Serializable {
    private static final Block block = new Block(-1, -1, Color.TRANSPARENT, 99);

    public int row;
    public int column;

    public boolean isDestroyed = false;

    private final Color color;
    public int type;

    public int x;
    public int y;

    private final int width = 100;
    private final int height = 30;
    private final int paddingTop = height * 2;
    private final int paddingH = 50;
    public Rectangle rect;


    public static int NO_HIT = -1;
    public static int HIT_RIGHT = 0;
    public static int HIT_BOTTOM = 1;
    public static int HIT_LEFT = 2;
    public static int HIT_TOP = 3;

    public static int BLOCK_NORMAL = 99;
    public static int BLOCK_CHOCO = 100;
    public static int BLOCK_STAR = 101;
    public static int BLOCK_HEART = 102;


    public Block(int row, int column, Color color, int type) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.type = type;

        draw();
    }

    //Draws the block based on its position and type.
    private void draw() {
        x = (column * width) + paddingH;
        y = (row * height) + paddingTop;

        rect = new Rectangle();
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setX(x);
        rect.setY(y);

        if (type == BLOCK_CHOCO) {
            Image image = new Image("choco.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_HEART) {
            Image image = new Image("heart.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_STAR) {
            Image image = new Image("star.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else {
            rect.setFill(color);
        }

    }

    /**
     * Checks if the ball hits the block and returns the hit direction.
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

        double combinedHalfWidths = (ballRadius + width) / 2;
        double combinedHalfHeights = (ballRadius + height) / 2;

        if (Math.abs(dx) <= combinedHalfWidths && Math.abs(dy) <= combinedHalfHeights) {
            double overlapX = combinedHalfWidths - Math.abs(dx);
            double overlapY = combinedHalfHeights - Math.abs(dy);

            if (overlapX >= overlapY) {
                if (dy > 0) {
                    System.out.println("bottom");
                    return HIT_BOTTOM; // Ball hit the bottom of the block.
                } else {
                    System.out.println("top");
                    return HIT_TOP; // Ball hit the top of the block.
                }
            } else {
                if (dx > 0) {
                    System.out.println("right");
                    return HIT_RIGHT; // Ball hit the right side of the block.
                } else {
                    System.out.println("left");
                    return HIT_LEFT; // Ball hit the left side of the block.
                }
            }
        }

        return NO_HIT;
    }


    public static int getPaddingTop() {
        return block.paddingTop;
    }

    public static int getPaddingH() {
        return block.paddingH;
    }

    public static int getHeight() {
        return block.height;
    }

    public static int getWidth() {
        return block.width;
    }

}
