package mpd;

public class ThreadedMinimumPairwiseDistance implements MinimumPairwiseDistance {

    private static long finalResult;

    // Some getters/setters for the finalResult.
    // They are synchronized to avoid race conditions
    public static synchronized void setFinalResult(long result) { finalResult = result;}
    public static synchronized long getFinalResult() { return finalResult;}

    @Override
    public long minimumPairwiseDistance(int[] values) {

        // Initially, the result should be set positive infinity (or as close as we can get).
        finalResult = Integer.MAX_VALUE;

        // A value used to define the ranges for each thread to search
        int N = values.length;

        // Now we define the four threads, each with a custom range to search.
        //========================================================================//
        Thread bottomRight = new Thread() {
            long result = Integer.MAX_VALUE;
            public void run() {
                for (int i = N / 2; i < N; ++i) {
                    for (int j = 0; j + N / 2 < i; ++j) {
                        long diff = Math.abs((long) values[i] - (long) values[j]);
                        if (diff < result) {
                            result = diff;
                        }
                    }
                }
                System.out.println("br: " + result);
                if (getFinalResult() > result) {
                    setFinalResult(result);
                }
            }
        };

        Thread bottomLeft = new Thread() {
            long result = Integer.MAX_VALUE;
            public void run() {
                for (int i = 0; i < N/2; ++i) {
                    for (int j = 0; j < i; ++j) {
                        long diff = Math.abs((long) values[i] - (long) values[j]);
                        if (diff < result) {
                            result = diff;
                        }
                    }
                }
                System.out.println("bl: " + result);
                if (getFinalResult() > result) {
                    setFinalResult(result);
                }
            }
        };

        Thread topRight = new Thread() {
            long result = Integer.MAX_VALUE;
            public void run() {
                for (int i = N/2; i < N; ++i) {
                    for (int j = N/2; j < i; ++j) {
                        long diff = Math.abs((long) values[i] - (long) values[j]);
                        if (diff < result) {
                            result = diff;
                        }
                    }
                }
                System.out.println("tr: " + result);
                if (getFinalResult() > result) {
                    setFinalResult(result);
                }
            }
        };

        Thread center = new Thread() {
            long result = Integer.MAX_VALUE;
            public void run() {
                for (int j = 0; j + N/2 < N; ++j) {
                    for (int i = N/2; i < j+N/2; ++i) {
                        long diff = Math.abs((long) values[i] - (long) values[j]);
                        if (diff < result) {
                            result = diff;
                        }
                    }
                }
                System.out.println("cen: " + result);
                if (getFinalResult() > result) {
                    setFinalResult(result);
                }
            }
        };
        //============================================================================//

        // Now collect the threads, start each one, and join each one.
        Thread[] threads = new Thread[] {bottomLeft, bottomRight, topRight, center};
        startAndJoin(threads);

        // Once the threads are done, print the minimum distance found.
        System.out.println("The minimum distance is: " + finalResult);
        return finalResult;
    }

    // This is a method used to start each thread, and then join them.
    public void startAndJoin(Thread[] threads) {
        try {
            for (Thread t : threads) {
                t.start();
            }
            for (Thread t: threads) {
                t.join();
            }
        } catch (InterruptedException intEx) {
            intEx.printStackTrace();
        }
    }






}
