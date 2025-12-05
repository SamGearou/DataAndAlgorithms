package graphs.maxflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Ford-Fulkerson's algorithm for computing maximum flow in a directed graph.
 * Uses DFS to find augmenting paths.
 * 
 * Algorithm:
 * 1. Setup directed residual graph with edge capacity = original graph weights
 * 2. While there exists an augmenting path p from s to t:
 *    - Find minimum edge weight along path p
 *    - Decrease capacity of forward edges along path p by f
 *    - Increase capacity of backward edges along path p by f
 *    - Add f to max flow
 * 3. Return max flow
 * 
 * Time complexity: O(E * max_flow) in worst case
 * Space complexity: O(V + E)
 */
public class FordFulkerson {

    /**
     * Edge class for residual graph.
     * Each edge stores its residual capacity.
     */
    static class Edge {
        int to;
        long capacity;
        int reverseIndex; // index of reverse edge in adjacency list

        Edge(int to, long capacity, int reverseIndex) {
            this.to = to;
            this.capacity = capacity;
            this.reverseIndex = reverseIndex;
        }
    }

    private final int V; // number of vertices
    private final HashMap<Integer, List<Edge>> residualGraph; // residual graph as adjacency list

    /**
     * Constructor initializes the residual graph.
     * 
     * @param V number of vertices
     */
    public FordFulkerson(int V) {
        this.V = V;
        this.residualGraph = new HashMap<>();
        for (int i = 0; i < V; i++) {
            residualGraph.put(i, new ArrayList<>());
        }
    }

    /**
     * Adds a directed edge from u to v with given capacity.
     * Also creates a reverse edge with capacity 0 for residual graph.
     * 
     * @param u source vertex
     * @param v destination vertex
     * @param capacity edge capacity
     */
    public void addEdge(int u, int v, long capacity) {
        // Ensure both vertices exist in the graph
        residualGraph.putIfAbsent(u, new ArrayList<>());
        residualGraph.putIfAbsent(v, new ArrayList<>());
        
        // Forward edge: u -> v with capacity
        int forwardIndex = residualGraph.get(u).size();
        // Backward edge: v -> u with capacity 0 (will be updated during augmentation)
        int backwardIndex = residualGraph.get(v).size();

        residualGraph.get(u).add(new Edge(v, capacity, backwardIndex));
        residualGraph.get(v).add(new Edge(u, 0, forwardIndex));
    }

    /**
     * DFS helper to find an augmenting path from u to t.
     * Stores the path in parent array and returns the bottleneck capacity.
     * 
     * @param u current vertex
     * @param t sink vertex
     * @param visited visited array
     * @param parent parent array to store path
     * @param minCapacity minimum capacity found so far
     * @return minimum capacity along the path if path exists, 0 otherwise
     */
    private long dfs(int u, int t, boolean[] visited, int[] parent, long minCapacity) {
        if (u == t) {
            return minCapacity;
        }

        visited[u] = true;

        List<Edge> edges = residualGraph.getOrDefault(u, new ArrayList<>());
        for (Edge e : edges) {
            int v = e.to;
            if (!visited[v] && e.capacity > 0) {
                parent[v] = u;
                long flow = dfs(v, t, visited, parent, Math.min(minCapacity, e.capacity));
                if (flow > 0) {
                    return flow;
                }
            }
        }

        return 0;
    }

    /**
     * Augments flow along the augmenting path from s to t.
     * 
     * @param s source vertex
     * @param t sink vertex
     * @param parent parent array from DFS
     * @param flow amount of flow to augment
     */
    private void augmentPath(int s, int t, int[] parent, long flow) {
        int v = t;
        
        // Traverse path backwards from t to s
        while (v != s) {
            int u = parent[v];
            
            // Find the edge from u to v in residual graph
            List<Edge> edges = residualGraph.getOrDefault(u, new ArrayList<>());
            for (Edge e : edges) {
                if (e.to == v) {
                    // Decrease capacity of forward edge (u -> v) by flow
                    e.capacity -= flow;
                    
                    // Increase capacity of backward edge (v -> u) by flow
                    List<Edge> reverseEdges = residualGraph.getOrDefault(v, new ArrayList<>());
                    Edge reverseEdge = reverseEdges.get(e.reverseIndex);
                    reverseEdge.capacity += flow;
                    break;
                }
            }
            
            v = u;
        }
    }

    /**
     * Computes the maximum flow from source s to sink t using Ford-Fulkerson method.
     * Uses DFS to find augmenting paths.
     * 
     * @param s source vertex
     * @param t sink vertex
     * @return maximum flow value
     */
    public long maxFlow(int s, int t) {
        long maxFlow = 0;

        // While there exists an augmenting path p from s to t
        while (true) {
            boolean[] visited = new boolean[V];
            int[] parent = new int[V];
            Arrays.fill(parent, -1);
            parent[s] = s;
            
            long flow = dfs(s, t, visited, parent, Long.MAX_VALUE);

            if (flow == 0) {
                // No augmenting path found, algorithm terminates
                break;
            }

            // Augment flow along the path
            augmentPath(s, t, parent, flow);
            maxFlow += flow;
        }

        return maxFlow;
    }

    /**
     * Example usage and tests
     */
    public static void main(String[] args) {
        System.out.println("=== Ford-Fulkerson Max Flow Algorithm ===\n");

        // Example 1: Simple graph
        System.out.println("Example 1: Simple graph");
        FordFulkerson ff1 = new FordFulkerson(4);
        ff1.addEdge(0, 1, 10); // s -> A: capacity 10
        ff1.addEdge(0, 2, 10); // s -> B: capacity 10
        ff1.addEdge(1, 2, 2);  // A -> B: capacity 2
        ff1.addEdge(1, 3, 15);  // A -> t: capacity 15
        ff1.addEdge(2, 3, 10);  // B -> t: capacity 10
        
        long maxFlow1 = ff1.maxFlow(0, 3);
        System.out.println("Max flow from 0 to 3: " + maxFlow1);
        System.out.println("Expected: 20\n");

        // Example 2: Classic flow network
        System.out.println("Example 2: Classic flow network");
        FordFulkerson ff2 = new FordFulkerson(6);
        // Source (0) to intermediate nodes
        ff2.addEdge(0, 1, 16); // s -> 1: capacity 16
        ff2.addEdge(0, 2, 13); // s -> 2: capacity 13
        // Intermediate edges
        ff2.addEdge(1, 2, 10);  // 1 -> 2: capacity 10
        ff2.addEdge(1, 3, 12);  // 1 -> 3: capacity 12
        ff2.addEdge(2, 1, 4);   // 2 -> 1: capacity 4
        ff2.addEdge(2, 4, 14);  // 2 -> 4: capacity 14
        ff2.addEdge(3, 2, 9);   // 3 -> 2: capacity 9
        ff2.addEdge(3, 5, 20);   // 3 -> t: capacity 20
        ff2.addEdge(4, 3, 7);   // 4 -> 3: capacity 7
        ff2.addEdge(4, 5, 4);   // 4 -> t: capacity 4
        
        long maxFlow2 = ff2.maxFlow(0, 5);
        System.out.println("Max flow from 0 to 5: " + maxFlow2);
        System.out.println("Expected: 23\n");

        // Example 3: Simple path
        System.out.println("Example 3: Simple path");
        FordFulkerson ff3 = new FordFulkerson(3);
        ff3.addEdge(0, 1, 5);  // s -> 1: capacity 5
        ff3.addEdge(1, 2, 3);  // 1 -> t: capacity 3
        
        long maxFlow3 = ff3.maxFlow(0, 2);
        System.out.println("Max flow from 0 to 2: " + maxFlow3);
        System.out.println("Expected: 3\n");
    }
}