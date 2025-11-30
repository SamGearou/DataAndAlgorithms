package graphs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Bipartite {

    /**
     * Checks if a graph is bipartite starting from a given source vertex.
     * Uses BFS coloring: each node is assigned a color 0 or 1.
     * If two adjacent nodes ever have the same color → graph is NOT bipartite.
     *
     * @param srcVertex the starting vertex for BFS
     * @param graph adjacency list stored in a HashMap
     * @return true if bipartite, false otherwise
     */
    public boolean isGraphBipartite(int srcVertex, HashMap<Integer, List<Integer>> graph) {

        // Color array: -1 means uncolored; 0 and 1 are the two groups
        int[] colors = new int[graph.size()];
        Arrays.fill(colors, -1);

        boolean isBipartite = true;
        LinkedList<Integer> queue = new LinkedList<>();

        // Start BFS by coloring the source vertex
        colors[srcVertex] = 1;
        queue.add(srcVertex);

        while (!queue.isEmpty() && isBipartite) {
            int currVertex = queue.poll();

            // Traverse all neighbors
            for (int neighbor : graph.get(currVertex)) {

                // If neighbor is uncolored, color it with opposite color
                if (colors[neighbor] == -1) {
                    colors[neighbor] = 1 - colors[currVertex];
                    queue.add(neighbor);

                    // If neighbor has the same color → NOT bipartite
                } else if (colors[neighbor] == colors[currVertex]) {
                    isBipartite = false;
                    break;
                }
            }
        }

        return isBipartite;
    }

    public static void main(String[] args) {
        Bipartite bipartite = new Bipartite();

        // -----------------------------
        // Build example bipartite graph
        //
        // 0 -- 1
        // |    |
        // 3 -- 2
        //
        // This is a cycle of length 4 → bipartite
        // -----------------------------
        HashMap<Integer, List<Integer>> graph1 = new HashMap<>();
        graph1.put(0, List.of(1, 3));
        graph1.put(1, List.of(0, 2));
        graph1.put(2, List.of(1, 3));
        graph1.put(3, List.of(0, 2));

        boolean result1 = bipartite.isGraphBipartite(0, graph1);
        System.out.println("Graph 1 bipartite? " + result1); // true

        // -----------------------------
        // Build NON-bipartite graph
        //
        // Triangle (odd cycle):
        // 0 -- 1
        //  \  /
        //    2
        //
        // All cycles of odd length are NON-bipartite
        // -----------------------------
        HashMap<Integer, List<Integer>> graph2 = new HashMap<>();
        graph2.put(0, List.of(1, 2));
        graph2.put(1, List.of(0, 2));
        graph2.put(2, List.of(0, 1));

        boolean result2 = bipartite.isGraphBipartite(0, graph2);
        System.out.println("Graph 2 bipartite? " + result2); // false
    }
}