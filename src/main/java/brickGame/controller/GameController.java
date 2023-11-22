package brickGame.controller;

import brickGame.Main;
import brickGame.model.Block;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.Random;

public class GameController {

    private int level;
    private int score;
    private int heart;
    private final Color[] colors = new Color[]{
            Color.MAGENTA,
            Color.RED,
            Color.GOLD,
            Color.CORAL,
            Color.AQUA,
            Color.VIOLET,
            Color.GREENYELLOW,
            Color.ORANGE,
            Color.PINK,
            Color.SLATEGREY,
            Color.YELLOW,
            Color.TOMATO,
            Color.TAN,
    };

//    private Circle ball;
    private double xBall;
    private double yBall;
    private final int ballRadius = 10;
    private Pane root; // The root pane where game elements are added
    private final int paddleWidth = 130;
    private final int paddleHeight = 30;
    private double xPaddle; // X position of the break
    private double yPaddle = 640.0f; // Y position of the break
    private boolean isGoldStatus;

    private boolean isExistHeartBlock = false;

    public GameController(int level, int score, int heart, Pane root) {
        this.level = level;
        this.score = score;
        this.heart = heart;
        this.root = root;
        this.xPaddle = (double) Main.SCENE_WIDTH / 2 - (double) paddleWidth / 2; // Assuming root has a predefined width
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

    public double getXBall() {
        return xBall;
    }

    public double getYBall() {
        return yBall;
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

    public void setXBall(double xBall) {
        this.xBall = xBall;
    }

    public void setYBall(double yBall) {
        this.yBall = yBall;
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
                Color color = colors[r % colors.length];
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

    public void initPaddle(Rectangle paddle) {
        // Initialize the paddle
        paddle.setWidth(paddleWidth);
        paddle.setHeight(paddleHeight);
        paddle.setX(xPaddle);
        paddle.setY(yPaddle);

        // Apply a texture or color to the paddle
        ImagePattern pattern = new ImagePattern(new Image("block.jpg"));
        paddle.setFill(pattern);

    }

    // Initialize the game ball
    public void initBall(Circle ball) {
        Random random = new Random();
        xBall = random.nextInt(Main.SCENE_WIDTH) + 1;
        int minY = Block.getPaddingTop() + (getLevel() + 1) * Block.getHeight() + ballRadius;
        int maxY = Main.SCENE_HEIGHT - ballRadius;
        yBall = Math.max(minY, Math.min(random.nextInt(Main.SCENE_HEIGHT - 200) + minY, maxY));
        ball.setRadius(ballRadius);
        ball.setFill(new ImagePattern(new Image("ball.png")));
    }
}
