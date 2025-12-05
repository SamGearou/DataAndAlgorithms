package matrices;

/**
 * Paper: http://www.cse.yorku.ca/~andy/pubs/X+Y.pdf
 */
public class MatrixRank {

    /**
     * Computes rank⁻(A, a) for an ordered matrix A. rank⁻(A, a) is the number of elements in matrix A that are less than a.
     * A must be n×n, rows nonincreasing, columns nondecreasing.
     *
     * @param A the matrix
     * @param a the threshold value
     * @return number of entries in A that are < a
     * This method runs in O(n) time for an nxn matrix
     */
    public static int rankMinus(int[][] A, int a) {
        int n = A.length;
        int j = 0;     // Java uses 0-based indexing
        int x = 0;

        for (int i = 0; i < n; i++) {
            while (j < n && A[i][j] >= a) {
                j++;
            }
            x += (n - j);
        }

        return x;
    }

    /**
     * Computes rank⁺(A, a): number of entries > a. rank⁺(A, a) is the number of elements in matrix A that are greater than a.
     * This is symmetric to rankMinus, but scanning from the RIGHT
     * since rows are nonincreasing.
     *
     * @param A the matrix
     * @param a the threshold value
     * @return number of entries in A that are > a
     * This method runs in O(n) time for an nxn matrix
     */
    public static int rankPlus(int[][] A, int a) {
        int n = A.length;
        int j = n-1;     // Java uses 0-based indexing
        int x = 0;

        for (int i = n-1; i >= 0; i--) {
            while (j >= 0 && A[i][j] <= a) {
                j--;
            }
            x += (j+1);
        }

        return x;
    }

    // Example usage
    public static void main(String[] args) {
        int[][] A = {
                {21, 16, 11, 6, 1},
                {22, 17, 12, 7, 2},
                {23, 18, 13, 8, 3},
                {24, 19, 14, 9, 4},
                {25, 20, 15, 10, 5}
        };

        int result = rankMinus(A, 15);
        System.out.println("rankMinus(A, 15) = " + result);
        result = rankPlus(A, 15);
        System.out.println("rankPlus(A, 15) = " + result);
    }
}

