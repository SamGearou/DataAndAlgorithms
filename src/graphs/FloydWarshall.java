package graphs;

import java.util.ArrayList;
import java.util.List;

public class FloydWarshall {

    /**
     * Runs Floyd-Warshall algorithm on a weighted directed graph.
     * Graph is represented as an adjacency matrix.
     * Solves the APSP (all-pairs shortest path) on weighted graphs
     * <p>
     * Time complexity: O(V^3)
     * Space complexity: O(V^2)
     *
     * @param graph adjacency matrix of the graph
     * @return distance matrix containing shortest distances between all pairs
     */
    public static long[][] floydWarshall(long[][] graph) {
        int V = graph.length;
        long INF = Long.MAX_VALUE / 2; // prevent overflow

        // Initialize distance matrix and parent matrix for path reconstruction
        long[][] dist = new long[V][V];
        int[][] parent = new int[V][V];

        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                dist[i][j] = graph[i][j];
                parent[i][j] = i;
            }
        }

        // Triple loop: update distances using intermediate vertices
        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        parent[i][j] = parent[k][j];
                    }
                }
            }
        }

        // Check for negative cycles
        for (int i = 0; i < V; i++) {
            if (dist[i][i] < 0) {
                System.out.println("Graph contains a negative weight cycle");
                return null;
            }
        }

        return dist;
    }

    /**
     * Prints the shortest path from vertex i to j using the parent matrix.
     */
    public void printPath(int[][] parent, int i, int j) {
        if (i != j) printPath(parent, i, parent[i][j]);
        System.out.print(j + " ");
    }

    /**
     * Computes the transitive closure of a graph using 1/0 and OR operator.
     * <p>
     * Transitive closure of a graph tells us **which vertices are reachable from which**.
     * In other words, for every pair of vertices (i, j):
     * - If there exists a path from vertex i to vertex j (direct or via other vertices),
     * reachable[i][j] = 1.
     * - If no path exists, reachable[i][j] = 0.
     * <p>
     * Example:
     * If reachable[0][2] = 1, it means vertex 2 can be reached from vertex 0 by following
     * edges, possibly through intermediate vertices.
     * <p>
     * This is useful for checking connectivity between nodes in a directed graph,
     * even if there isn’t a direct edge between them.
     * <p>
     * The algorithm uses a Floyd-Warshall style triple loop:
     * reachable[i][j] = reachable[i][j] OR (reachable[i][k] AND reachable[k][j])
     * which propagates the connectivity information through all intermediate vertices k.
     *
     * @param graph adjacency matrix (non-zero = edge exists, zero = no edge)
     * @return reachability matrix: 1 if reachable, 0 otherwise
     */
    public static int[][] transitiveClosure(int[][] graph) {
        int V = graph.length;
        int[][] reachable = new int[V][V];

        // Initialize reachable matrix
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                reachable[i][j] = (graph[i][j] != 0 ? 1 : 0);
            }
            reachable[i][i] = 1; // each vertex can reach itself
        }

        // Floyd-Warshall style update using OR and AND operators
        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    reachable[i][j] = reachable[i][j] | (reachable[i][k] & reachable[k][j]);
                }
            }
        }

        return reachable;
    }

    /**
     * Finds the cheapest (most negative) cycle in the graph.
     * <p>
     * After running Floyd-Warshall, checks all diagonal entries dist[i][i].
     * If dist[i][i] < 0, it means there's a negative cycle that includes vertex i.
     * The cheapest cycle is the one with the most negative total weight.
     * <p>
     * Time complexity: O(V^3) for Floyd-Warshall + O(V) for cycle detection = O(V^3)
     * Space complexity: O(V^2)
     *
     * @param graph adjacency matrix of the graph
     * @return the weight of the cheapest negative cycle, or Long.MAX_VALUE if no negative cycle exists
     */
    public static long findCheapestNegativeCycle(long[][] graph) {
        int V = graph.length;
        long INF = Long.MAX_VALUE / 2;

        // Run Floyd-Warshall to compute shortest distances
        long[][] dist = new long[V][V];
        int[][] parent = new int[V][V];

        // Initialize distance and parent matrices
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                dist[i][j] = graph[i][j];
                parent[i][j] = i;
            }
            // Set diagonal to INF to find cycles (path from i back to i)
            dist[i][i] = INF;
        }

        // Floyd-Warshall algorithm
        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        parent[i][j] = parent[k][j];
                    }
                }
            }
        }

        // Find the cheapest negative cycle by checking all diagonal entries
        long cheapestCycle = Long.MAX_VALUE;
        for (int i = 0; i < V; i++) {
            if (dist[i][i] < 0) {
                cheapestCycle = Math.min(cheapestCycle, dist[i][i]);
            }
        }

        return cheapestCycle;
    }

    /**
     * Finds the diameter of a graph.
     * <p>
     * The diameter is the maximum shortest path distance between any pair of vertices.
     * This method computes all-pairs shortest paths using Floyd-Warshall and returns
     * the maximum distance found.
     * <p>
     * Time complexity: O(V^3) for Floyd-Warshall + O(V^2) for finding maximum = O(V^3)
     * Space complexity: O(V^2)
     *
     * @param graph adjacency matrix of the graph
     * @return the diameter of the graph (maximum shortest path distance),
     * or -1 if the graph is disconnected (no finite paths exist between all pairs)
     */
    public static long findDiameter(long[][] graph) {
        int V = graph.length;
        long INF = Long.MAX_VALUE / 2;

        // Run Floyd-Warshall to compute all-pairs shortest paths
        long[][] dist = floydWarshall(graph);

        // If graph contains negative cycles, diameter is undefined
        if (dist == null) {
            return -1;
        }

        // Find the maximum shortest path distance
        long diameter = -1;
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                // Skip diagonal entries (distance from vertex to itself is 0)
                if (i != j && dist[i][j] < INF) {
                    diameter = Math.max(diameter, dist[i][j]);
                }
            }
        }

        return diameter;
    }

    /**
     * Finds the strongly connected components (SCCs) of a graph using Floyd-Warshall.
     * <p>
     * Two vertices i and j belong to the same strongly connected component if and only if
     * there exists a path from i to j and a path from j to i. Using Floyd-Warshall,
     * we can check if both dist[i][j] and dist[j][i] are finite (less than INF).
     * <p>
     * Time complexity: O(V^3) for Floyd-Warshall + O(V^2) for SCC grouping = O(V^3)
     * Space complexity: O(V^2)
     *
     * @param graph adjacency matrix of the graph
     * @return list of strongly connected components, where each component is a list of vertex indices
     */
    public static List<List<Integer>> findStronglyConnectedComponents(long[][] graph) {
        int V = graph.length;
        long INF = Long.MAX_VALUE / 2;

        // Run Floyd-Warshall to compute all-pairs shortest paths
        long[][] dist = floydWarshall(graph);

        // If graph contains negative cycles, return empty list
        if (dist == null) {
            return new ArrayList<>();
        }

        // Build reachability matrix: reachable[i][j] = true if i can reach j
        boolean[][] reachable = new boolean[V][V];
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                reachable[i][j] = (dist[i][j] < INF);
            }
        }

        // Find strongly connected components
        // Two vertices are in the same SCC if they can reach each other
        List<List<Integer>> components = new ArrayList<>();
        boolean[] assigned = new boolean[V];

        for (int i = 0; i < V; i++) {
            if (assigned[i]) continue;

            // Find all vertices in the same SCC as i
            List<Integer> component = new ArrayList<>();
            for (int j = 0; j < V; j++) {
                // j is in the same SCC as i if both can reach each other
                if (reachable[i][j] && reachable[j][i]) {
                    component.add(j);
                    assigned[j] = true;
                }
            }
            components.add(component);
        }

        return components;
    }

    /**
     * Computes the minimax (maximin) distances between all pairs of vertices.
     * <p>
     * minimax[i][j] = the smallest possible maximum edge weight along any path from i to j.
     *
     * @param graph adjacency matrix of the graph (INF if no direct edge)
     * @return minimax matrix
     */
    public static long[][] minimaxPaths(long[][] graph) {
        int V = graph.length;
        long INF = Long.MAX_VALUE / 2;
        long[][] minimax = new long[V][V];

        // Initialize minimax matrix - all entries to INF initially
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                minimax[i][j] = INF;
            }
        }

        // Set diagonal to 0 and copy direct edges from graph
        for (int i = 0; i < V; i++) {
            minimax[i][i] = 0;
            for (int j = 0; j < V; j++) {
                if (i != j && graph[i][j] < INF) {
                    minimax[i][j] = graph[i][j];
                }
            }
        }

        // Floyd-Warshall style update for minimax paths
        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    // Only update if both paths exist (neither is INF)
                    if (minimax[i][k] < INF && minimax[k][j] < INF) {
                        long candidate = Math.max(minimax[i][k], minimax[k][j]);
                        if (minimax[i][j] >= INF || candidate < minimax[i][j]) {
                            minimax[i][j] = candidate;
                        }
                    }
                }
            }
        }

        return minimax;
    }

    public static void main(String[] args) {
        int V = 4;
        long INF = Long.MAX_VALUE / 2;
        long[][] graph = new long[V][V];

        // Initialize adjacency matrix
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (i == j) graph[i][j] = 0;
                else graph[i][j] = INF;
            }
        }

        // Add weighted edges
        graph[0][1] = 5;
        graph[0][3] = 10;
        graph[1][2] = 3;
        graph[2][3] = 1;

        // --- Floyd-Warshall shortest paths ---
        long[][] dist = floydWarshall(graph);

        if (dist != null) {
            System.out.println("All-pairs shortest distances:");
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (dist[i][j] >= INF) {
                        System.out.println("Distance from " + i + " → " + j + " = INF");
                    } else {
                        System.out.println("Distance from " + i + " → " + j + " = " + dist[i][j]);
                    }
                }
            }
        }

        // --- Transitive closure using 1/0 ---
        int[][] adjMatrix = new int[V][V];
        adjMatrix[0][1] = 1;
        adjMatrix[0][3] = 1;
        adjMatrix[1][2] = 1;
        adjMatrix[2][3] = 1;

        int[][] reach = transitiveClosure(adjMatrix);

        System.out.println("\nTransitive closure (reachability using 1/0):");
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                System.out.println("Transitive Closure for " + i + " → " + j + ": " + reach[i][j] + " ");
            }
            System.out.println();
        }

        // --- Minimax paths example ---
        long[][] minimax = minimaxPaths(graph);

        System.out.println("\nMinimax paths (smallest maximum edge on any path from i to j):");
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (minimax[i][j] >= INF) {
                    System.out.println("Minimax from " + i + " → " + j + " = INF");
                } else {
                    System.out.println("Minimax from " + i + " → " + j + " = " + minimax[i][j]);
                }
            }
        }
    }
}