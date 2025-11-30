package concurrency.forkjoin.recursiveaction;

import java.util.concurrent.ForkJoinPool;

public class ForkJoinDemo {
    public static void main(String[] args) {
        ForkJoinPool fjp = new ForkJoinPool();

        double[] nums = new double[100_000];

        for (int i = 0; i < nums.length; i++) {
            nums[i] = i;
        }

        System.out.println("A portionn of the original sequence:");
        for (int i = 0; i < 10; i++) {
            System.out.print(nums[i] + " ");
        }
        System.out.println("\n");
        SqrtTransform task  = new SqrtTransform(nums, 0, nums.length);

        // Start the main ForkJoinTask.
        fjp.invoke(task);

        System.out.println("A portion of the transformed sequence" + "(to four decimal places)");
        for (int i = 0; i < 10; i++) {
            System.out.format("%.4f ", nums[i]);
        }
        System.out.println();
    }
}
