package byow.Core.Graph;

import byow.Core.worldMap.Block;

public class WeightedEdge {
    private Double weight;
    private Block startBlock;
    private Block endBlock;
    private Integer start;
    private Integer end;

    public WeightedEdge(Block b1, Block b2, double weight) {
        startBlock = b1;
        endBlock = b2;
        start = b1.Key();
        end = b2.Key();
        this.weight = weight;
    }
    public Double weight() {
        return weight;
    }
    public Block from() {
        return startBlock;
    }

    public Block to() {
        return endBlock;
    }

    /*
    public static boolean isSame(WeightedEdge a, WeightedEdge b) {
        /*
        if (!a.weight.equals(b.weight)) {
            return false;
        }
         */
    /*
        if (a.from().equals(b.from())) { // (1->2, 0.1) and (1->2, 0.1) are same
            if (a.to().equals(b.to())) {
                return true;
            }
        } else if (a.from().equals(b.to())) { // (1->2, 0.1) and (2->1, 0.1) are same
            if (a.to().equals(b.from())) {
                return true;
            }
        }
        return false;
    }
    */

    @Override
    public String toString() {
        return "(" + start + "," + end + ", " + weight +")";
    }
}
