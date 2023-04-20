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
            adj.put(i, new LinkedList<>());
        }
    }

    public boolean isIsolated (int i) {
        if (adj(i).size() == 0) {
            return true;
        } else {
            return false;
        }
    }
    public List<WeightedEdge> adj(int i) {
        return adj.get(i);
    }

    public List<WeightedEdge> adj(Block b) {
        return adj.get(b.Key());
    }
    public void addEdge(Block s, Block t, double w) {
        adj.get(s.Key()).add(new WeightedEdge(s, t, w));
        adj.get(t.Key()).add(new WeightedEdge(t, s, w));
        e++;

    }

    public boolean isConnected(int a, int b) {

        //System.out.println("ISCONNECTED:" + " " + a + " " + b);
        for (WeightedEdge e: adj(a)) {
            //System.out.println( a + ", "+ b + ", " + e.to().equals(b));

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
        for (WeightedEdge e: adj(a)) {
            for (int i = 0; i < adj(a).size(); i++) {
                if (e.to().equals(b)) {
                    lst.add(i);
                }
            }
        }
        for (WeightedEdge e: adj(b)) {
            for (int i = 0; i < adj(b).size(); i++) {
                if (e.to().equals(a)) {
                    lst.add(i);
                }
            }
        }

        if (lst.size() == 0) {
            lst = null;
        }
        return lst;
    }

    public void disconnect(Block a, Block b) {

        if (isConnected(a.Key(), b.Key())) {
            List<Integer> lst = hasEdge(a.Key(), b.Key());
            System.out.println("disconnect called");
            System.out.println("follwoing index will be removed: " + lst.get(0) + ", " + lst.get(1));
            if (lst != null) {
                Integer index_a = lst.indexOf(0);
                Integer index_b = lst.indexOf(1);

                System.out.println("adj(a) : " + adj(a));
                System.out.println("adj(b): " + adj(b));
                adj(a).remove(index_a);
                adj(b).remove(index_b);
                e--;
                System.out.println("adj(a) : " + adj(a));
                System.out.println("adj(b): " + adj(b));
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
