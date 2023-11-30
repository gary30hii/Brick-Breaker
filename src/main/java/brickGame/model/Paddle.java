package brickGame.model;

import brickGame.Main;
import brickGame.controller.GameController;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Paddle extends Rectangle {
    private static final Logger logger = LoggerFactory.getLogger(Paddle.class);
    private final int paddleWidth = 130;
    private final int paddleHeight = 30;
    private double xPaddle = (double) Main.SCENE_WIDTH / 2 - (double) paddleWidth / 2; // X position of the break
    private double yPaddle = 640.0; // Y position of the break

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

    public int getPaddleWidth() {
        return paddleWidth;
    }

    public int getPaddleHeight() {
        return paddleHeight;
    }

    public double getXPaddle() {
        return xPaddle;
    }

    public double getYPaddle() {
        return yPaddle;
    }

    public void setXPaddle(double xPaddle) {
        this.xPaddle = xPaddle;
    }

    public void setYPaddle(double yPaddle) {
        this.yPaddle = yPaddle;
    }

    // Move the paddle left or right
    public void movePaddle(int direction) {
        new Thread(() -> {
            int sleepTime = 1;
            for (int i = 0; i < 30; i++) {
                if (getXPaddle() >= (Main.SCENE_WIDTH - getPaddleWidth()) && direction == GameController.RIGHT) {
                    return; //paddle stop moving to the right when it touches the right wall
                }
                if (getXPaddle() <= 0 && direction == GameController.LEFT) {
                    return; //paddle stop moving to the left when it touch the left wall
                }
                if (direction == GameController.RIGHT) {
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
