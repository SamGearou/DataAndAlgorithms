package graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import unionfind.UnionFind; // Using your existing UnionFind class

public class KruskalsAlgorithm {

    // Edge class representing an edge between two vertices u and v with weight w
    static class Edge implements Comparable<Edge> {
        int u, v, w;

        Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }

        // Compare edges based on weight (safe for large integers)
        public int compareTo(Edge other) {
            return Integer.compare(this.w, other.w);
        }
    }

    // Utility method to add undirected edge to the graph
    static void addEdge(HashMap<Integer, List<Edge>> graph, int u, int v, int w) {
        graph.get(u).add(new Edge(u, v, w));
        graph.get(v).add(new Edge(v, u, w)); // undirected graph
    }

    /**
     * Kruskal's algorithm to compute MST cost.
     *
     * @param V     Number of vertices
     * @param graph Graph represented as HashMap<Integer, List<Edge>>
     * @return Minimum Spanning Tree cost
     */
    public static int kruskal(int V, HashMap<Integer, List<Edge>> graph) {
        List<Edge> edgeList = new ArrayList<>();

        // Flatten the graph HashMap into a single list of edges
        for (List<Edge> edges : graph.values()) {
            edgeList.addAll(edges);
        }

        // Sort edges by weight in ascending order
        Collections.sort(edgeList);

        int mstCost = 0;
        UnionFind uf = new UnionFind(V); // use existing UnionFind class

        // Iterate over edges in increasing order of weight
        for (Edge edge : edgeList) {
            // Include edge if endpoints are not already in the same set
            if (!uf.isSameSet(edge.u, edge.v)) {
                mstCost += edge.w;
                uf.union(edge.u, edge.v); // union sets
            } else {
                // Edge would create a cycle
                System.out.println("Skipping edge (" + edge.u + " - " + edge.v + ") with weight "
                        + edge.w + " to avoid cycle.");
            }
        }

        return mstCost;
    }

    public static void main(String[] args) {
        int V = 4; // Number of vertices
        HashMap<Integer, List<Edge>> graph = new HashMap<>();

        // Initialize adjacency list for each vertex
        for (int i = 0; i < V; i++) graph.put(i, new ArrayList<>());

        // Add edges to the graph (undirected)
        addEdge(graph, 0, 1, 10);
        addEdge(graph, 0, 2, 6);
        addEdge(graph, 0, 3, 5);
        addEdge(graph, 1, 3, 15);
        addEdge(graph, 2, 3, 4);

        /*
          Original Graph:
                0 -----------
                |     |     |
           (10) |     | (5) |
                |     |     |
                1 --- 3     | (6)
                  (15)|     |
                      | (4) |
                      |     |
                      2------


          Minimum Spanning Tree (MST) tree diagram:

                0
               / \
             (10) (5)
             1     3
                  /
               (4)
              2

          Explanation of MST edges (weights in parentheses):
          - 0 — 1 (10)
          - 0 — 3 (5)
          - 3 — 2 (4)

          MST cost = 10 + 5 + 4 = 19
        */

        // Compute MST cost using Kruskal's algorithm
        int mstCost = kruskal(V, graph);
        System.out.println("MST cost = " + mstCost); // Expected output: MST cost = 19
    }
}