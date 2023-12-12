package brickGame.model;

import brickGame.Main;
import brickGame.controller.GameController;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

/**
 * The SplitBall class represents a specialized type of ball in the game,
 * which behaves similarly to a standard ball but with some different behavior.
 * It extends the Ball class, inheriting its basic functionality.
 */
public class SplitBall extends Ball {

    /**
     * Constructor for SplitBall class. Initializes the split ball with its position and direction.
     * Sets the visual appearance specific to a split ball.
     *
     * @param xBall The initial X position of the ball.
     * @param yBall The initial Y position of the ball.
     * @param goRightBall The initial horizontal direction of the ball.
     */
    public SplitBall(int xBall, int yBall, boolean goRightBall) {
        super(xBall, yBall);
        setGoRightBall(goRightBall);
        setGoDownBall(false);
        setFill(new ImagePattern(new Image("split-ball.png")));
    }

    /**
     * Overrides the updateBallMovement method from the Ball class to define the specific behavior
     * for the movement of the split ball. This includes custom logic for collisions and movement.
     *
     * @param main The main class instance of the application.
     * @param gameController The game controller managing the game logic.
     * @param paddle The paddle object in the game.
     */
    @Override
    public void updateBallMovement(Main main, GameController gameController, Paddle paddle){
        // Define vertical and horizontal movement speed
        double vY = 1.0;

        // Update the ball's vertical position
        if (isGoDownBall()) {
            setYBall(getYBall() + vY);
        } else {
            setYBall(getYBall() - vY);
        }

        // Update the ball's horizontal position
        if (isGoRightBall()) {
            setXBall(getXBall() + getVX());
        } else {
            setXBall(getXBall() - getVX());
        }

        // Handle ball's collision with the top boundary
        if (getYBall() <= 0) {
            setVX(1.00);
            resetCollisionStates();
            setGoDownBall(true);
            return;
        }

        // Handle ball's collision with the paddle
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

        // Handle ball's collision with the right wall
        if (getXBall() >= Main.SCENE_WIDTH) {
            resetCollisionStates();
            setCollideToRightWall(true);
        }

        // Handle ball's collision with the left wall
        if (getXBall() <= 0) {
            resetCollisionStates();
            setCollideToLeftWall(true);
        }

        // Update the direction based on collision
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
