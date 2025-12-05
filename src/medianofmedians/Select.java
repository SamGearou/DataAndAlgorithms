package medianofmedians;

/**
 * Select algorithm using Median of Medians for guaranteed O(n) worst-case time complexity.
 * Finds the kth smallest element in an unsorted array.
 */
public class Select {

    /**
     * Selects the kth smallest element from an array in O(n) worst-case time.
     * Uses the median of medians algorithm for pivot selection.
     *
     * @param arr the array to search in
     * @param k   the kth smallest element to find (1-indexed, so k=1 is the smallest)
     * @return the kth smallest element
     */
    public static int select(int[] arr, int k) {
        return selectRecursive(arr, 0, arr.length - 1, k - 1); // Convert to 0-indexed
    }

    /**
     * Recursively selects the element at index k (0-indexed) in the sorted order of arr[left..right].
     * Uses the median of medians algorithm for pivot selection to guarantee O(n) worst-case time.
     *
     * @param arr   the array
     * @param left  left boundary (inclusive)
     * @param right right boundary (inclusive)
     * @param k     the target index (0-indexed)
     * @return the element at position k in sorted order
     */
    private static int selectRecursive(int[] arr, int left, int right, int k) {
        if (left == right) {
            return arr[left];
        }

        // Find a good pivot using median of medians
        int pivotIndex = findPivot(arr, left, right);

        // Partition around the pivot
        pivotIndex = partition(arr, left, right, pivotIndex);

        // Recurse on the appropriate side
        if (k == pivotIndex) {
            return arr[k];
        } else if (k < pivotIndex) {
            return selectRecursive(arr, left, pivotIndex - 1, k);
        } else {
            return selectRecursive(arr, pivotIndex + 1, right, k);
        }
    }

    /**
     * Finds a good pivot using the median of medians approach.
     * Divides the array into groups of 5, finds median of each group,
     * then recursively finds the median of those medians.
     * This guarantees that the pivot will be in the middle 30-70% of elements.
     *
     * @param arr   the array
     * @param left  left boundary
     * @param right right boundary
     * @return the index of the pivot element
     */
    private static int findPivot(int[] arr, int left, int right) {
        int n = right - left + 1;

        // If array is small (≤5 elements), just return the median directly
        if (n <= 5) {
            return medianOfFive(arr, left, right);
        }

        // Divide into groups of 5 and find median value of each group
        int numGroups = (n + 4) / 5; // Ceiling division
        int[] medianValues = new int[numGroups];

        for (int i = 0; i < numGroups; i++) {
            int groupLeft = left + i * 5;
            int groupRight = Math.min(left + i * 5 + 4, right);
            int medianIdx = medianOfFive(arr, groupLeft, groupRight);
            medianValues[i] = arr[medianIdx];
        }

        // Recursively find median of medians (this returns a value)
        int medianOfMediansValue = selectRecursive(medianValues, 0, numGroups - 1, numGroups / 2);

        // Find the index of this median value in the original array segment
        for (int i = left; i <= right; i++) {
            if (arr[i] == medianOfMediansValue) {
                return i;
            }
        }

        // Fallback (should not happen)
        return left;
    }

    /**
     * Finds the median of at most 5 elements using insertion sort.
     * Returns the index of the median element.
     *
     * @param arr   the array
     * @param left  left boundary
     * @param right right boundary
     * @return the index of the median element
     */
    private static int medianOfFive(int[] arr, int left, int right) {
        int n = right - left + 1;
        int[] temp = new int[n];
        int[] indices = new int[n];

        // Copy elements with their original indices
        for (int i = 0; i < n; i++) {
            temp[i] = arr[left + i];
            indices[i] = left + i;
        }

        // Insertion sort
        for (int i = 1; i < n; i++) {
            int key = temp[i];
            int keyIdx = indices[i];
            int j = i - 1;

            while (j >= 0 && temp[j] > key) {
                temp[j + 1] = temp[j];
                indices[j + 1] = indices[j];
                j--;
            }
            temp[j + 1] = key;
            indices[j + 1] = keyIdx;
        }

        // Return index of median
        return indices[n / 2];
    }

    /**
     * Partitions the array around the pivot value at pivotIndex.
     * Elements less than pivot are moved to the left, greater than pivot to the right.
     *
     * @param arr        the array
     * @param left       left boundary
     * @param right      right boundary
     * @param pivotIndex index of the pivot element
     * @return the final position of the pivot after partitioning
     */
    private static int partition(int[] arr, int left, int right, int pivotIndex) {
        int pivotValue = arr[pivotIndex];

        // Move pivot to end
        swap(arr, pivotIndex, right);

        // Partition: move all elements < pivot to the left
        int storeIndex = left;
        for (int i = left; i < right; i++) {
            if (arr[i] < pivotValue) {
                swap(arr, storeIndex, i);
                storeIndex++;
            }
        }

        // Move pivot to its final position
        swap(arr, right, storeIndex);
        return storeIndex;
    }

    /**
     * Swaps two elements in the array.
     *
     * @param arr the array
     * @param i   first index
     * @param j   second index
     */
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * Finds the median of an array using the select algorithm.
     *
     * @param arr the array
     * @return the median value
     * @throws IllegalArgumentException if array is null or empty
     */
    public static int findMedian(int[] arr) {
        int k = (arr.length + 1) / 2; // Median position (1-indexed)
        return select(arr, k);
    }

    // Example usage and tests
    public static void main(String[] args) {
        System.out.println("=== Select Algorithm Tests ===\n");

        // Test 1
        int[] test1 = {5, 2, 8, 1, 9, 3, 7, 4, 6};
        System.out.println("Test 1 - Array: " + java.util.Arrays.toString(test1));
        System.out.println("1st smallest: " + select(test1, 1));
        System.out.println("3rd smallest: " + select(test1, 3));
        System.out.println("5th smallest (median): " + select(test1, 5));
        System.out.println("9th smallest: " + select(test1, 9));
        System.out.println("Median (using findMedian): " + findMedian(test1));
        System.out.println();

        // Test 2
        int[] test2 = {12, 3, 5, 7, 4, 19, 26, 1, 8, 15};
        System.out.println("Test 2 - Array: " + java.util.Arrays.toString(test2));
        System.out.println("1st smallest: " + select(test2, 1));
        System.out.println("5th smallest: " + select(test2, 5));
        System.out.println("10th smallest: " + select(test2, 10));
        System.out.println("Median (using findMedian): " + findMedian(test2));
        System.out.println();

        // Test 3 - Single element
        int[] test3 = {42};
        System.out.println("Test 3 - Array: " + java.util.Arrays.toString(test3));
        System.out.println("1st smallest: " + select(test3, 1));
        System.out.println();

        // Test 4 - Already sorted
        int[] test4 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        System.out.println("Test 4 - Array: " + java.util.Arrays.toString(test4));
        System.out.println("5th smallest: " + select(test4, 5));
        System.out.println("Median: " + findMedian(test4));
        System.out.println();

        // Verify with sorted arrays
        System.out.println("=== Verification ===");
        int[] verify1 = test1.clone();
        java.util.Arrays.sort(verify1);
        System.out.println("Test 1 sorted: " + java.util.Arrays.toString(verify1));
        System.out.println("Expected 5th: " + verify1[4] + ", Got: " + select(test1.clone(), 5));
    }
}

