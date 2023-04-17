package byow.Core;

public class Block {
    private String type;
    private Integer weight;
    private int x;
    private int y;
    private int key; // Index of Block[][] world

    /**
     *
     * @param index Index of {@code Block[][] world}.
     * @param x column x of {@code Blcok[][] world}. => x location
     * @param y row y of {@code Block[][] world}. => y location
     * @param type type of block. It will be either null, door, room, wall, hallway, avatar
     */
    public Block(int index, int x, int y, String type) {
        key = index;
        this.x = x;
        this.y = y;
        this.type = type;
        this.weight = null;
    }

    /**
     *
     * @param index Index of {@code Block[][] world}.
     * @param x column x of {@code Blcok[][] world}. => x location
     * @param y row y of {@code Block[][] world}. => y location
     * @param type type of block. It will be either null, door, room, wall, hallway, avatar
     */
    public Block(int index, int x, int y, String type, int weight) {
        key = index;
        this.x = x;
        this.y = y;
        this.type = type;
        this.weight = weight;
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

    public double Weight() {
        return weight;
    }






    /** blockType() returns current block's type.
     * This method is useful when we want to check the type of the current block.
     * */
    private String blockType() {
        return type;
    }


    /** toString() is for debugging purpose. Example String Format -> "Block Type: Floor / Weight: 5" */
    @Override
    public String toString() {
        return "Block Type: " + type + " / " + "Weight: " + weight;
    }
}
