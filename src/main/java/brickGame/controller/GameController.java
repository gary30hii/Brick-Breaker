package brickGame.controller;
import brickGame.Main;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class GameController {

    private int level;
    private int score;
    private int heart;
//    private ArrayList<Block> blocks;
    private Circle ball;
    private Rectangle paddle;
    private boolean isGoldStatus;
    private boolean isExistHeartBlock;

    public GameController(int level, int score, int heart) {
        this.level = level;
        this.score = score;
        this.heart = heart;
//        blocks = new ArrayList<>();
        // Initialize other game variables and objects
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

    public void setLevel(int level) {
        this.level = level;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public void nextLevel(Main scene){
        setLevel(getLevel() + 1);
        if (getLevel() > 1) {
            new Score().showMessage("Level Up :)", scene);
        }
        if (getLevel() == 18) {
            new Score().showWin(scene);
        }
    }
}
