package trees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Leetcode hard problem: https://leetcode.com/problems/maximum-subgraph-score-in-a-tree/description/
 * Video tutorial on a solution to the problem: https://www.youtube.com/watch?v=7eyaNW_KpAY
 * More information on "re-rooting DP on Trees" (generalized technique): https://www.youtube.com/watch?v=7_huTWwl5jM&t=1s
 */
public class LeetCode3772 {
    HashMap<Integer, List<Integer>> m = new HashMap<>();

    public int[] maxSubgraphScore(int n, int[][] edges, int[] good) {
        for (int[] edge : edges) {
            m.computeIfAbsent(edge[0], k -> new ArrayList<>()).add(edge[1]);
            m.computeIfAbsent(edge[1], k -> new ArrayList<>()).add(edge[0]);
        }
        int[] sol = new int[n];
        int[] downScore = new int[n];
        int[] upScore = new int[n];

        dfsDownScore(0, -1, good, downScore);
        dfsUpScoreAndAns(0, -1, downScore, upScore, sol);

        return sol;
    }

    public void dfsDownScore(int vertex, int parent, int[] good, int[] downScore) {
        downScore[vertex] = good[vertex] == 1 ? 1 : -1;
        for (int neighbor : m.getOrDefault(vertex, new ArrayList<>())) {
            if (neighbor != parent) {
                dfsDownScore(neighbor, vertex, good, downScore);
                downScore[vertex] += Math.max(0, downScore[neighbor]);
            }
        }
    }

    public void dfsUpScoreAndAns(int vertex, int parent, int[] downScore, int[] upScore, int[] sol) {
        sol[vertex] = downScore[vertex] + upScore[vertex];
        for (int neighbor : m.getOrDefault(vertex, new ArrayList<>())) {
            if (neighbor != parent) {
                int upScoreWithoutSubTree = downScore[vertex] - Math.max(0, downScore[neighbor]); // remove the neighbor's downscore from the parent's downScore
                upScore[neighbor] = Math.max(0, upScore[vertex] + upScoreWithoutSubTree);
                dfsUpScoreAndAns(neighbor, vertex, downScore, upScore, sol);
            }
        }
    }
}
