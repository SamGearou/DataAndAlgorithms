package graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class DijkstrasAlgorithm {

    /**
     * Edge class representing: src → dest with a given weight.
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
     * Graph represented as an adjacency list:
     * vertex → list of outgoing edges
     */
    private final HashMap<Integer, List<Edge>> graph;

    public DijkstrasAlgorithm() {
        this.graph = new HashMap<>();
    }

    private void addVertex(int v) {
        graph.putIfAbsent(v, new ArrayList<>());
    }

    /**
     * Adds a directed edge (src → dest) with weight.
     */
    public void addEdge(int src, int dest, long weight) {
        addVertex(src);
        addVertex(dest);
        graph.get(src).add(new Edge(src, dest, weight));
    }

    /**
     * Dijkstra's algorithm: computes the shortest distances from a source vertex.
     * Infinitely loops for a graph with a negative edge weight cycle. Use Bellman-Ford's
     * algorithm to overcome this.
     *
     * Time complexity: O((V + E) log V)
     *   - V = number of vertices
     *   - E = number of edges
     *   - PriorityQueue operations (add/poll) take O(log V)
     * Space complexity: O(V + E)
     */
    public void dijkstra(int source) {

        // dist[v] = shortest known distance from source to v
        HashMap<Integer, Long> dist = new HashMap<>();
        HashMap<Integer, Integer> parent = new HashMap<>();

        // Initialize all distances to infinity
        for (int v : graph.keySet()) {
            dist.put(v, Long.MAX_VALUE);
            parent.put(v, -1);
        }

        // Distance to the source is 0
        dist.put(source, 0L);

        // Min-heap storing pairs (distance, vertex)
        PriorityQueue<long[]> pq = new PriorityQueue<>(
                Comparator.comparingLong(a -> a[0])
        );

        // Push source
        pq.add(new long[]{0L, source});

        while (!pq.isEmpty()) {

            long[] top = pq.poll();
            long d = top[0];
            int u = (int) top[1];

            // Outdated entry check (important)
            if (d > dist.get(u)) {
                continue;
            }

            // Relax all outgoing edges
            for (Edge e : graph.getOrDefault(u, Collections.emptyList())) {
                int v = e.dest;
                long w = e.weight;

                // No need to check dist[u] != INF — Dijkstra guarantees it
                if (dist.get(u) + w < dist.get(v)) {
                    dist.put(v, dist.get(u) + w);
                    parent.put(v, u);
                    pq.add(new long[]{dist.get(v), v});
                }
            }
        }

        // Print results
        System.out.println("Shortest distances from source vertex " + source + ":");
        for (int v : dist.keySet()) {
            long d = dist.get(v);
            if (d == Long.MAX_VALUE) {
                System.out.println(" - to " + v + " = unreachable");
            } else {
                System.out.println(" - to " + v + " = " + d);
            }
        }
    }

    /**
     * Example usage.
     */
    public static void main(String[] args) {
        DijkstrasAlgorithm g = new DijkstrasAlgorithm();

        /*
            Example directed weighted graph:

                0 --4--> 1
                | \
                |  \2
                v   v
                2 --8--> 3
                 \
                  \5
                   v
                   1

            Expected shortest distances from 0:
            - to 1 = 4
            - to 2 = 1
            - to 3 = 2
        */

        g.addEdge(0, 1, 4);
        g.addEdge(0, 2, 1);
        g.addEdge(0, 3, 2);
        g.addEdge(2, 1, 5);
        g.addEdge(2, 3, 8);

        g.dijkstra(0);
    }
}