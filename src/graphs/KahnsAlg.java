package graphs;

import java.util.*;

public class KahnsAlg {

    /**
     * Performs topological sort using Kahn's Algorithm on a graph represented
     * as a HashMap of adjacency lists.
     *
     * @param graph A map where each key is a node and its value is the list of outgoing neighbors.
     * @return A list with the topological order, or an empty list if a cycle exists.
     */
    public static List<Integer> topologicalSort(HashMap<Integer, List<Integer>> graph) {
        // Build indegree map
        HashMap<Integer, Integer> indegree = new HashMap<>();

        // Initialize indegree for all nodes
        for (Integer node : graph.keySet()) {
            indegree.putIfAbsent(node, 0);
            for (Integer neighbor : graph.get(node)) {
                indegree.put(neighbor, indegree.getOrDefault(neighbor, 0) + 1);
            }
        }

        // Queue for nodes with indegree = 0
        Queue<Integer> queue = new LinkedList<>();
        for (Integer node : indegree.keySet()) {
            if (indegree.get(node) == 0) {
                queue.add(node);
            }
        }

        List<Integer> topoOrder = new ArrayList<>();

        // Process nodes with indegree 0
        while (!queue.isEmpty()) {
            int node = queue.poll();
            topoOrder.add(node);

            // Reduce indegree of neighbors
            for (Integer neighbor : graph.getOrDefault(node, Collections.emptyList())) {
                indegree.put(neighbor, indegree.get(neighbor) - 1);
                if (indegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        // If topoOrder doesn't include all nodes → cycle exists
        if (topoOrder.size() != indegree.size()) {
            return new ArrayList<>(); // return empty
        }

        return topoOrder;
    }


    // Example usage
    public static void main(String[] args) {
        HashMap<Integer, List<Integer>> graph = new HashMap<>();

        graph.put(5, Arrays.asList(2, 0));
        graph.put(4, Arrays.asList(0, 1));
        graph.put(2, Arrays.asList(3));
        graph.put(3, Arrays.asList(1));
        graph.put(0, new ArrayList<>());
        graph.put(1, new ArrayList<>());

        List<Integer> result = topologicalSort(graph);

        if (result.isEmpty()) {
            System.out.println("Cycle detected — no topological ordering.");
        } else {
            System.out.println("Topological Order: " + result);
        }
    }
}

