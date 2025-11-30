package graphs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;

/**
 * Tarjan's Algorithm for finding Strongly Connected Components (SCCs)
 * in a directed graph.
 * <p>
 * The graph is represented as a HashMap<Integer, List<Integer>>.
 * Node IDs are assumed to be in the range 0..V-1.
 */
public class TarjanSCC {

    private final int V;                                   // number of nodes 0..V-1
    private final HashMap<Integer, List<Integer>> graph;   // adjacency list

    // Tarjan's algorithm state
    private int[] dfsNum;          // dfsNum[u] = discovery time of u
    private int[] dfsLow;          // dfsLow[u] = lowest reachable discovery time from u
    private boolean[] onStack;     // true if a node is currently on the stack
    private Deque<Integer> stack;  // Tarjan stack

    private int dfsCounter;        // running counter for discovery times
    private int numSCC;            // number of SCCs found
    private final int UNVISITED = -1;

    /**
     * Constructor.
     *
     * @param V     number of nodes (0..V-1)
     * @param graph adjacency list
     */
    public TarjanSCC(int V, HashMap<Integer, List<Integer>> graph) {
        this.V = V;
        this.graph = graph;
    }

    /**
     * Runs Tarjan's algorithm and prints SCCs as they are found.
     */
    public void runTarjan() {
        dfsNum = new int[V];
        dfsLow = new int[V];
        onStack = new boolean[V];
        stack = new ArrayDeque<>();

        Arrays.fill(dfsNum, UNVISITED);  // -1 means unvisited
        dfsCounter = 0;
        numSCC = 0;

        // Handle disconnected graphs
        for (int u = 0; u < V; u++) {
            if (dfsNum[u] == UNVISITED) {
                tarjanSCC(u);
            }
        }
    }

    /**
     * Recursive DFS that identifies SCCs using Tarjan's algorithm.
     *
     * @param u current node
     */
    private void tarjanSCC(int u) {
        dfsNum[u] = dfsLow[u] = dfsCounter++;
        stack.push(u);
        onStack[u] = true;

        for (int v : graph.getOrDefault(u, new ArrayList<>())) {
            if (dfsNum[v] == UNVISITED) {
                tarjanSCC(v);
            }
            if (onStack[v]) { // condition for update (only update if vertex has been visited (part of current SCC)
                dfsLow[u] = Math.min(dfsLow[u], dfsLow[v]);
            }
        }

        // If u is a root of an SCC
        if (dfsLow[u] == dfsNum[u]) {
            numSCC++;
            System.out.print("SCC " + numSCC + ": ");
            while (true) {
                int v = stack.pop();
                onStack[v] = false;
                System.out.print(v + " ");
                if (v == u) break;
            }
            System.out.println();
        }
    }

    /**
     * Example usage with the updated graph.
     */
    public static void main(String[] args) {

        /*
         * EXAMPLE GRAPH (nodes 0..7)
         *
         * Edges:
         *   0 → 1
         *   1 → 3
         *   2 → 1
         *   3 → 2, 4
         *   4 → 5
         *   5 → 7
         *   7 → 6
         *   6 → 4
         *
         * VISUAL STRUCTURE:
         *
         *       ┌─────┐
         *       ↓     │
         *   1 ↔ 3 ↔ 2
         *         ↑
         *         │
         *   0 → 1
         *
         *   4 → 5 → 7 → 6
         *   ↑             ↓
         *   └─────────────┘
         *
         * STRONGLY CONNECTED COMPONENTS:
         *
         *   {1, 2, 3}       ← cycle
         *   {4, 5, 6, 7}    ← cycle
         *   {0}             ← single node
         *
         * EXPECTED OUTPUT (order may vary):
         *
         *   SCC X: 1 3 2
         *   SCC X: 4 6 7 5
         *   SCC X: 0
         */

        int V = 8;

        HashMap<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, List.of(1));
        graph.put(1, List.of(3));
        graph.put(2, List.of(1));
        graph.put(3, List.of(2, 4));
        graph.put(4, List.of(5));
        graph.put(5, List.of(7));
        graph.put(7, List.of(6));
        graph.put(6, List.of(4));

        TarjanSCC tarjan = new TarjanSCC(V, graph);
        tarjan.runTarjan();
    }
}
