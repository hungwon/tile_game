package byow.Core.Graph;

import byow.Core.worldMap.Block;
import edu.princeton.cs.algs4.IndexMinPQ;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Dijkstra {
    Double[] distTo;
    Integer[] edgeTo;
    IndexMinPQ<Double> fringe;
    UndirectedGraph graph;

    public static final double MAXDOUBLE = 1000000.;

    public static final int MAXINDEX = 2400;


    public Dijkstra(UndirectedGraph g) {
        graph = g;

    }

    public List<Integer> findPath(int s, int t) {
        //System.out.println("findPath: " + "s: " + s);

        distTo = new Double[graph.V()];
        Arrays.fill(distTo, MAXDOUBLE);
        edgeTo = new Integer[graph.V()];
        fringe = new IndexMinPQ<>(MAXINDEX);

        List<Integer> hallwayIndex = new LinkedList<>();
        distTo[s] = 0.;
        for (int i = 0; i < MAXINDEX; i++) {
            if (i != s) {
                fringe.insert(i, MAXDOUBLE);
            }
        }
        int p = MAXINDEX;
        while (!fringe.isEmpty() && edgeTo[t] == null) {
            if (p == MAXINDEX) {
                p = s;
            } else {
                p = fringe.delMin();
            }
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

        //System.out.println("relax started with index = " + index);
        //System.out.println(graph.adj(index).toString());

        for (WeightedEdge e: graph.adj(index)) {
            //System.out.println(edgeTo[e.to().key()] == null);
            if (edgeTo[e.to().key()] != null) {
                //System.out.println("so Skip");
                continue;
            }

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

        //System.out.println("relax end");
    }

    /**
     * is q can be a path?
     * @param b
     * @return
     */
    public boolean isPossible(Block b) {

        /*
        for (WeightedEdge edge: graph.adj(b)) {
            Block next = edge.to();
            if (B)
        }
        */

        if (b.isDoor() && b.isMargin()) {
            return false;
        }
        return true;


    }
}
