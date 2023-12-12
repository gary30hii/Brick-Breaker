package brickGame.model;

import brickGame.Main;
import brickGame.controller.GameController;
import brickGame.controller.GameUIController;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.Serializable;

/**
 * The Ball class represents the ball used in the game. It extends Circle and includes
 * methods to handle its movement and collisions.
 */
public class Ball extends Circle implements Serializable {
    private boolean goDownBall = true, goRightBall = true, isGoldStatus;

    // Collision flags
    private boolean collideToPaddle, collideToPaddleAndMoveToRight, collideToRightWall, collideToLeftWall;
    private boolean collideToRightBlock, collideToBottomBlock, collideToLeftBlock, collideToTopBlock;

    // Ball position and velocity
    private double xBall;
    private double yBall;
    private double vX = 1.0;
    public static final int BALL_RADIUS = 10;

    /**
     * Constructor for Ball class, setting initial position and image for the ball.
     *
     * @param xBall The initial X position of the ball.
     * @param yBall The initial Y position of the ball.
     */
    public Ball(int xBall, int yBall){
        this.xBall = xBall;
        this.yBall = yBall;
        setRadius(BALL_RADIUS);
        setFill(new ImagePattern(new Image("ball.png")));
    }

    // Getter and setter methods
    /**
     * Retrieves the current X position of the ball.
     *
     * @return The X coordinate of the ball.
     */
    public double getXBall() {
        return xBall;
    }

    /**
     * Retrieves the current Y position of the ball.
     *
     * @return The Y coordinate of the ball.
     */
    public double getYBall() {
        return yBall;
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
     * Checks if the ball is in gold status.
     *
     * @return True if the ball is in gold status, false otherwise.
     */
    public boolean isGoldStatus() {
        return isGoldStatus;
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
     * Checks if the ball has collided with the right block.
     *
     * @return True if the ball has collided with the right block, false otherwise.
     */
    public boolean isCollideToRightBlock() {
        return collideToRightBlock;
    }

    /**
     * Checks if the ball has collided with the bottom block.
     *
     * @return True if the ball has collided with the bottom block, false otherwise.
     */
    public boolean isCollideToBottomBlock() {
        return collideToBottomBlock;
    }

    /**
     * Checks if the ball has collided with the left block.
     *
     * @return True if the ball has collided with the left block, false otherwise.
     */
    public boolean isCollideToLeftBlock() {
        return collideToLeftBlock;
    }

    /**
     * Checks if the ball has collided with the top block.
     *
     * @return True if the ball has collided with the top block, false otherwise.
     */
    public boolean isCollideToTopBlock() {
        return collideToTopBlock;
    }

    /**
     * Retrieves the horizontal velocity of the ball.
     *
     * @return The horizontal velocity (vX) of the ball.
     */
    public double getVX() {
        return vX;
    }

    /**
     * Sets the direction of the ball's vertical movement.
     *
     * @param goDownBall If true, the ball moves downward; if false, it moves upward.
     */
    public void setGoDownBall(boolean goDownBall) {
        this.goDownBall = goDownBall;
    }

    /**
     * Sets the direction of the ball's horizontal movement.
     *
     * @param goRightBall If true, the ball moves to the right; if false, it moves to the left.
     */
    public void setGoRightBall(boolean goRightBall) {
        this.goRightBall = goRightBall;
    }

    /**
     * Sets the gold status of the ball.
     *
     * @param goldStatus If true, the ball is in gold status; otherwise, it is not.
     */
    public void setGoldStatus(boolean goldStatus) {
        isGoldStatus = goldStatus;
    }

    /**
     * Sets the collision status with the paddle.
     *
     * @param collideToPaddle If true, the ball has collided with the paddle; otherwise, it has not.
     */
    public void setCollideToPaddle(boolean collideToPaddle) {
        this.collideToPaddle = collideToPaddle;
    }

    /**
     * Sets the ball's movement direction after colliding with the paddle.
     *
     * @param collideToPaddleAndMoveToRight If true, the ball moves to the right after colliding with the paddle; otherwise, to the left.
     */
    public void setCollideToPaddleAndMoveToRight(boolean collideToPaddleAndMoveToRight) {
        this.collideToPaddleAndMoveToRight = collideToPaddleAndMoveToRight;
    }

    /**
     * Sets the collision status with the right wall.
     *
     * @param collideToRightWall If true, the ball has collided with the right wall; otherwise, it has not.
     */
    public void setCollideToRightWall(boolean collideToRightWall) {
        this.collideToRightWall = collideToRightWall;
    }

    /**
     * Sets the collision status with the left wall.
     *
     * @param collideToLeftWall If true, the ball has collided with the left wall; otherwise, it has not.
     */
    public void setCollideToLeftWall(boolean collideToLeftWall) {
        this.collideToLeftWall = collideToLeftWall;
    }

    /**
     * Sets the collision status with a right block.
     *
     * @param collideToRightBlock If true, the ball has collided with a right block; otherwise, it has not.
     */
    public void setCollideToRightBlock(boolean collideToRightBlock) {
        this.collideToRightBlock = collideToRightBlock;
    }

    /**
     * Sets the collision status with a bottom block.
     *
     * @param collideToBottomBlock If true, the ball has collided with a bottom block; otherwise, it has not.
     */
    public void setCollideToBottomBlock(boolean collideToBottomBlock) {
        this.collideToBottomBlock = collideToBottomBlock;
    }

    /**
     * Sets the collision status with a left block.
     *
     * @param collideToLeftBlock If true, the ball has collided with a left block; otherwise, it has not.
     */
    public void setCollideToLeftBlock(boolean collideToLeftBlock) {
        this.collideToLeftBlock = collideToLeftBlock;
    }

    /**
     * Sets the collision status with a top block.
     *
     * @param collideToTopBlock If true, the ball has collided with a top block; otherwise, it has not.
     */
    public void setCollideToTopBlock(boolean collideToTopBlock) {
        this.collideToTopBlock = collideToTopBlock;
    }

    /**
     * Sets the X position of the ball.
     *
     * @param xBall The X coordinate to set for the ball.
     */
    public void setXBall(double xBall) {
        this.xBall = xBall;
    }

    /**
     * Sets the Y position of the ball.
     *
     * @param yBall The Y coordinate to set for the ball.
     */
    public void setYBall(double yBall) {
        this.yBall = yBall;
    }

    /**
     * Sets the horizontal velocity of the ball.
     *
     * @param vX The horizontal velocity to set for the ball.
     */
    public void setVX(double vX) {
        this.vX = vX;
    }

    /**
     * Resets the collision states of the ball. This includes resetting all flags that
     * indicate collision with various objects in the game.
     */
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

    /**
     * Updates the ball's movement based on the current game state. It handles the ball's
     * physics and interactions with other game elements like the paddle and walls.
     *
     * @param main The main class instance of the application.
     * @param gameController The game controller managing the game logic.
     * @param paddle The paddle object in the game.
     */
    public void updateBallMovement(Main main, GameController gameController, Paddle paddle) {

        // Set the vertical speed of the ball
        double vY = 1.0;

        // Move the ball up or down based on the direction flag
        if (goDownBall) {
            setYBall(getYBall() + vY); // Move the ball downwards
        } else {
            setYBall(getYBall() - vY); // Move the ball upwards
        }

        // Move the ball right or left based on the direction flag
        if (goRightBall) {
            setXBall(getXBall() + vX); // Move the ball to the right
        } else {
            setXBall(getXBall() - vX); // Move the ball to the left
        }

        // Handle when the ball reaches the top of the scene
        if (getYBall() <= 0) {
            vX = 1.000; // Reset horizontal velocity
            resetCollisionStates(); // Reset collision states
            goDownBall = true; // Set the ball to move downwards
            return;
        }

        // Handle when the ball reaches the bottom of the scene
        if (getYBall() >= Main.SCENE_HEIGHT) {
            resetCollisionStates(); // Reset collision states
            goDownBall = false; // Stop the ball from moving downwards

            // Decrease heart count and check for game over
            if (!isGoldStatus) {
                gameController.setHeart(gameController.getHeart() - 1); // Decrease heart count
                new GameUIController().showMessage("Heart -1", main); // Display heart decrease message

                // Check for game over if hearts reach zero
                if (gameController.getHeart() == 0) {
                    main.showGameOver(); // Display game over screen
                }
            }
        }

        // Handle collision with the paddle
        if (getYBall() >= paddle.getYPaddle() - BALL_RADIUS) {
            if (getXBall() >= paddle.getXPaddle() && getXBall() <= paddle.getXPaddle() + paddle.getPaddleWidth()) {
                resetCollisionStates(); // Reset collision states
                collideToPaddle = true; // Set collision with paddle flag
                goDownBall = false; // Set the ball to move upwards

                // Determine the relation of the ball position to the center of the paddle
                double relation = (getXBall() - (paddle.getXPaddle() + (double) paddle.getPaddleWidth() / 2)) / ((double) paddle.getPaddleWidth() / 2);

                // Adjust horizontal velocity based on the relation to the center of the paddle
                if (Math.abs(relation) <= 0.3) {
                    vX = Math.abs(relation);
                } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                    vX = (Math.abs(relation) * 1.3) + (gameController.getLevel() / 20.000);
                } else {
                    vX = (Math.abs(relation) * 1.3) + (gameController.getLevel() / 20.000);
                }

                collideToPaddleAndMoveToRight = getXBall() - (paddle.getXPaddle() + (double) paddle.getPaddleWidth() / 2) > 0;
            }
        }

        // Handle collision with the right wall
        if (getXBall() >= Main.SCENE_WIDTH) {
            resetCollisionStates(); // Reset collision states
            collideToRightWall = true; // Set collision with the right wall flag
        }

        // Handle collision with the left wall
        if (getXBall() <= 0) {
            resetCollisionStates(); // Reset collision states
            collideToLeftWall = true; // Set collision with the left wall flag
        }

        // Update ball movement after collision with the paddle
        if (collideToPaddle) {
            goRightBall = collideToPaddleAndMoveToRight;
        }

        // Handle ball collision with the walls
        if (collideToRightWall) {
            goRightBall = false; // Set the ball to move to the left after hitting the right wall
        }
        if (collideToLeftWall) {
            goRightBall = true; // Set the ball to move to the right after hitting the left wall
        }

        // Handle ball collision with blocks (assuming variables are set elsewhere)
        if (collideToRightBlock) {
            resetCollisionStates(); // Reset collision states
            goRightBall = true; // Set the ball to move to the right after hitting a block on the right
        }

        if (collideToLeftBlock) {
            resetCollisionStates(); // Reset collision states
            goRightBall = false; // Set the ball to move to the left after hitting a block on the left
        }

        if (collideToTopBlock) {
            resetCollisionStates(); // Reset collision states
            goDownBall = false; // Set the ball to move upwards after hitting a block on the top
        }

        if (collideToBottomBlock) {
            resetCollisionStates(); // Reset collision states
            goDownBall = true; // Set the ball to move downwards after hitting a block on the bottom
        }
    }
}
