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
    }

    /**
     * it checks whether {@code adj} has (Edge: {@code a} -> {@code b}) and (Edge: {@code b} -> {@code a})
     * If those edges exist, it returns the list of index.
     * Otherwise, return null.
     * The Index of edge start from {@code a} is 0 th elem of the list
     * The index of edge start grom {@code b} if 1 st elem of the list
     * @param a
     * @param b
     * @return Either the list of index or null
     */
    public List<Integer> hasEdge(int a, int b) {

        return null;
    }
    public void disConnect(Block a, Block b) {
        List<Integer> lst = hasEdge(a.Key(), b.Key());
        if (lst != null ) {
            Integer index_a = lst.indexOf(0);
            Integer index_b = lst.indexOf(1);
            adj.get(a.Key()).remove(index_a);
            adj.get(b.Key()).remove(index_b);
        }
    }







}
