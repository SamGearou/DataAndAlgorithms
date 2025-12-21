package quickselect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementation of QuickSelect algorithm to find k closest points to origin.
 * QuickSelect is a selection algorithm that finds the k-th smallest element
 * in an unordered list. It uses partitioning similar to QuickSort but only
 * recurses on one side of the partition, making it more efficient for selection.
 * <p>
 * Time Complexity: O(n) average case, O(n²) worst case
 * Space Complexity: O(1) auxiliary space (excluding input)
 */
public class Select {

    private final Random random = new Random();

    /**
     * Finds the k closest points to the origin (0,0) from a list of points.
     * Uses QuickSelect to efficiently find the k smallest elements by distance.
     *
     * @param points List of points to search through
     * @param k      Number of closest points to return
     * @return List containing the k closest points to origin, unsorted by distance
     */
    public List<Point> kClosestToOrigin(List<Point> points, int k) {
        quickSelect(points, k, 0, points.size() - 1);

        return points.subList(0, k);
    }

    /**
     * Recursive QuickSelect implementation to find the k-th smallest element.
     * Partitions the array around a random pivot and only recurses on the side
     * that contains the k-th element, making it efficient for selection.
     *
     * @param points List of points being processed
     * @param k      Index (0-based) of the element to find (k-th smallest)
     * @param left   Left boundary of current subarray
     * @param right  Right boundary of current subarray
     */
    private void quickSelect(List<Point> points, int k, int left, int right) {
        if (left >= right) {
            return; // Base case: single element or invalid range
        }

        int pivot = left + random.nextInt(right - left + 1);

        int pivotIndex = partition(points, pivot, left, right);

        if (k < pivotIndex) {
            quickSelect(points, k, left, pivotIndex - 1);
        } else if (k > pivotIndex) {
            quickSelect(points, k, pivotIndex + 1, right);
        }
        // If k == pivotIndex, we're done - the k-th element is in its final position
    }

    /**
     * Partitions the array around a pivot element based on distance from origin.
     * Elements smaller than or equal to pivot distance go to the left,
     * elements larger go to the right. Uses Lomuto partition scheme.
     *
     * @param points List of points to partition
     * @param pivot  Index of the pivot element
     * @param left   Left boundary of partition range
     * @param right  Right boundary of partition range
     * @return Final position of the pivot element after partitioning
     */
    private int partition(List<Point> points, int pivot, int left, int right) {
        // Move pivot to the end for easier partitioning
        swap(points, pivot, right);

        // Partition: elements <= pivot distance go to the left
        int currIndex = left;
        for (int i = left; i < right; i++) {
            if (points.get(i).dist() <= points.get(right).dist()) {
                swap(points, currIndex, i);
                currIndex++;
            }
        }
        // Move pivot to its final position
        swap(points, currIndex, right);

        return currIndex;
    }

    /**
     * Swaps two elements in the points list.
     *
     * @param points List containing elements to swap
     * @param i      Index of first element
     * @param j      Index of second element
     */
    private void swap(List<Point> points, int i, int j) {
        Point temp = points.get(i);
        points.set(i, points.get(j));
        points.set(j, temp);
    }

    /**
     * Represents a 2D point with integer coordinates.
     * Provides distance calculation from origin using squared Euclidean distance
     * (avoids floating point operations for better performance).
     */
    static class Point {
        final int x;
        final int y;

        /**
         * Constructs a point with given coordinates.
         *
         * @param x X-coordinate
         * @param y Y-coordinate
         */
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Calculates the squared Euclidean distance from origin (0,0).
         * Returns x² + y² instead of sqrt(x² + y²) to avoid floating point
         * operations while maintaining correct ordering for distance comparisons.
         *
         * @return Squared distance from origin
         */
        public int dist() {
            return (x * x) + (y * y);
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }

    /**
     * Demo method showing how to use the QuickSelect implementation.
     * Creates sample points and finds the 3 closest to origin.
     * Expected output: (1,1), (1,4), (2,5) - the 3 points with smallest distances.
     */
    public static void main(String[] args) {
        List<Point> points = new ArrayList<>();
        // Points with their squared distances from origin:
        points.add(new Point(8, 9)); // 145 (8² + 9²)
        points.add(new Point(1, 4)); // 17 (1² + 4²)
        points.add(new Point(2, 5)); // 29 (2² + 5²)
        points.add(new Point(7, 8)); // 113 (7² + 8² = 49 + 64)
        points.add(new Point(1, 1)); // 2 (1² + 1²)

        Select selector = new Select();
        List<Point> closestPoints = selector.kClosestToOrigin(points, 3);

        System.out.println("3 closest points to origin:");
        for (Point p : closestPoints) {
            System.out.println(p + " (distance squared: " + p.dist() + ")");
        }
    }
}
