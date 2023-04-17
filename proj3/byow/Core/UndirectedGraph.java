package byow.Core;

import java.util.*;

public class UndirectedGraph {
    private int v;
    private int e;
    private Map<Integer, List<WeightedEdge>> adj;

    public UndirectedGraph(int size) {
        v = size;
        e = 0;
        adj = new HashMap<>();
        for (int i = 0; i < size; i++) {
            adj.put(i, new LinkedList<>());
        }
    }

    public void addEdge(int s, int t, double w) {
        adj.get(s).add(new WeightedEdge(s, t, w));
        adj.get(t).add(new WeightedEdge(t, s, w));
        e++;
    }

    /**
     * <h4>
     *     If {@code adj} contains (Edge: {@code a} -> {@code b}) and (Edge: {@code b} -> {@code a}),
     *     it returns the list of index. Otherwise, return null.
     * </h4>
     * <p>
     *     List's 0th elem = The Index of WeightedEdge starting from {@code a}
     * </p>
     * <p>
     *     List's 1st elem = The Index of WeightedEdge starting from {@code b}
     * </p>
     * @return Either the list of index or null
     */
    public List<Integer> hasEdge(int a, int b) {

        return null;
    }

    /**
     * If there are (Edge: {@code a} -> {@code b}) or (Edge: {@code b} -> {@code a}) in {@code adj},
     * remove those Edges in {@code adj}.
     * @param a
     * @param b
     */
    public void disconnect(Block a, Block b) {
        List<Integer> lst = hasEdge(a.Key(), b.Key());
        if (lst != null ) {
            Integer index_a = lst.indexOf(0);
            Integer index_b = lst.indexOf(1);
            adj.get(a.Key()).remove(index_a);
            adj.get(b.Key()).remove(index_b);
            e--;
        }
    }

    /**
     *
     * @return the number of vertices
     */
    public int V() {
        return v;
    }

    /**
     *
     * @return the number of edges
     */
    public int E() {
        return e;
    }
}
