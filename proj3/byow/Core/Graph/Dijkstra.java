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


    public Dijkstra(UndirectedGraph g ) {
        graph = g;
        distTo = new Double[g.V()];
        Arrays.fill(distTo, 10000.);
        edgeTo = new Integer[g.V()];
        fringe = new IndexMinPQ<>(2399);
    }

    public List<Integer> findPath(int s, int t) {
        List<Integer> hallwayIndex = new LinkedList<>();
        fringe.insert(s, 0.);
        distTo[s] = 0.;
        for (int i = 0; i < 2399; i++) {
            if (i != s) {
                fringe.insert(i, 10000.);
            }
        }
        while (!fringe.isEmpty() && edgeTo[t] == null) {
            int p = fringe.delMin();
            relax(p);
        }

        int x = t;
        while (x != s) {
            System.out.println(edgeTo[x]);
            hallwayIndex.add(x);
            x = edgeTo[x];
        }
        hallwayIndex.add(x);

        return hallwayIndex;
    }
    public void relax(int index) {
        for (WeightedEdge e: graph.adj(index)) {
            Block p = e.from();
            int pIndex = p.Key();
            Block q = e.to();
            int qIndex = q.Key();
            if (distTo[pIndex] + e.weight() < distTo[qIndex] && isPossible(p,q) ) {
                distTo[qIndex] = distTo[pIndex] + e.weight();
                edgeTo[qIndex] = pIndex;
                fringe.changeKey(qIndex, distTo[qIndex]);
            }
        }
    }

    /**
     * is q can be a path?
     * @param end
     * @return
     */
    public boolean isPossible (Block start, Block end) {
        for (WeightedEdge e: graph.adj(end)) {
            Block prev = e.to();
            Block next = e.from();
            if (!prev.Key().equals(start.Key())) {
                if (!next.isNull() || !next.isWall() ) {
                    return false;
                }
            }
        }
        return true;
    }

}
