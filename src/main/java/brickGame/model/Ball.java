package brickGame.model;

import brickGame.Main;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class Ball extends Circle {
    private boolean loadFromSave, goDownBall = true, goRightBall = true, isGoldStatus;
    private boolean collideToPaddle, collideToPaddleAndMoveToRight, collideToRightWall, collideToLeftWall;
    private boolean collideToRightBlock, collideToBottomBlock, collideToLeftBlock, collideToTopBlock;
    private double xBall, yBall;
    public Ball(int xBall, int yBall){
        this.xBall = xBall;
        this.yBall = yBall;
        setRadius(Main.BALL_RADIUS);
        setFill(new ImagePattern(new Image("ball.png")));
    }

    public double getXBall() {
        return xBall;
    }

    public double getYBall() {
        return yBall;
    }

    public void setXBall(double xBall) {
        this.xBall = xBall;
    }

    public void setYBall(double yBall) {
        this.yBall = yBall;
    }
}
