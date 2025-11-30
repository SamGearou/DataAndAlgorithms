package graphs;


import java.util.*;

public class SSSPUnweightedGraph {

    private HashMap<Integer, List<Integer>> graph;

    public SSSPUnweightedGraph() {
        graph = new HashMap<>();
    }

    // Add a vertex if not present
    private void addVertex(int v) {
        graph.putIfAbsent(v, new ArrayList<>());
    }

    // Add an undirected edge
    public void addEdge(int u, int v) {
        addVertex(u);
        addVertex(v);
        graph.get(u).add(v);
        graph.get(v).add(u);
    }

    // BFS Single-Source Shortest Path
    public void bfsSSSP(int src, int dest) {

        HashMap<Integer, Integer> dist = new HashMap<>();
        HashMap<Integer, Integer> parent = new HashMap<>();

        // Initialize distances
        for (int v : graph.keySet()) {
            dist.put(v, Integer.MAX_VALUE);
            parent.put(v, -1);
        }

        Queue<Integer> q = new LinkedList<>();
        dist.put(src, 0);
        q.add(src);

        // Standard BFS loop
        while (!q.isEmpty()) {
            int currVertex = q.poll();

            for (int neighbor : graph.getOrDefault(currVertex, new ArrayList<>())) {
                if (dist.get(neighbor) == Integer.MAX_VALUE) {
                    dist.put(neighbor, dist.get(currVertex) + 1);
                    parent.put(neighbor, currVertex);
                    q.add(neighbor);
                }
            }
        }

        // Output results
        System.out.println("Distance from " + src + " to " + dest + ": " + dist.get(dest));

        System.out.print("Path: ");
        printPath(src, dest, parent);
        System.out.println();
    }

    // Recursive path reconstruction
    private void printPath(int src, int dest, HashMap<Integer, Integer> parent) {
        if (dest == -1) return;
        if (dest == src) {
            System.out.print(src);
            return;
        }
        printPath(src, parent.get(dest), parent);
        System.out.print(" -> " + dest);
    }

    // Example usage
    public static void main(String[] args) {

        /*
            Example Graph:
                0 -- 1 -- 2
                |    |
                3 -- 4
        */

        SSSPUnweightedGraph g = new SSSPUnweightedGraph();

        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(0, 3);
        g.addEdge(1, 4);
        g.addEdge(3, 4);

        int source = 0;
        int target = 2;

        g.bfsSSSP(source, target);
    }
}
