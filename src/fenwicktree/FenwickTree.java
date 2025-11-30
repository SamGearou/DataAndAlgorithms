package fenwicktree;

public class FenwickTree {

    // Fenwick tree array is 1-indexed.
    // ft[i] stores the cumulative frequency over a range whose size = LSOne(i).
    int[] ft;

    public FenwickTree(int n) {
        ft = new int[n + 1];
    }

    /**
     * Returns the cumulative frequency of all values <= b.
     */
    public int rsq(int b) {
        int sum = 0;
        while (b != 0) {
            sum += ft[b];
            b -= LSOne(b);
        }
        return sum;
    }

    /**
     * Returns the frequency count of values in range [i, j].
     */
    public int rsq(int i, int j) {
        return rsq(j) - (i == 1 ? 0 : rsq(i - 1));
    }

    /**
     * Increases the frequency of value k by v.
     * (Used to record occurrences of values.)
     */
    public void adjust(int k, int v) {
        while (k < ft.length) {
            ft[k] += v;
            k += LSOne(k);
        }
    }

    public int LSOne(int b) {
        return b & (-b);
    }

    /**
     * Demonstration using frequency interpretation:
     * Array values: [2,4,5,5,6,6,6,7,7,8,9]
     */
    public static void main(String[] args) {

        int[] arr = {2,4,5,5,6,6,6,7,7,8,9};

        FenwickTree ft = new FenwickTree(10);
        // we need max value = 9 → choose size ≥ 9

        // Insert frequencies: each arr[i] represents a value occurring once.
        for (int v : arr) {
            ft.adjust(v, 1);
        }

        System.out.println("Fenwick Tree Cumulative Frequency Test");
        System.out.println("----------------------------------------");

        // How many numbers ≤ 6?
        System.out.println("Count of values <= 6: " + ft.rsq(6));
        // Values ≤ 6 are: 2,4,5,5,6,6,6 → total = 7

        // How many are ≤ 5?
        System.out.println("Count of values <= 5: " + ft.rsq(5));
        // 2,4,5,5 → total = 4

        // How many values are in [6, 8]?
        System.out.println("Count in range [6,8]: " + ft.rsq(6, 8));
        // 6,6,6,7,7,8 → total = 6

        // Add another occurrence of value 5
        System.out.println("\nAdding one more value: adjust(5, +1)");
        ft.adjust(5, 1);

        // Now count ≤ 5 again
        System.out.println("Count of values <= 5: " + ft.rsq(5));
        // now 2,4,5,5,5 → total = 5
    }
}