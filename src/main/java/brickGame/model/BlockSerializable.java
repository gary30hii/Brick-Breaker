package brickGame.model;

import java.io.Serializable;

/**
 * The BlockSerializable class is a serializable version of a block used in the game.
 * It is designed to store the essential properties of a block, making it suitable for serialization.
 */
public class BlockSerializable implements Serializable {
    // Block properties
    public final int row;  // Row of the block
    public final int column; // Column of the block (modified 'j' to 'column' for clarity)
    public final int type;  // Type of the blockÏ
    public final boolean isDestroyed; // Identify weather the block has been destroyed


    /**
     * Constructor for creating a serializable block with specified properties.
     *
     * @param row The row position of the block.
     * @param column The column position of the block.
     * @param type The type of the block.
     * @param isDestroyed Flag indicating whether the block is destroyed.
     */
    public BlockSerializable(int row, int column, int type, boolean isDestroyed) {
        this.row = row;
        this.column = column;
        this.type = type;
        this.isDestroyed = isDestroyed;
    }
}
