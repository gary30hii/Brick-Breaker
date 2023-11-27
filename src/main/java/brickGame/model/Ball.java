package brickGame.model;

import brickGame.Main;
import brickGame.controller.GameController;
import brickGame.controller.Score;
import brickGame.engine.GameEngine;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.Serializable;

public class Ball extends Circle implements Serializable {
    private boolean goDownBall = true, goRightBall = true, isGoldStatus;
    private boolean collideToPaddle, collideToPaddleAndMoveToRight, collideToRightWall, collideToLeftWall;
    private boolean collideToRightBlock, collideToBottomBlock, collideToLeftBlock, collideToTopBlock;
    private double xBall;
    private double yBall;
    private double vX = 1.0;


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

    public boolean isGoDownBall() {
        return goDownBall;
    }

    public boolean isGoRightBall() {
        return goRightBall;
    }

    public boolean isGoldStatus() {
        return isGoldStatus;
    }

    public boolean isCollideToPaddle() {
        return collideToPaddle;
    }

    public boolean isCollideToPaddleAndMoveToRight() {
        return collideToPaddleAndMoveToRight;
    }

    public boolean isCollideToRightWall() {
        return collideToRightWall;
    }

    public boolean isCollideToLeftWall() {
        return collideToLeftWall;
    }

    public boolean isCollideToRightBlock() {
        return collideToRightBlock;
    }

    public boolean isCollideToBottomBlock() {
        return collideToBottomBlock;
    }

    public boolean isCollideToLeftBlock() {
        return collideToLeftBlock;
    }

    public boolean isCollideToTopBlock() {
        return collideToTopBlock;
    }

    public double getVX() {
        return vX;
    }

    public void setGoDownBall(boolean goDownBall) {
        this.goDownBall = goDownBall;
    }

    public void setGoRightBall(boolean goRightBall) {
        this.goRightBall = goRightBall;
    }

    public void setGoldStatus(boolean goldStatus) {
        isGoldStatus = goldStatus;
    }

    public void setCollideToPaddle(boolean collideToPaddle) {
        this.collideToPaddle = collideToPaddle;
    }

    public void setCollideToPaddleAndMoveToRight(boolean collideToPaddleAndMoveToRight) {
        this.collideToPaddleAndMoveToRight = collideToPaddleAndMoveToRight;
    }

    public void setCollideToRightWall(boolean collideToRightWall) {
        this.collideToRightWall = collideToRightWall;
    }

    public void setCollideToLeftWall(boolean collideToLeftWall) {
        this.collideToLeftWall = collideToLeftWall;
    }

    public void setCollideToRightBlock(boolean collideToRightBlock) {
        this.collideToRightBlock = collideToRightBlock;
    }

    public void setCollideToBottomBlock(boolean collideToBottomBlock) {
        this.collideToBottomBlock = collideToBottomBlock;
    }

    public void setCollideToLeftBlock(boolean collideToLeftBlock) {
        this.collideToLeftBlock = collideToLeftBlock;
    }

    public void setCollideToTopBlock(boolean collideToTopBlock) {
        this.collideToTopBlock = collideToTopBlock;
    }

    public void setYBall(double yBall) {
        this.yBall = yBall;
    }

    public void setVX(double vX) {
        this.vX = vX;
    }


    // Reset collision flags
    public void resetCollisionStates() {

        collideToPaddle = false;
        collideToPaddleAndMoveToRight = false;
        collideToRightWall = false;
        collideToLeftWall = false;

        collideToRightBlock = false;
        collideToBottomBlock = false;
        collideToLeftBlock = false;
        collideToTopBlock = false;
    }

    // Handle physics for the game ball
    public void updateBallMovement(Main main, GameController gameController, GameEngine engine) {

        double vY = 3.0;
        if (goDownBall) {
            setYBall(getYBall() + vY);
        } else {
            setYBall(getYBall() - vY);
        }

        if (goRightBall) {
            setXBall(getXBall() + vX);
        } else {
            setXBall(getXBall() - vX);
        }

        if (getYBall() <= 0) {
            vX = 1.000;
            resetCollisionStates();
            goDownBall = true;
            return;
        }
        if (getYBall() >= Main.SCENE_HEIGHT) {
            resetCollisionStates();
            goDownBall = false;
            if (!isGoldStatus) {
                //TODO game-over
                gameController.setHeart(gameController.getHeart() - 1);
                new Score().show((double) Main.SCENE_WIDTH / 2, (double) Main.SCENE_HEIGHT / 2, -1, main);

                if (gameController.getHeart() == 0) {
                    new Score().showGameOver(main);
                    engine.stop();
                }
            }
        }

        if (getYBall() >= Main.Y_PADDLE - Main.BALL_RADIUS) {
            if (getXBall() >= Main.X_PADDLE && getXBall() <= Main.X_PADDLE + Main.PADDLE_WIDTH) {
                resetCollisionStates();
                collideToPaddle = true;
                goDownBall = false;

                double relation = (getXBall() - Main.X_PADDLE_CENTER) / ((double) Main.PADDLE_WIDTH / 2);

                if (Math.abs(relation) <= 0.3) {
                    vX = Math.abs(relation);
                } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                    vX = (Math.abs(relation) * 1.5) + (gameController.getLevel() / 3.500);
                } else {
                    vX = (Math.abs(relation) * 2) + (gameController.getLevel() / 3.500);
                }

                collideToPaddleAndMoveToRight = getXBall() - Main.X_PADDLE_CENTER > 0;
            }
        }

        if (getXBall() >= Main.SCENE_WIDTH) {
            resetCollisionStates();
            collideToRightWall = true;
        }

        if (getXBall() <= 0) {
            resetCollisionStates();
            collideToLeftWall = true;
        }

        if (collideToPaddle) {
            goRightBall = collideToPaddleAndMoveToRight;
        }

        //Wall Collide
        if (collideToRightWall) {
            goRightBall = false;
        }
        if (collideToLeftWall) {
            goRightBall = true;
        }

        //Block Collide
        if (collideToRightBlock) {
            resetCollisionStates();
            goRightBall = true;
        }

        if (collideToLeftBlock) {
            resetCollisionStates();
            goRightBall = false;
        }

        if (collideToTopBlock) {
            resetCollisionStates();
            goDownBall = false;
        }

        if (collideToBottomBlock) {
            resetCollisionStates();
            goDownBall = true;
        }
    }
}
