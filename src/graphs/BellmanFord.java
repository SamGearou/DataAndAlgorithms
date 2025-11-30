package graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BellmanFord {

    /**
     * Edge class representing: src -> dest with a weight
     */
    static class Edge {
        int src;
        int dest;
        long weight;

        Edge(int src, int dest, long weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }
    }

    /**
     * Graph represented as adjacency list: vertex -> list of outgoing edges
     */
    private final HashMap<Integer, List<Edge>> graph;

    public BellmanFord() {
        this.graph = new HashMap<>();
    }

    private void addVertex(int v) {
        graph.putIfAbsent(v, new ArrayList<>());
    }

    /**
     * Add directed edge src -> dest with weight
     */
    public void addEdge(int src, int dest, long weight) {
        addVertex(src);
        addVertex(dest);
        graph.get(src).add(new Edge(src, dest, weight));
    }

    /**
     * Runs Bellman-Ford algorithm from a source vertex
     * @param source the starting vertex
     * @return array of distances, or null if a negative cycle is detected
     *
     * Time complexity: O(V * E)
     *   - We relax all edges V-1 times → O(V * E)
     *   - Checking for negative cycles → O(E)
     * Space complexity: O(V + E)
     */
    public long[] bellmanFord(int source) {
        int V = graph.size();
        long INF = Long.MAX_VALUE;
        long[] dist = new long[V];

        // Initialize all distances to infinity
        for (int i = 0; i < V; i++) {
            dist[i] = INF;
        }
        dist[source] = 0;

        // Relax all edges V-1 times
        for (int i = 0; i < V - 1; i++) {
            for (int u : graph.keySet()) {
                for (Edge e : graph.get(u)) {
                    int v = e.dest;
                    long w = e.weight;
                    if (dist[u] != INF && dist[u] + w < dist[v]) {
                        dist[v] = dist[u] + w;
                    }
                }
            }
        }

        // Check for negative weight cycles
        for (int u : graph.keySet()) {
            for (Edge e : graph.get(u)) {
                int v = e.dest;
                long w = e.weight;
                if (dist[u] != INF && dist[u] + w < dist[v]) {
                    System.out.println("Graph contains a negative weight cycle");
                    return null;
                }
            }
        }

        return dist;
    }

    public static void main(String[] args) {
        BellmanFord g = new BellmanFord();

        /*
            Example graph:

                Vertex 0:
                    0 -> 1 weight 4
                    0 -> 2 weight 5
                Vertex 1:
                    1 -> 2 weight -3
                    1 -> 3 weight 2
                Vertex 2:
                    2 -> 3 weight 4
         */

        g.addEdge(0, 1, 4);
        g.addEdge(0, 2, 5);
        g.addEdge(1, 2, -3);
        g.addEdge(1, 3, 2);
        g.addEdge(2, 3, 4);

        long[] distances = g.bellmanFord(0);

        if (distances != null) {
            System.out.println("Shortest distances from source vertex 0:");
            for (int i = 0; i < distances.length; i++) {
                if (distances[i] == Long.MAX_VALUE) {
                    System.out.println(" - to " + i + " = unreachable");
                } else {
                    System.out.println(" - to " + i + " = " + distances[i]);
                }
            }
        }
    }
}
