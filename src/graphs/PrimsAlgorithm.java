package graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class PrimsAlgorithm {

    // Edge class representing an edge between two vertices u and v with weight w
    static class Edge {
        int u, v, w;

        Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }

    private int V; // number of vertices
    private HashMap<Integer, List<Edge>> graph; // adjacency list representation
    private boolean[] taken; // keeps track of included vertices
    private PriorityQueue<int[]> pq; // stores edges as {weight, vertex}

    /**
     * Constructor to initialize graph and MST structures
     *
     * @param V number of vertices
     */
    public PrimsAlgorithm(int V) {
        this.V = V;
        graph = new HashMap<>();
        taken = new boolean[V];
        pq = new PriorityQueue<>((a, b) -> a[0] - b[0]); // min-heap by weight

        // Initialize adjacency list for each vertex
        for (int i = 0; i < V; i++) {
            graph.put(i, new ArrayList<>());
        }
    }

    /**
     * Adds an undirected edge to the graph
     */
    public void addEdge(int u, int v, int w) {
        graph.get(u).add(new Edge(u, v, w));
        graph.get(v).add(new Edge(v, u, w)); // undirected
    }

    /**
     * Marks a vertex as included in the MST and adds all incident edges to the priority queue
     */
    private void process(int vtx) {
        taken[vtx] = true;
        for (Edge e : graph.get(vtx)) {
            int other = (e.u == vtx) ? e.v : e.u;
            if (!taken[other]) {
                pq.offer(new int[]{e.w, other});
            }
        }
    }

    /**
     * Computes MST cost using Prim's algorithm starting from vertex 0
     *
     * @return MST cost
     */
    public int prim() {
        int mstCost = 0;
        process(0); // start from vertex 0

        while (!pq.isEmpty()) {
            int[] front = pq.poll();
            int w = front[0];
            int u = front[1];

            if (!taken[u]) {
                mstCost += w;
                process(u);
            } else {
                // Edge leads to a vertex already included in MST, would form a cycle
                System.out.println("Skipping edge leading to vertex " + u + " with weight " + w + " to avoid cycle.");
            }
        }

        return mstCost;
    }

    public static void main(String[] args) {
        // Create graph with 4 vertices
        PrimsAlgorithm primGraph = new PrimsAlgorithm(4);

        // Add edges
        primGraph.addEdge(0, 1, 10);
        primGraph.addEdge(0, 2, 6);
        primGraph.addEdge(0, 3, 5);
        primGraph.addEdge(1, 3, 15);
        primGraph.addEdge(2, 3, 4);

        /*
          Original Graph (tree-style diagram for visualization, rooted at 0):

                0
              / | \
           (10)(6)(5)
            1   2   3
                 \  |
                (4)(15)

          Minimum Spanning Tree (MST) tree diagram selected by Prim's algorithm:

                0
               / \
             (5)  (6)
             3     2
             |
            (10)
             1

          Explanation of MST edges (weights in parentheses):
          - 0 — 3 (5)
          - 3 — 2 (4)
          - 0 — 1 (10)

          MST cost = 5 + 4 + 10 = 19
        */

        int mstCost = primGraph.prim();
        System.out.println("MST cost = " + mstCost); // Expected output: 19
    }
}