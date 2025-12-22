package leetcode.trees;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Leetcode hard problem: https://leetcode.com/problems/total-sum-of-interaction-cost-in-tree-groups/
 * More information on "re-rooting DP on Trees" (generalized technique): https://www.youtube.com/watch?v=7_huTWwl5jM&t=1s
 */
public class Leetcode3786 {
    long sol = 0;

    public long interactionCosts(int n, int[][] edges, int[] group) {
        HashMap<Integer, ArrayList<Integer>> map = new HashMap<>();
        HashMap<Integer, Integer> parent = new HashMap<>();
        long[][][] down = new long[n][21][2];
        long[][][] up = new long[n][21][2];
        for (int[] edge : edges) {
            map.computeIfAbsent(edge[0], k -> new ArrayList<>()).add(edge[1]);
            map.computeIfAbsent(edge[1], k -> new ArrayList<>()).add(edge[0]);
        }
        dfsSetup(0, -1, map, group, down);
        dfsFindCosts(0, -1, down, up, map, group);

        return sol / 2;
    }

    public long[][] dfsSetup(int vertex, int parent, HashMap<Integer, ArrayList<Integer>> map, int[] group, long[][][] down) {
        int vGroup = group[vertex];
        for (int neighbor : map.getOrDefault(vertex, new ArrayList<>())) {
            if (neighbor != parent) {
                dfsSetup(neighbor, vertex, map, group, down);
                for (int i = 1; i < 21; i++) {
                    down[vertex][i][0] += down[neighbor][i][0] + down[neighbor][i][1];
                    down[vertex][i][1] += down[neighbor][i][1];
                }
            }
        }

        down[vertex][vGroup][1]++;
        return down[vertex];
    }

    public void dfsFindCosts(int vertex, int parent, long[][][] down, long[][][] up, HashMap<Integer, ArrayList<Integer>> map, int[] group) {
        int vGroup = group[vertex];
        sol += down[vertex][vGroup][0] + up[vertex][vGroup][0];
        up[vertex][vGroup][1]++;
        for (int neighbor : map.getOrDefault(vertex, new ArrayList<>())) {
            if (neighbor != parent) {
                for (int i = 1; i < 21; i++) {
                    up[neighbor][i][0] = down[vertex][i][0] - (down[neighbor][i][0] + down[neighbor][i][1]);
                    up[neighbor][i][1] = down[vertex][i][1] - down[neighbor][i][1];
                    up[neighbor][i][0] += up[neighbor][i][1];

                    up[neighbor][i][0] += (up[vertex][i][0] + up[vertex][i][1]);
                    up[neighbor][i][1] += up[vertex][i][1];

                    if (vGroup == i) {
                        up[neighbor][i][0]--;
                        up[neighbor][i][1]--;
                    }
                }
                dfsFindCosts(neighbor, vertex, down, up, map, group);
            }
        }
    }
}
