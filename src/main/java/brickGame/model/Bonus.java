package brickGame.model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.Random;

public class Bonus implements Serializable {
    public Rectangle choco;

    public double x;
    public double y;
    public long timeCreated;
    public boolean taken = false;
    private int type;

    public Bonus(int row, int column, int type) {
        x = (column * (Block.getWidth())) + Block.getPaddingH() + ((double) Block.getWidth() / 2) - 15;
        y = (row * (Block.getHeight())) + Block.getPaddingTop() + ((double) Block.getHeight() / 2) - 15;
        this.type = type;
        draw();
    }

    //    Draws the bonus item, setting its appearance based on a random choice.
    private void draw() {
        choco = new Rectangle();
        choco.setWidth(30);
        choco.setHeight(30);
        choco.setX(x);
        choco.setY(y);

        String url;
        if (type == Block.BLOCK_FOUL){
            url = "bonus3.png";
        } else {
            if (new Random().nextInt(20) % 2 == 0) {
                url = "bonus1.png";
            } else {
                url = "bonus2.png";
            }
        }

        choco.setFill(new ImagePattern(new Image(url)));
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
