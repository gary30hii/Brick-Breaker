package brickGame.model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.Random;

/**
 * The Bonus class represents bonus items in the game. Each bonus item is drawn as a rectangle
 * and can have different types, affecting its appearance and behavior in the game.
 */
public class Bonus implements Serializable {
    public Rectangle choco;

    public double x;
    public double y;
    public long timeCreated;
    public boolean taken = false;
    private final int type;

    /**
     * Constructor for Bonus class. Initializes the bonus item's position based on the given row and column,
     * and sets its type and appearance.
     *
     * @param row The row position for the bonus item.
     * @param column The column position for the bonus item.
     * @param type The type of the bonus item.
     */
    public Bonus(int row, int column, int type) {
        x = (column * (Block.getWidth())) + Block.getPaddingH() + ((double) Block.getWidth() / 2) - 15;
        y = (row * (Block.getHeight())) + Block.getPaddingTop() + ((double) Block.getHeight() / 2) - 15;
        this.type = type;
        draw();
    }

    /**
     * Draws the bonus item, setting its appearance based on the type. The appearance is determined by
     * assigning an image to the rectangle shape of the bonus item.
     */
    private void draw() {
        choco = new Rectangle();
        choco.setWidth(30);
        choco.setHeight(30);
        choco.setX(x);
        choco.setY(y);

        // Determine the image URL based on the type of the bonus.
        String url;
        if (type == Block.BLOCK_FOUL){
            url = "bonus3.png";
        } else if (type == Block.BLOCK_BALL) {
            url = "bonus4.png";
        } else {
            if (new Random().nextInt(20) % 2 == 0) {
                url = "bonus1.png";
            } else {
                url = "bonus2.png";
            }
        }

        choco.setFill(new ImagePattern(new Image(url)));
    }

    /**
     * Retrieves the type of the bonus item.
     *
     * @return The type of the bonus.
     */
    public int getType() {
        return type;
    }
}
