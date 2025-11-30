package concurrency.forkjoin.recursivetask;

import java.util.concurrent.RecursiveTask;

public class Sum extends RecursiveTask<Double> {
    final int seqThreshold = 500;

    double[] data;

    int start, end;
    public Sum(double[] data, int start, int end) {
        this.data = data;
        this.start = start;
        this.end = end;
    }

    protected Double compute() {
        double sum = 0;

        if ((end - start) < seqThreshold) {
            for (int i = start; i < end; i++) {
                sum += data[i];
            }
        } else {
            int middle = (start + end) / 2;

            Sum subTaskA = new Sum(data, start, middle);
            Sum subTaskB = new Sum(data, middle, end);

            // Start each subtask by forking (runs async).
            subTaskA.fork();
            subTaskB.fork();

            // wait for the subtasks to return, and aggregate the results.
            sum = subTaskA.join() + subTaskB.join();
        }
        return sum;
    }
}
