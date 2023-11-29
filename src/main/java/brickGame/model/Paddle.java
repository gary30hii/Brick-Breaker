package brickGame.model;

import brickGame.Main;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Paddle extends Rectangle {
    private final int paddleWidth = 130;
    private final int paddleHeight = 30;
    private double xPaddle = (double) Main.SCENE_WIDTH / 2 - (double) paddleWidth / 2; // X position of the break
    private double yPaddle = 640.0; // Y position of the break

    public Paddle(){
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
}
