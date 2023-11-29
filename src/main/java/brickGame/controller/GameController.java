package brickGame.controller;

import brickGame.Main;
import brickGame.model.Ball;
import brickGame.model.Block;
import brickGame.model.Paddle;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Random;

public class GameController implements EventHandler<KeyEvent> {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private static final int LEFT = 1, RIGHT = 2;
    private Main main;
    private int level;
    private int score;
    private int heart;
    private Paddle paddle;
    private Ball ball;
    private Pane root; // The root pane where game elements are added
    private boolean isExistHeartBlock = false;
    private final int finalLevel = 3;


    public GameController(Main main, int level, int score, int heart) {
        this.main =main;
        this.level = level;
        this.score = score;
        this.heart = heart;
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public int getHeart() {
        return heart;
    }

    public boolean isExistHeartBlock() {
        return isExistHeartBlock;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public void setExistHeartBlock(boolean existHeartBlock) {
        isExistHeartBlock = existHeartBlock;
    }

    // Initialize the game board
    public void initBoard(ArrayList<Block> blocks) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < getLevel() + 1; j++) {
                int r = new Random().nextInt(500);
                if (r % 5 == 0) {
                    continue; // Empty block
                }
                int type = determineBlockType(r);
                Color color = Color.BLUE;
                blocks.add(new Block(j, i, color, type, false));
            }
        }
    }

    private int determineBlockType(int randomValue) {
        // Logic to determine the block type
        if (randomValue % 10 == 1) {
            return Block.BLOCK_CHOCO;
        } else if (randomValue % 10 == 2) {
            if (!isExistHeartBlock) {
                isExistHeartBlock = true;
                return Block.BLOCK_HEART;
            } else {
                return Block.BLOCK_NORMAL;
            }
        } else if (randomValue % 10 == 3) {
            return Block.BLOCK_STAR;
        } else {
            return Block.BLOCK_NORMAL;
        }
    }

    public Paddle initPaddle() {
        this.paddle = new Paddle();
        return paddle;
    }

    // Initialize the game ball
    public Ball initBall() {
        Random random = new Random();
        int xBall = random.nextInt(Main.SCENE_WIDTH) + 1;
        int minY = Block.getPaddingTop() + (getLevel() + 1) * Block.getHeight() + Ball.BALL_RADIUS;
        int maxY = Main.SCENE_HEIGHT - Ball.BALL_RADIUS;
        int yBall = Math.max(minY, Math.min(random.nextInt(Main.SCENE_HEIGHT - 200) + minY, maxY));
        ball = new Ball(xBall, yBall);
        return ball;
    }

    // Handle key events for game controls
    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                movePaddle(LEFT);
                break;
            case RIGHT:
                movePaddle(RIGHT);
                break;
            case S:
                new FileController().saveCurrentGameState(main, this, ball, paddle);
                break;
        }
    }

    // Move the paddle left or right
    private void movePaddle(int direction) {
        new Thread(() -> {
            int sleepTime = 1;
            for (int i = 0; i < 30; i++) {
                if (paddle.getXPaddle() >= (Main.SCENE_WIDTH - paddle.getPaddleWidth()) && direction == RIGHT) {
                    return; //paddle stop moving to the right when it touches the right wall
                }
                if (paddle.getXPaddle() <= 0 && direction == LEFT) {
                    return; //paddle stop moving to the left when it touch the left wall
                }
                if (direction == RIGHT) {
                    paddle.setXPaddle(paddle.getXPaddle() + 2);
                } else {
                    paddle.setXPaddle(paddle.getXPaddle() - 2);
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

    public void levelUp(Main main) {
        setLevel(getLevel() + 1);

        if (getLevel() > 1 && getLevel() != finalLevel) {
            new GameUIController().showMessage("Level Up :)", main);
        }

        if (getLevel() == finalLevel) {
            main.resetGameToStart();
            main.showWin();
        }
    }
}
