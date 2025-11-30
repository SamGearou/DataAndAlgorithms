package kadanes;

/**
 * Implementation of Kadane's Algorithm.
 * Finds the maximum sum of a contiguous subarray in O(n) time.
 */
public class KadanesAlg {

    /**
     * Returns the maximum sum of any contiguous subarray of arr.
     *
     * @param arr input array of integers
     * @return maximum subarray sum
     */
    public int maxSubArraySum(int[] arr) {
        // ans stores the maximum sum found so far
        int ans = Integer.MIN_VALUE;

        // sum stores the sum of the current subarray
        int sum = 0;

        for (int x : arr) {
            // Add current element to the running subarray sum
            sum += x;

            // Update maximum if current subarray sum is greater
            ans = Math.max(ans, sum);

            // If running sum becomes negative, reset to 0
            // because a negative sum would reduce any future subarray sum
            if (sum < 0) {
                sum = 0;
            }
        }

        return ans;
    }

    public static void main(String[] args) {
        int[] arr = new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4};
        KadanesAlg kadane = new KadanesAlg();

        // Expected output: 6 (subarray [4, -1, 2, 1])
        System.out.println("Maximum subarray sum: " + kadane.maxSubArraySum(arr));
    }
}
