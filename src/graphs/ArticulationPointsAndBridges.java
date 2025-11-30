package graphs;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ArticulationPointsAndBridges {

    // Graph represented as an adjacency list using a HashMap
    private HashMap<Integer, List<Integer>> graph = new HashMap<>();

    private int V;                                  // number of vertices
    private int[] dfs_num, dfs_low, dfs_parent;     // arrays used by Tarjan's DFS
    private boolean[] articulation_vertex;          // which vertices are articulation points
    private int dfsNumberCounter;                   // keeps increasing during DFS
    private int dfsRoot, rootChildren;              // special handling for the DFS root

    private static final int UNVISITED = -1;

    /**
     * Constructor initializes the graph and DFS helper arrays.
     */
    public ArticulationPointsAndBridges(int V) {
        this.V = V;

        // Initialize adjacency lists
        for (int i = 0; i < V; i++) {
            graph.put(i, new ArrayList<>());
        }

        dfs_num = new int[V];
        dfs_low = new int[V];
        dfs_parent = new int[V];
        articulation_vertex = new boolean[V];
    }

    /**
     * Add an undirected edge (u -- v) to the graph.
     */
    public void addEdge(int u, int v) {
        graph.get(u).add(v);
        graph.get(v).add(u);
    }

    /**
     * Driver function: runs DFS on each connected component,
     * detects articulation points and bridges.
     */
    public void findArticulationPointsAndBridges() {
        Arrays.fill(dfs_num, UNVISITED);
        dfsNumberCounter = 0;

        System.out.println("Bridges:");

        // Run DFS on every component
        for (int i = 0; i < V; i++) {
            if (dfs_num[i] == UNVISITED) {
                dfsRoot = i;               // Mark root of DFS
                rootChildren = 0;          // Count children of root
                articulationPointAndBridge(i);

                // Root is an articulation point iff it has 2 or more DFS children
                articulation_vertex[dfsRoot] = (rootChildren > 1);
            }
        }

        // Print articulation points
        System.out.println("\nArticulation Points:");
        for (int i = 0; i < V; i++) {
            if (articulation_vertex[i]) {
                System.out.println(" Vertex " + i);
            }
        }
    }

    /**
     * Core recursive DFS function implementing Tarjan's algorithm.
     * dfs_num[u] = order of visitation
     * dfs_low[u] = lowest reachable dfs_num from u (via tree or back edge)
     */
    private void articulationPointAndBridge(int u) {
        dfs_low[u] = dfs_num[u] = dfsNumberCounter++;

        for (int v : graph.get(u)) {

            if (dfs_num[v] == UNVISITED) {      // Tree Edge (u → v)
                dfs_parent[v] = u;

                if (u == dfsRoot)               // Special root rule
                    rootChildren++;

                articulationPointAndBridge(v);  // DFS deeper

                // Check if u is an articulation point
                if (dfs_low[v] >= dfs_num[u])
                    articulation_vertex[u] = true;

                // Check if edge u–v is a bridge
                if (dfs_low[v] > dfs_num[u])
                    System.out.println(" Edge (" + u + ", " + v + ") is a bridge");

                // Update dfs_low[u] after visiting child v. If the child has a dfs_low[v] that
                // is lower than dfs_low[u] (the parent), the parent will also be able to reach
                // the dfs_num[x] vertex that the child can reach, so we need to update dfs_low[u] for
                // the parent as well.
                dfs_low[u] = Math.min(dfs_low[u], dfs_low[v]);

            } else if (v != dfs_parent[u]) {    // Back Edge (ignore direct parent)
                dfs_low[u] = Math.min(dfs_low[u], dfs_num[v]);
            }
        }
    }

    /**
     * Example usage
     */
    public static void main(String[] args) {

        /*
            Example Graph (same as in CLRS and CP3 book):

                       1
                     /   \
                    0 --- 2
                    |
                    3
                    |
                    4

            Adjacency:
            0: [1,2,3]
            1: [0,2]
            2: [0,1]
            3: [0,4]
            4: [3]

            Expected Output:
              Bridges: (0,3), (3,4)
              Articulation Points: 0, 3
        */

        ArticulationPointsAndBridges g = new ArticulationPointsAndBridges(5);

        // Create the graph from the diagram
        g.addEdge(1, 0);
        g.addEdge(0, 2);
        g.addEdge(2, 1);
        g.addEdge(0, 3);
        g.addEdge(3, 4);

        // Run Tarjan algorithm
        g.findArticulationPointsAndBridges();
    }
}