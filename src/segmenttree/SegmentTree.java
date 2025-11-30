package segmenttree;

/**
 * Segment Tree implementation (1-indexed) to track range sums.
 */
public class SegmentTree {

    private int[] st;  // segment tree array (1-indexed)
    private int n;     // size of input array

    public SegmentTree(int[] arr) {
        n = arr.length;
        st = new int[4 * n + 1]; // +1 because 1-indexed
        build(arr, 1, 0, n - 1); // root p = 1
    }

    /** Returns the left child index of p */
    private int left(int p) {
        return 2 * p;
    }

    /** Returns the right child index of p */
    private int right(int p) {
        return 2 * p + 1;
    }

    /** Recursively builds the segment tree */
    private void build(int[] arr, int p, int l, int r) {
        if (l == r) {
            st[p] = arr[l]; // leaf node
        } else {
            int mid = (l + r) / 2;
            build(arr, left(p), l, mid);
            build(arr, right(p), mid + 1, r);
            st[p] = st[left(p)] + st[right(p)]; // sum of children
        }
    }

    /** Public query for sum of range [i, j] */
    public int query(int i, int j) {
        return query(1, 0, n - 1, i, j); // root = 1
    }

    /** Recursive query function */
    private int query(int p, int l, int r, int i, int j) {
        if (j < l || i > r) return 0;       // no overlap
        if (i <= l && r <= j) return st[p]; // total overlap
        int mid = (l + r) / 2;
        int leftSum = query(left(p), l, mid, i, j);
        int rightSum = query(right(p), mid + 1, r, i, j);
        return leftSum + rightSum;          // partial overlap
    }

    /** Public update function: sets arr[idx] = val */
    public void update(int idx, int val) {
        update(1, 0, n - 1, idx, val); // root = 1
    }

    /** Recursive update function */
    private void update(int p, int l, int r, int idx, int val) {
        if (l == r) {
            st[p] = val; // leaf node
        } else {
            int mid = (l + r) / 2;
            if (idx <= mid) {
                update(left(p), l, mid, idx, val);
            } else {
                update(right(p), mid + 1, r, idx, val);
            }
            st[p] = st[left(p)] + st[right(p)]; // update parent
        }
    }

    /** Example usage */
    public static void main(String[] args) {
        int[] arr = {2, 4, 5, 5, 6, 6, 6, 7, 7, 8, 9};
        SegmentTree st = new SegmentTree(arr);

        System.out.println("Sum of range [0, 4] = " + st.query(0, 4)); // 22
        System.out.println("Sum of range [3, 7] = " + st.query(3, 7)); // 30

        System.out.println("\nUpdating index 4 to 10...");
        st.update(4, 10);

        System.out.println("Sum of range [0, 4] = " + st.query(0, 4)); // 26
        System.out.println("Sum of range [3, 7] = " + st.query(3, 7)); // 34
    }
}