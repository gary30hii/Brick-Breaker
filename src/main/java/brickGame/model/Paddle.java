package brickGame.model;

import brickGame.Main;
import brickGame.controller.GameController;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Paddle class represents the paddle in the game. It extends Rectangle and includes
 * functionalities for its positioning and movement within the game scene.
 */
public class Paddle extends Rectangle {
    private static final Logger logger = LoggerFactory.getLogger(Paddle.class);
    private final int paddleWidth = 130;
    private final int paddleHeight = 30;
    private double xPaddle = (double) Main.SCENE_WIDTH / 2 - (double) paddleWidth / 2; // X position of the break
    private double yPaddle = 640.0; // Y position of the break

    /**
     * Constructor for Paddle class. Initializes the paddle's size, position, and appearance.
     */
    public Paddle() {
        // Initialize the paddle
        setWidth(paddleWidth);
        setHeight(paddleHeight);
        setX(xPaddle);
        setY(yPaddle);

        // Apply a texture or color to the paddle
        ImagePattern pattern = new ImagePattern(new Image("block.jpg"));
        setFill(pattern);
    }

    /**
     * Retrieves the width of the paddle.
     *
     * @return The width of the paddle.
     */
    public int getPaddleWidth() {
        return paddleWidth;
    }

    /**
     * Retrieves the height of the paddle.
     *
     * @return The height of the paddle.
     */
    public int getPaddleHeight() {
        return paddleHeight;
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
     * Sets the X position of the paddle.
     *
     * @param xPaddle The new X coordinate of the paddle.
     */
    public void setXPaddle(double xPaddle) {
        this.xPaddle = xPaddle;
    }

    /**
     * Sets the Y position of the paddle.
     *
     * @param yPaddle The new Y coordinate of the paddle.
     */
    public void setYPaddle(double yPaddle) {
        this.yPaddle = yPaddle;
    }

    /**
     * Moves the paddle in the specified direction.
     * The paddle's movement is restricted to the bounds of the game scene.
     *
     * @param direction The direction to move the paddle (left or right).
     */
    public void movePaddle(int direction) {
        new Thread(() -> {
            int sleepTime = 1;
            for (int i = 0; i < 30; i++) {
                if (getXPaddle() >= (Main.SCENE_WIDTH - getPaddleWidth()) && direction == GameController.getRIGHT()) {
                    return; //paddle stop moving to the right when it touches the right wall
                }
                if (getXPaddle() <= 0 && direction == GameController.getLEFT()) {
                    return; //paddle stop moving to the left when it touch the left wall
                }
                if (direction == GameController.getRIGHT()) {
                    setXPaddle(getXPaddle() + 1);
                } else {
                    setXPaddle(getXPaddle() - 1);
                }
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    logger.error("An error occurred in move() Method: " + e.getMessage(), e);
                }
                if (i >= 20) {
                    sleepTime = i;
                }
            }
        }).start();
    }
}
