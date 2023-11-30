package brickGame.controller;

import brickGame.Main;
import brickGame.engine.GameEngine;
import brickGame.model.Ball;
import brickGame.model.Block;
import brickGame.model.Bonus;
import brickGame.model.Paddle;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Random;

public class GameController implements EventHandler<KeyEvent>, GameEngine.OnAction {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    private Main main;
    private int level;
    private int score;
    private int heart;
    private Paddle paddle;
    private Ball ball;
    public final ArrayList<Block> blocks = new ArrayList<>();
    public final ArrayList<Bonus> bonuses = new ArrayList<>();
    private boolean isExistHeartBlock = false;
    private final int finalLevel = 18;


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
                paddle.movePaddle(LEFT);
                break;
            case RIGHT:
                paddle.movePaddle(RIGHT);
                break;
            case S:
                new FileController().saveCurrentGameState(main, this, ball, paddle);
                break;
        }
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

    // Method to handle game updates
    @Override
    public void updateGameFrame() {
        Platform.runLater(() -> {

            main.updateGameData(getScore(), getHeart());
            paddle.setX(paddle.getXPaddle());
            paddle.setY(paddle.getYPaddle());
            ball.setCenterX(ball.getXBall());
            ball.setCenterY(ball.getYBall());

            for (Bonus choco : bonuses) {
                choco.choco.setY(choco.y);
            }

            if (main.destroyedBlockCount == blocks.size() && getLevel() < finalLevel) {
                main.prepareForNextLevel();
            }
        });

        if (ball.getYBall() >= Block.getPaddingTop() && ball.getYBall() <= (Block.getHeight() * (getLevel() + 1)) + Block.getPaddingTop()) {
            for (final Block block : blocks) {
                int hitCode = block.checkHitToBlock(ball.getXBall(), ball.getYBall(), Ball.BALL_RADIUS);
                if (hitCode != Block.NO_HIT) {
                    setScore(getScore() + 1);

                    new GameUIController().show(block.x, block.y, 1, main);

                    block.rect.setVisible(false);
                    block.isDestroyed = true;
                    main.destroyedBlockCount++;
                    ball.resetCollisionStates();

                    if (block.type == Block.BLOCK_CHOCO) {
                        final Bonus choco = new Bonus(block.row, block.column);
                        choco.timeCreated = main.time;
                        Platform.runLater(() -> main.root.getChildren().add(choco.choco));
                        bonuses.add(choco);
                    }

                    if (block.type == Block.BLOCK_STAR) {
                        main.goldTime = main.time;
                        ball.setFill(new ImagePattern(new Image("goldball.png")));
                        main.root.getStyleClass().add("goldRoot");
                        ball.setGoldStatus(true);
                    }

                    if (block.type == Block.BLOCK_HEART) {
                        setHeart(getHeart() + 1);
                    }

                    if (hitCode == Block.HIT_RIGHT) {
                        ball.setCollideToRightBlock(true);
                    } else if (hitCode == Block.HIT_BOTTOM) {
                        ball.setCollideToBottomBlock(true);
                    } else if (hitCode == Block.HIT_LEFT) {
                        ball.setCollideToLeftBlock(true);
                    } else if (hitCode == Block.HIT_TOP) {
                        ball.setCollideToTopBlock(true);
                    }

                }

            }
        }
    }

    @Override
    public void onInit() {
        if (!main.loadFromSave) {
            levelUp(main);
            main.initializeGameElements();
        } else {
            main.initializeGameElements();
            new FileController().loadSavedGameState(main, this, ball, paddle);
            main.loadFromSave = false;
        }
        main.initializeAndStartGameEngine();
        main.setUpGameUI();
        main.scene.setOnKeyPressed(this);
    }

    @Override
    public void performPhysicsCalculations() {
        ball.updateBallMovement(main, this, paddle);

        if (main.time - main.goldTime > 5000) {
            ball.setFill(new ImagePattern(new Image("ball.png")));
            ball.setGoldStatus(false);
        }

        for (Bonus choco : bonuses) {
            if (choco.y > Main.SCENE_HEIGHT || choco.taken) {
                continue;
            }
            if (choco.y >= paddle.getYPaddle() && choco.y <= paddle.getYPaddle() + paddle.getPaddleHeight() && choco.x >= paddle.getXPaddle() && choco.x <= paddle.getXPaddle() + paddle.getPaddleWidth()) {
                choco.taken = true;
                choco.choco.setVisible(false);
                setScore(getScore() + 3);
                new GameUIController().show(choco.x, choco.y, 3, main);
            }
            choco.y += ((main.time - choco.timeCreated) / 1000.000) + 1.000;
        }

    }

    @Override
    public void onTime(long time) {
        main.time = time;
    }

}
