package byow.Core.worldMap;

public class Block {
    private String type;
    private int x;
    private int y;
    private int key; // Index of Block[][] world

    /**
     *
     * @param index Index of {@code Block[][] world}.
     * @param x x th column of {@code Blcok[][] world}.
     * @param y y th row of {@code Block[][] world}.
     * @param type type of block. It will be either null, door, room, wall, hallway, avatar
     */
    public Block(int index, int x, int y, String type) {
        key = index;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int Key() {
        return key;
    }

    public int X() {
        return x;
    }

    public int Y() {
        return y;
    }


    /**
     * <h2>
     *     Re-initialize current block's {@code type}.
     * </h2>
     */

    public void changeType(String newType) { type = newType; }

    /**
     * <h2>
     *     {@code type} of this Block
     * </h2>
     * @return {@code type} of {@code this} block
     */
    public String blockType() {
        return type;
    } // changed to public


    /**
     * <p>
     *     Ex) "(Key: 10, Type: Floor)"
     * </p>
     * @return String ver of {@code this} block
     */
    @Override
    public String toString() {
        return "(Key: " + key + ", Type: " + type + ")";
    }
}
