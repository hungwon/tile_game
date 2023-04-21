package byow.Core.Graph;

import byow.Core.worldMap.Block;
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
            adj.put(i, new ArrayList<>());
        }
    }

    public boolean isIsolated(int i) {
        return adj(i).size() == 0;
    }

    public List<WeightedEdge> adj(int i) {
        return adj.get(i);
    }

    public List<WeightedEdge> adj(Block b) {
        return adj.get(b.key());
    }

    public void addEdge(Block s, Block t, double w) {
        adj.get(s.key()).add(new WeightedEdge(s, t, w));
        adj.get(t.key()).add(new WeightedEdge(t, s, w));
        e++;
    }

    public boolean isConnected(int a, int b) {
        if (isIsolated(a) || isIsolated(b)) {
            return false;
        }
        for (WeightedEdge edgeE: adj(a)) {
            if (edgeE.to().key() == b) {
                return true;
            }
        }
        return false;
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
        List<Integer> lst = new ArrayList<>();
        int i = 0;
        for (WeightedEdge edgeE: adj(a)) {
            if (edgeE.to().key() == b) {
                lst.add(i);
            }
            i++;
        }
        i = 0;
        for (WeightedEdge edgeE: adj(b)) {
            if (edgeE.to().key() == a) {
                lst.add(i);
            }
            i++;
        }
        if (lst.size() == 0) {
            lst = null;
        }
        return lst;
    }

    public void disconnect(Block a, Block b) {

        if (isConnected(a.key(), b.key())) {
            List<Integer> lst = hasEdge(a.key(), b.key());
            if (lst != null) {
                int indexA = lst.get(0);
                int indexB = lst.get(1);
                adj(a).remove(indexA);
                adj(b).remove(indexB);
                e--;
            }
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
