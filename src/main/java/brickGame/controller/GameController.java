package brickGame.controller;
import brickGame.Main;
import brickGame.model.Block;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class GameController {

    private int level;
    private int score;
    private int heart;
//    private final ArrayList<Block> blocks;
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

    // Getter for blocks
//    public ArrayList<Block> getBlocks() {
//        return blocks;
//    }


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

    // Initialize the game board
    public void initBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < getLevel() + 1; j++) {
                int r = new Random().nextInt(500);
                if (r % 5 == 0) {
                    continue; //empty block
                }
                int type;
                if (r % 10 == 1) {
                    type = Block.BLOCK_CHOCO; // choco block
                } else if (r % 10 == 2) {
                    if (!isExistHeartBlock) {
                        type = Block.BLOCK_HEART; // heart block
                        isExistHeartBlock = true;
                    } else {
                        type = Block.BLOCK_NORMAL; // normal block
                    }
                } else if (r % 10 == 3) {
                    type = Block.BLOCK_STAR; // star block
                } else {
                    type = Block.BLOCK_NORMAL; // normal block
                }
//                blocks.add(new Block(j, i, colors[r % (colors.length)], type, false));
            }
        }
    }

//    public void clearBlocks(){
//        blocks.clear();
//    }

//    public int blocksSize(){
//        return blocks.size();
//    }

    // Method to add a block to the blocks ArrayList
//    public void addBlock(int row, int column, int type, boolean isDestroyed) {
//        int r = new Random().nextInt(200); // Example random color selection
//        Block newBlock = new Block(row, column, colors[r % colors.length], type, isDestroyed);
////        blocks.add(newBlock);
//    }
}
