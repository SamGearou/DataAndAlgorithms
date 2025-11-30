package graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TopologicalSort {

    // Tracks nodes that have already been visited in the DFS
    HashSet<Integer> visited = new HashSet<>();

    // Stores the resulting topological order (in reverse)
    ArrayList<Integer> topoSort = new ArrayList<>();

    /**
     * Performs a DFS from the given vertex and records nodes
     * in topological order. Nodes are added AFTER exploring all neighbors,
     * meaning the result list is in reverse topological order.
     *
     * @param vertex starting node for DFS
     * @param graph  adjacency list representation of the graph
     */
    public void dfs(int vertex, HashMap<Integer, List<Integer>> graph) {
        visited.add(vertex);

        // Explore all outgoing edges
        for (int neighbor : graph.getOrDefault(vertex, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, graph);
            }
        }

        // Post-order insertion: add after visiting all children
        topoSort.add(vertex);
    }

    public static void main(String[] args) {
        TopologicalSort topo = new TopologicalSort();

        // -----------------------------------------------------
        // Example DAG (Directed Acyclic Graph)
        //
        // Graph structure:
        //    5 → 2 → 3 → 1
        //    ↓
        //    0
        //    4 → 1
        //
        // A valid topological order: [5, 4, 2, 3, 1, 0]
        // -----------------------------------------------------
        HashMap<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(5, List.of(2, 0));
        graph.put(4, List.of(1));
        graph.put(2, List.of(3));
        graph.put(3, List.of(1));
        graph.put(1, new ArrayList<>());
        graph.put(0, new ArrayList<>());

        // Run DFS-based topological sort from all unvisited nodes
        for (int node : graph.keySet()) {
            if (!topo.visited.contains(node)) {
                topo.dfs(node, graph);
            }
        }

        // Reverse the final list to get correct topological ordering
        ArrayList<Integer> ordering = new ArrayList<>();
        for (int i = topo.topoSort.size() - 1; i >= 0; i--) {
            ordering.add(topo.topoSort.get(i));
        }

        System.out.println("Topological Order: " + ordering);
    }
}