package mpd;

import java.io.IOException;

public class ThreadedMinimumPairwiseDistance implements MinimumPairwiseDistance {

    private long finalResult;

    public void setFinalResult(long result) { finalResult = result;}
    public long getFinalResult() { return finalResult;}

    @Override
    public long minimumPairwiseDistance(int[] values) {

        // Initially, the result should be set positive infinity (or as close as we can get).
        long finalResult = Integer.MAX_VALUE;
        int N = values.length;

        // REMOVE THIS WHEN DONE:
        // The threads need to be divided into 4 ranges:
        // 0 <= j < i < N/2            ...
        // N/2 <= (j + N/2) < i < N
        // N/2 <= j < i < N
        // N/2 <= i <= (j + N/2) < N

        // Each thread finds the minimum for it's assigned range.
        // The thread updates a global variable WHEN THE THREAD IS FINISHED.
        // Wait for each thread to finish
        // Report the resulting global minimum.


        Thread bottomRight = new Thread() {
            long result = Integer.MAX_VALUE;
            public void run() {
                for (int i = N / 2; i < N; ++i) {
                    for (int j = 0; j + N / 2 < i; ++j) {
                        // Gives us all the pairs (i, j) where 0 <= j < i < values.length
                        long diff = Math.abs(values[i] - values[j]);
                        if (diff < result) {
                            result = diff;
                        }
                    }
                }
                if (getFinalResult() > result) {
                    setFinalResult(result);
                }
            }
        };
        bottomRight.start();

        Thread bottomLeft = new Thread() {
            long result = Integer.MAX_VALUE;
            public void run() {
                for (int i = 0; i < N/2; ++i) {
                    for (int j = 0; j < i; ++j) {
                        // Gives us all the pairs (i, j) where 0 <= j < i < values.length
                        long diff = Math.abs(values[i] - values[j]);
                        if (diff < result) {
                            result = diff;
                        }
                    }
                }
                if (getFinalResult() > result) {
                    setFinalResult(result);
                }
            }
        };
        bottomLeft.start();


//            for (int i = N/2; i < N; ++i) {
//                for (int j = N/2; j < i; ++j) {
        Thread topRight = new Thread() {
            long result = Integer.MAX_VALUE;
            public void run() {
                for (int i = N/2; i < N; ++i) {
                    for (int j = N/2; j < i; ++j) {
                        // Gives us all the pairs (i, j) where 0 <= j < i < values.length
                        long diff = Math.abs(values[i] - values[j]);
                        if (diff < result) {
                            result = diff;
                        }
                    }
                }
                if (getFinalResult() > result) {
                    setFinalResult(result);
                }
            }
        };
        topRight.start();


//            for (int j = 0; j + N/2 < N; ++j) {
//                for (int i = N/2; i < j; ++i) {
        Thread center = new Thread() {
            long result = Integer.MAX_VALUE;
            public void run() {
                for (int j = 0; j + N/2 < N; ++j) {
                    for (int i = N/2; i < j; ++i) {
                        // Gives us all the pairs (i, j) where 0 <= j < i < values.length
                        long diff = Math.abs(values[i] - values[j]);
                        if (diff < result) {
                            result = diff;
                        }
                    }
                }
                if (getFinalResult() > result) {
                    setFinalResult(result);
                }
            }
        };
        center.start();

        try{
            bottomLeft.join();
            bottomRight.join();
            topRight.join();
            center.join();
        } catch (InterruptedException intEx) {
            intEx.printStackTrace();
        }

        System.out.println("The minimum distance is: " + finalResult);
        return finalResult;
    }

}
