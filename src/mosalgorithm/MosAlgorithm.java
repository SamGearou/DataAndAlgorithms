package mosalgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * leetcode hard: https://leetcode.com/problems/minimum-operations-to-equalize-subarrays/
 * Mo's Algorithm (CP): https://cp-algorithms.com/data_structures/sqrt_decomposition.html
 */
public class MosAlgorithm {
    private static final int blockSize = 200;

    public long[] minOperations(int[] nums, int k, int[][] queries) {
        MosAlgorithmImpl mo = new MosAlgorithmImpl(nums, k);
        long[] sol = new long[queries.length];
        long[] last = new long[nums.length];
        List<Query> q = new ArrayList<>();
        for (int i = 0; i < queries.length; i++) {
            q.add(new Query(queries[i][0], queries[i][1], i));
        }
        Collections.sort(q);

        for (int i = 1; i < nums.length; i++) {
            if (nums[i] % k == nums[i - 1] % k) {
                last[i] = last[i - 1];
            } else {
                last[i] = i;
            }
        }

        for (Query query : q) {
            if (last[query.r] > query.l) {
                sol[query.idx] = -1;
            } else {
                mo.processQuery(query, sol);
            }
        }

        return sol;
    }

    class Query implements Comparable<Query> {
        int l;
        int r;
        int idx;

        public Query(int l, int r, int idx) {
            this.l = l;
            this.r = r;
            this.idx = idx;
        }

        public int compareTo(Query other) {
            if (this.l / blockSize != other.l / blockSize) {
                return this.l - other.l;
            } else {
                return this.r - other.r;
            }
        }
    }

    class MosAlgorithmImpl {
        int[] nums;
        int k;
        TreeMap<Integer, Integer> left;
        TreeMap<Integer, Integer> right;
        long leftSum;
        long rightSum;
        int leftSize;
        int rightSize;
        int leftPos;
        int rightPos;

        public MosAlgorithmImpl(int[] nums, int k) {
            this.nums = nums;
            this.k = k;
            this.left = new TreeMap<>();
            this.right = new TreeMap<>();
            leftSum = 0;
            rightSum = 0;
            leftSize = 0;
            rightSize = 0;
            leftPos = 0;
            rightPos = -1;
        }

        public void add(int val) {
            if (left.size() == 0 || left.lastKey() >= val) {
                left.put(val, left.getOrDefault(val, 0) + 1);
                leftSum += val;
                leftSize++;
            } else {
                right.put(val, right.getOrDefault(val, 0) + 1);
                rightSum += val;
                rightSize++;
            }
            rebalance();
        }

        public void delete(int val) {
            if (left.containsKey(val)) {
                left.put(val, left.getOrDefault(val, 0) - 1);
                if (left.get(val) <= 0) {
                    left.remove(val);
                }
                leftSum -= val;
                leftSize--;
            } else {
                right.put(val, right.getOrDefault(val, 0) - 1);
                if (right.get(val) <= 0) {
                    right.remove(val);
                }
                rightSum -= val;
                rightSize--;
            }
            rebalance();
        }

        public void rebalance() {
            while (leftSize > rightSize + 1) {
                int topKey = left.lastKey();
                left.put(topKey, left.getOrDefault(topKey, 0) - 1);
                right.put(topKey, right.getOrDefault(topKey, 0) + 1);
                if (left.get(topKey) <= 0) {
                    left.remove(topKey);
                }
                leftSum -= topKey;
                rightSum += topKey;
                leftSize--;
                rightSize++;
            }
            while (leftSize < rightSize) {
                int bottomKey = right.firstKey();
                right.put(bottomKey, right.getOrDefault(bottomKey, 0) - 1);
                left.put(bottomKey, left.getOrDefault(bottomKey, 0) + 1);
                if (right.get(bottomKey) <= 0) {
                    right.remove(bottomKey);
                }
                leftSum += bottomKey;
                rightSum -= bottomKey;
                leftSize++;
                rightSize--;
            }
        }

        public void processQuery(Query query, long[] sol) {
            while (rightPos < query.r) {
                rightPos++;
                add(nums[rightPos]);
            }
            while (rightPos > query.r) {
                rightPos--;
                delete(nums[rightPos + 1]);
            }
            while (leftPos > query.l) {
                leftPos--;
                add(nums[leftPos]);
            }
            while (leftPos < query.l) {
                leftPos++;
                delete(nums[leftPos - 1]);
            }

            sol[query.idx] = calculateOps();
        }

        public long calculateOps() {
            int topKey = left.lastKey();
            long leftOps = (((long) topKey * (leftSize - 1)) - (leftSum - topKey)) / k;
            long rightOps = (rightSum - ((long) topKey * rightSize)) / k;

            return leftOps + rightOps;
        }
    }

    public static void main(String[] args) {
        MosAlgorithm solution = new MosAlgorithm();
        int[] nums = {1, 4, 7};
        int k = 3;
        int[][] queries = {{0, 1}, {0, 2}};
        
        long[] result = solution.minOperations(nums, k, queries);
        
        System.out.println("Input: nums = [1, 4, 7], k = 3");
        System.out.println("Queries: [[0,1],[0,2]]");
        for (int i = 0; i < result.length; i++) {
            System.out.println("Result for Query [" + queries[i][0] + ", " + queries[i][1] + "]: " + result[i]);
        }
    }
}
