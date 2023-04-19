package byow.Core.Graph;

import byow.Core.worldMap.Block;
import edu.princeton.cs.algs4.In;
import org.apache.commons.collections.list.TreeList;

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

    public List<WeightedEdge> adj(int i) {
        return adj.get(i);
    }

    public void addEdge(int s, int t, double w) {
        adj.get(s).add(new WeightedEdge(s, t, w));
        adj.get(t).add(new WeightedEdge(t, s, w));
        e++;

    }

    public boolean isConnected(int a, int b) {
        for (WeightedEdge e: adj(a)) {
            //System.out.println(adj(a) + "," + a + ", "+ b + ", " + e.to().equals(b));
            if (e.to().equals(b)) {
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
        for (Integer i: adj.keySet()) {
            List<WeightedEdge> adjOfI = adj.get(i);

            if (i.equals(a)) {
                for (int index = 0; index < adjOfI.size(); index++) {
                    if (adjOfI.get(index).equals(b)) {
                        lst.add(index);
                    }
                }
            } else if (i.equals(b)) {
               for (int index = 0; index< adjOfI.size(); index++) {
                   if (adjOfI.get(index).equals(a)) {
                       lst.add(index);
                   }
               }
            }
        }

        if (lst.size() == 0) {
            lst = null;
        }
        return lst;
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
