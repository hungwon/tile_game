package byow.Core.Graph;

import byow.Core.worldMap.World;
import edu.princeton.cs.algs4.IndexMinPQ;
import byow.Core.worldMap.World.*;
import java.util.LinkedList;
import java.util.List;

public class Dijkstra {
    Double[] distTo;
    Integer[] edgeTo;
    IndexMinPQ<Double> fringe;
    UndirectedGraph graph;

    public Dijkstra(UndirectedGraph g ) {
        graph = g;
        distTo = new Double[g.V()];
        edgeTo = new Integer[g.V()];
        fringe = new IndexMinPQ<>(2399);
    }

    public List<Integer> findPath(int s, int t) {
        List<Integer> hallwayIndex = new LinkedList<>();
        fringe.insert(s, 0.);
        distTo[s] = 0.;
        for (int i = 0; i < 2399; i++) {
            if (i != s) {
                fringe.insert(i, 100.);
            }
        }
        while (!fringe.isEmpty()) {
            int p = fringe.delMin();
            relax(p);
        }



        return hallwayIndex;
    }
    public void relax(int index) {
        for (WeightedEdge e: graph.adj(index)) {
            int p = e.from();
            int q = e.to();
            if (distTo[p] + e.weight() < distTo[q]) {
                distTo[q] = distTo[p] + e.weight();
                edgeTo[q] = p;
                fringe.changeKey(q, distTo[q]);
            }
        }
    }
    public boolean isPossible(int index) {
        return true;
    }

}
