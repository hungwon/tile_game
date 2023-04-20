package byow.Core.worldMap;

public class Block {
    private String type;
    private int x;
    private int y;
    private int key; // Index of Block[][] world

    public static final int MAXINDEXFORY = 29;
    public static final int MAXINDEXFORX = 2399;

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

    public boolean isMargin() {
        if (x == 0 || x == MAXINDEXFORX) {
            return true;
        }
        if (y == 0 || y == MAXINDEXFORY) {
            return true;
        }

        return false;
    }

    public Integer key() {
        return key;
    }

    public int X() {
        return x;
    }

    public int Y() {
        return y;
    }

    public void changeType(String tyPe) {
        this.type = tyPe;
    }

    /**
     * <h2>
     *     Re-initialize current block's {@code type}.
     * </h2>
     * <h2>
     *     {@code type} of this Block
     * </h2>
     * @return {@code type} of {@code this} block
     */
    public String blockType() {
        return type;
    } // changed to public

    public boolean isNull() {
        if (type == null) {
            return true;
        }
        return false;
    }

    public boolean isDoor() {
        if (type == null) {
            return false;
        }

        if (type.equals("door")) {
            return true;
        }
        return false;
    }

    public boolean isRoom() {
        if (type == null) {
            return false;
        }
        if (type.equals("room")) {
            return true;
        }
        return false;
    }

    public boolean isWall() {
        if (type == null) {
            return false;
        }
        if (type.equals("wall")) {
            return true;
        }
        return false;
    }

    public boolean isHallway() {
        if (type == null) {
            return false;
        }
        if (type.equals("hallway")) {
            return true;
        }
        return false;
    }


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
