package graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GraphCheck {

    private HashMap<Integer, List<Integer>> graph; // adjacency list
    private HashMap<Integer, Integer> dfsNum;        // 0=UNVISITED, 1=EXPLORED, 2=VISITED
    private HashMap<Integer, Integer> dfsParent;     // parent of each vertex

    private final int UNVISITED = 0;
    private final int EXPLORED = 1;
    private final int VISITED = 2;

    public GraphCheck() {
        graph = new HashMap<>();
        dfsNum = new HashMap<>();
        dfsParent = new HashMap<>();
    }

    /**
     * Add a directed edge u -> v
     */
    public void addEdge(int u, int v) {
        graph.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
        graph.putIfAbsent(v, new ArrayList<>()); // ensure v exists in the map
    }

    /**
     * DFS to classify edges: tree, back, forward/cross, two-way
     */
    public void graphCheck(int u) {
        dfsNum.put(u, EXPLORED); // mark as EXPLORED

        for (int v : graph.get(u)) {
            int vType = dfsNum.getOrDefault(v, UNVISITED);

            if (vType == UNVISITED) { // Tree Edge
                dfsParent.put(v, u);
                graphCheck(v);
            } else if (vType == EXPLORED) { // Back Edge or Two-way
                if (dfsParent.getOrDefault(u, -1) == v) {
                    System.out.printf("Two ways (%d, %d)-(%d, %d)\n", u, v, v, u);
                } else {
                    System.out.printf("Back Edge (%d, %d) (Cycle)\n", u, v);
                }
            } else if (vType == VISITED) { // Forward/Cross Edge
                System.out.printf("Forward/Cross Edge (%d, %d)\n", u, v);
            }
        }

        dfsNum.put(u, VISITED); // mark as fully visited
    }

    public static void main(String[] args) {
        GraphCheck g = new GraphCheck();

        // Example graph
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 0); // cycle
        g.addEdge(1, 3);
        g.addEdge(3, 4);

        // Initialize dfsNum and dfsParent
        for (int vertex : g.graph.keySet()) {
            g.dfsNum.put(vertex, g.UNVISITED);
            g.dfsParent.put(vertex, -1);
        }

        // Run DFS on all vertices (handles disconnected components)
        for (int vertex : g.graph.keySet()) {
            if (g.dfsNum.get(vertex) == g.UNVISITED) {
                g.graphCheck(vertex);
            }
        }
    }
}