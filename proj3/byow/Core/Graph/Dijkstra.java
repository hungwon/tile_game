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


        return hallwayIndex;
    }



}
