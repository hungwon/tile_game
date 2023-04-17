package byow.Core;


import java.util.Random;

public class Block {

    private String type;
    private int weight;
    private Random rand;




    /** The Block constructor has a "type" parameter which is a string
     * that states current block's type. It also instantiates the Random
     * class that will be used to define current block's weight.
     * The weight will be any random variable from between [1, 100].*/
    public Block(String type) {

        rand = new Random();

        this.type = type;
        this.weight = 1 + rand.nextInt(100); // 1 + random number between 0 and 99 (inclusive)
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
