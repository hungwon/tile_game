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

    public final static double MAXDOUBLE = 10000;

    public final static int MAXINDEX = 2400;


    public Dijkstra(UndirectedGraph g) {
        graph = g;
        distTo = new Double[g.V()];
        Arrays.fill(distTo, MAXDOUBLE);
        edgeTo = new Integer[g.V()];
    }

    public List<Integer> findPath(int s, int t) {

        System.out.println("findPath called");
        fringe = new IndexMinPQ<>(MAXINDEX);
        List<Integer> hallwayIndex = new LinkedList<>();
        fringe.insert(s, 0.);
        distTo[s] = 0.;

        fringe = new IndexMinPQ<>(MAXINDEX);

        for (int i = 0; i < MAXINDEX; i++) {
            if (i != s) {
                fringe.insert(i, MAXDOUBLE);
            }
        }


        while (!fringe.isEmpty()) { //) && edgeTo[t] == null ) {
            int p = fringe.delMin();
            relax(p);
        }


        int x = t;
        while (x != s) {
            hallwayIndex.add(x);
            x = edgeTo[x];
        }
        hallwayIndex.add(x);
        System.out.println("findPath end");

        return hallwayIndex;
    }

    public void relax(int index) {
        System.out.println("relax Start: ");
        for (WeightedEdge e: graph.adj(index)) {

            //역류방지
            if (edgeTo[e.to().Key()] != null) {
                System.out.println("skip" + e);
                continue;
            }

            Block p = e.from();
            int pIndex = p.Key();

            Block q = e.to();
            int qIndex = q.Key();


            System.out.println(pIndex + " -> " + qIndex);

            if (distTo[pIndex] + e.weight() < distTo[qIndex] && isPossible(p, q)){
                distTo[qIndex] = distTo[pIndex] + e.weight();
                edgeTo[qIndex] = pIndex;
                System.out.println(fringe.minIndex());
                fringe.changeKey(qIndex, distTo[qIndex]);
            }
        }
        System.out.println("releax end");
    }

    /**
     * is q can be a path?
     * @param end
     * @return
     */

    public boolean isPossible(Block start, Block end) {
        for (WeightedEdge e: graph.adj(end)) {
            Block prev = e.to();
            Block next = e.from();
            if (!prev.Key().equals(start.Key()) ) {
                if (next.isRoom()) {
                    return false;
                }
            }
        }
        return true;
    }
}
