package brickGame;

import java.io.Serializable;

public class BlockSerializable implements Serializable {
    // Block properties
    public final int row;  // Row of the block
    public final int column; // Column of the block (modified 'j' to 'column' for clarity)
    public final int type;  // Type of the blockÏ

    public BlockSerializable(int row , int column , int type) {
        this.row = row;
        this.column = column;
        this.type = type;
    }
}
