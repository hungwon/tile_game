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

    @Override
    public String toString() {
        return "(" + start + "," + end + ", " + weight +")";
    }
}
