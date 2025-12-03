package dp;

/**
 * Leetcode hard: https://leetcode.com/problems/maximize-cyclic-partition-score/
 * Divide and Conquer DP (CP): https://cp-algorithms.com/dynamic_programming/divide-and-conquer-dp.html
 * Divide and Conquer (another good explanation: https://jeffreyxiao.me/blog/divide-and-conquer-optimization
 * Time Complexity: O(nklogn) where n is the array length and k is the number of partitions (running time with "non-divide and conquer" DP would be: O(n^2k)
 */
public class DivideAndConquerDP {
    public long maximumScore(int[] nums, int k) {
        int n = nums.length;
        long[][] dp = new long[n + 1][k + 1];
        int[] one = new int[n]; // stores the array shifting such that the minimum element is at the beginning of the array
        int[] two = new int[n]; // stores the array shifting such that the minimum element is the last element of the array

        int minInd = -1;
        int minVal = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            if (nums[i] < minVal) {
                minVal = nums[i];
                minInd = i;
            }
        }

        int index = 0;
        int iterations = n;
        while (iterations-- > 0) {
            one[index++] = nums[minInd];
            minInd = (minInd + 1) % n;
        }

        index = 0;
        iterations = n;
        while (iterations-- > 0) {
            two[index] = one[(index + 1) % n];
            index++;
        }

        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        long[][][] maxMin = new long[n + 1][n + 1][2];
        for (int i = 1; i <= n; i++) {
            for (int j = i; j <= n; j++) {
                min = Math.min(min, one[j - 1]);
                max = Math.max(max, one[j - 1]);
                maxMin[i][j][0] = min;
                maxMin[i][j][1] = max;
            }
            min = Long.MAX_VALUE;
            max = Long.MIN_VALUE;
        }

        for (int i = 1; i <= k; i++) {
            compute(dp, i, 1, n, 1, n, maxMin);
        }

        long sol = Long.MIN_VALUE;
        for (int i = 1; i <= k; i++) {
            sol = Math.max(sol, dp[n][i]);
        }

        dp = new long[n + 1][k + 1];

        min = Long.MAX_VALUE;
        max = Long.MIN_VALUE;
        maxMin = new long[n + 1][n + 1][2];
        for (int i = 1; i <= n; i++) {
            for (int j = i; j <= n; j++) {
                min = Math.min(min, two[j - 1]);
                max = Math.max(max, two[j - 1]);
                maxMin[i][j][0] = min;
                maxMin[i][j][1] = max;
            }
            min = Long.MAX_VALUE;
            max = Long.MIN_VALUE;
        }

        for (int i = 1; i <= k; i++) {
            compute(dp, i, 1, n, 1, n, maxMin);
        }

        for (int i = 1; i <= k; i++) {
            sol = Math.max(sol, dp[n][i]);
        }

        return sol;
    }

    /**
     * Computes the optimal DP values for group g in the range [i, j] using divide and conquer optimization.
     * This function implements the divide and conquer DP technique to efficiently find the optimal splitting points.
     * 
     * The key insight is the monotonicity property: if opt(i, j) is the optimal splitting point for position j
     * in group g, then opt(i, j') <= opt(i, j) for all j' < j. This allows us to narrow the search space.
     * 
     * @param dp The DP table where dp[mid][g] stores the maximum score for position mid with g groups
     * @param g The current group number (1-indexed)
     * @param i The start index of the range to compute (1-indexed)
     * @param j The end index of the range to compute (1-indexed)
     * @param l The lower bound for the optimal splitting point search
     * @param r The upper bound for the optimal splitting point search
     * @param maxMin Precomputed array where maxMin[k][mid][0] = min and maxMin[k][mid][1] = max in range [k, mid]
     */
    void compute(long[][] dp, int g, int i, int j, int l, int r, long[][][] maxMin) {
        if (i <= j) {
            int bestIndex = l;
            int mid = (i + j) / 2;
            for (int k = l; k <= Math.min(mid, r); k++) {
                long val = dp[k - 1][g - 1] + maxMin[k][mid][1] - maxMin[k][mid][0]; // k is the start of the "new" group, k-1 is the last person in the previous group.
                if (val > dp[mid][g]) {
                    dp[mid][g] = val;
                    bestIndex = k;
                }
            }
            compute(dp, g, mid + 1, j, bestIndex, r, maxMin);
            compute(dp, g, i, mid - 1, l, bestIndex, maxMin);
        }
    }

    public static void main(String[] args) {
        DivideAndConquerDP solution = new DivideAndConquerDP();
        int[] nums = {1, 3, 4};
        int k = 2;
        
        long result = solution.maximumScore(nums, k);
        
        // The result represents the maximum score achievable by partitioning the cyclic array
        // into k contiguous partitions, where each partition's score is (max - min) of that partition.
        // For nums = [1, 3, 4] and k = 2, the algorithm considers all possible ways to split the array
        // (considering all cyclic rotations) into 2 contiguous partitions. For example:
        // - Partitions: [1] and [3, 4] -> scores: (1-1) + (4-3) = 0 + 1 = 1
        // - Partitions: [1, 3] and [4] -> scores: (3-1) + (4-4) = 2 + 0 = 2
        // - Or with cyclic rotation [3, 4, 1]: [3] and [4, 1] -> scores: (3-3) + (4-1) = 0 + 3 = 3
        // The algorithm finds the partition arrangement that maximizes the sum of (max - min) for each partition.
        System.out.println("Input: nums = [1, 3, 4], k = 2");
        System.out.println("Result: " + result);
    }
}
