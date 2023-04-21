package byow.Core.Graph;

import byow.Core.worldMap.Block;
import edu.princeton.cs.algs4.IndexMinPQ;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Dijkstra {
    Double[] distTo;
    Integer[] edgeTo;
    IndexMinPQ<Double> fringe;
    UndirectedGraph graph;
    List<Integer> lst;
    public static final double MAXDOUBLE = 1000000.;
    public static final int MAXINDEX = 2400;

    public Dijkstra(UndirectedGraph g) {
        graph = g;
    }

    public List<Integer> findPath(int s, int t) {

        distTo = new Double[graph.V()];
        Arrays.fill(distTo, MAXDOUBLE);
        edgeTo = new Integer[graph.V()];
        fringe = new IndexMinPQ<>(MAXINDEX);
        lst = new ArrayList<>();

        List<Integer> hallwayIndex = new LinkedList<>();
        distTo[s] = 0.;
        fringe.insert(s, 0.);
        for (int i = 0; i < MAXINDEX; i++) {
            if (i != s) {
                fringe.insert(i, MAXDOUBLE);
            }
        }
        int p;
        while (!fringe.isEmpty() && edgeTo[t] == null) {
            p = fringe.delMin();
            relax(p);
        }

        int x = t;
        while (x != s) {
            hallwayIndex.add(x);
            x = edgeTo[x];
        }
        hallwayIndex.add(x);

        return hallwayIndex;
    }

    public void relax(int index) {
        for (WeightedEdge e: graph.adj(index)) {
            Block p = e.from();
            int pIndex = p.key();
            Block q = e.to();
            int qIndex = q.key();

            if (distTo[pIndex] + e.weight() < distTo[qIndex] && isPossible(q)) {
                distTo[qIndex] = distTo[pIndex] + e.weight();
                edgeTo[qIndex] = pIndex;
                fringe.changeKey(qIndex, distTo[qIndex]);
            }
        }
    }

    /**
     * is q can be a path?
     * @param b
     * @return
     */
    public boolean isPossible(Block b) {

        if (b.isRoom() || b.isMargin()) {
            lst.add(b.key());
            return false;
        }
        return true;


    }
}
