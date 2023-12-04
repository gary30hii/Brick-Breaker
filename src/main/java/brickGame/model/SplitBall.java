package brickGame.model;

import brickGame.Main;
import brickGame.controller.GameController;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class SplitBall extends Ball {
    public SplitBall(int xBall, int yBall, boolean goRightBall) {
        super(xBall, yBall);
        setGoRightBall(goRightBall);
        setGoDownBall(false);
        setFill(new ImagePattern(new Image("split-ball.png")));
    }

    @Override
    public void updateBallMovement(Main main, GameController gameController, Paddle paddle){
        double vY = 1.0;
        if (isGoDownBall()) {
            setYBall(getYBall() + vY);
        } else {
            setYBall(getYBall() - vY);
        }

        if (isGoRightBall()) {
            setXBall(getXBall() + getVX());
        } else {
            setXBall(getXBall() - getVX());
        }

        if (getYBall() <= 0) {
            setVX(1.00);
            resetCollisionStates();
            setGoDownBall(true);
            return;
        }

        if (getYBall() >= paddle.getYPaddle() - BALL_RADIUS) {
            if (getXBall() >= paddle.getXPaddle() && getXBall() <= paddle.getXPaddle() + paddle.getPaddleWidth()) {
                resetCollisionStates();

                setCollideToPaddle(true);
                setGoDownBall(false);

                double relation = (getXBall() - (paddle.getXPaddle() + (double) paddle.getPaddleWidth() / 2)) / ((double) paddle.getPaddleWidth() / 2);

                if (Math.abs(relation) <= 0.3) {
                    setVX(Math.abs(relation));
                } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                    setVX(Math.abs(relation) * 1.3);
                } else {
                    setVX(Math.abs(relation) * 1.3);
                }

                setCollideToPaddleAndMoveToRight(getXBall() - (paddle.getXPaddle() + (double) paddle.getPaddleWidth() / 2) > 0);
            }
        }

        if (getXBall() >= Main.SCENE_WIDTH) {
            resetCollisionStates();
            setCollideToRightWall(true);
        }

        if (getXBall() <= 0) {
            resetCollisionStates();
            setCollideToLeftWall(true);
        }

        if (isCollideToPaddle()) {
            setGoRightBall(isCollideToPaddleAndMoveToRight());
        }

        //Wall Collide
        if (isCollideToRightWall()) {
            setGoRightBall(false);
        }
        if (isCollideToLeftWall()) {
            setGoRightBall(true);
        }

        //Block Collide
        if (isCollideToRightBlock()) {
            resetCollisionStates();
            setGoRightBall(true);
        }

        if (isCollideToLeftBlock()) {
            resetCollisionStates();
            setGoRightBall(false);
        }

        if (isCollideToTopBlock()) {
            resetCollisionStates();
            setGoDownBall(false);
        }

        if (isCollideToBottomBlock()) {
            resetCollisionStates();
            setGoDownBall(true);
        }
    }
}
