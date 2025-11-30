package unionfind;

public class UnionFind {

    // p[i] stores the parent of i.
    // If p[i] == i, then i is a root (representative of its set).
    int[] p;

    // rank[i] stores an upper-bound heuristic of the tree height rooted at i.
    // Used to keep the structure shallow (union by rank).
    int[] rank;

    /**
     * Initializes a Union-Find structure with n elements: 0, 1, ..., n-1.
     * Each element starts in its own set (self parent).
     */
    public UnionFind(int n) {
        p = new int[n];
        rank = new int[n];

        for (int i = 0; i < n; i++) {
            p[i] = i;      // each element is its own parent initially
            rank[i] = 0;   // rank starts at 0
        }
    }

    /**
     * Finds the representative (root) of the set containing i.
     * Implements path compression: flattens the tree for efficiency.
     */
    public int findSet(int i) {
        return (p[i] == i) ? i : (p[i] = findSet(p[i]));
    }

    /**
     * Checks whether elements i and j belong to the same set.
     */
    public boolean isSameSet(int i, int j) {
        return findSet(i) == findSet(j);
    }

    /**
     * Unites the sets containing elements i and j.
     * Uses union by rank to attach the smaller tree under the larger one.
     */
    public void union(int i, int j) {
        if (!isSameSet(i, j)) {
            int x = findSet(i); // root of i
            int y = findSet(j); // root of j

            // attach smaller rank tree under larger rank tree
            if (rank[x] > rank[y]) {
                p[y] = x;
            } else {
                p[x] = y;
                if (rank[x] == rank[y]) {
                    rank[y]++; // increase rank if trees had equal height
                }
            }
        }
    }

    /**
     * Counts how many disjoint sets currently exist.
     * A root (representative) is where p[i] == i.
     */
    public int numDisjointSets() {
        int count = 0;
        for (int i = 0; i < p.length; i++) {
            if (findSet(i) == i) {
                count++;
            }
        }
        return count;
    }

    /**
     * Example usage of the Union-Find data structure.
     */
    public static void main(String[] args) {
        // Create a Union-Find with 7 elements: 0–6
        UnionFind uf = new UnionFind(7);

        // Initial: 7 disjoint sets
        System.out.println("Initial number of sets: " + uf.numDisjointSets());

        // Union some pairs
        uf.union(0, 1);
        uf.union(1, 2);
        uf.union(3, 4);

        System.out.println("After unions:");
        System.out.println("Set count = " + uf.numDisjointSets()); // expect 4

        // Check connections
        System.out.println("0 and 2 same set?  " + uf.isSameSet(0, 2)); // true
        System.out.println("0 and 3 same set?  " + uf.isSameSet(0, 3)); // false

        // More unions
        uf.union(2, 4); // merges {0,1,2} with {3,4}

        System.out.println("After union(2,4):");
        System.out.println("Set count = " + uf.numDisjointSets()); // expect 3

        // Final checks
        System.out.println("2 and 4 same set?  " + uf.isSameSet(2, 4)); // true
        System.out.println("3 and 0 same set?  " + uf.isSameSet(3, 0)); // true
    }
}