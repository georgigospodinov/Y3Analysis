package main;

import matrices.IntMatrix;
import matrices.SparseMatrix;

import static java.lang.System.nanoTime;
import static matrices.IntMatrix.*;
import static matrices.SparseMatrix.createRandomSparse;
import static util.DataWriter.writeDP;
import static util.DataWriter.writeFirstDP;
import static util.ProgressBar.formatBar;

class DataGenerator {

    /**
     * Measures a single run by creating and multiplying two matrices.
     * Uses the basic multiplication.
     *
     * @param dim     the dimension of the matrices
     * @param density the density of the matrices
     * @return the time it took to perform the multiplication
     */
    private static long singleBasic(int dim, double density) {
        IntMatrix a = createRandom(dim, density), b = createRandom(dim, density);
        long start = nanoTime();
        basicMultiplication(a, b);
        long end = nanoTime();
        return end - start;
    }

    /**
     * Measures a single run by creating and multiplying two matrices.
     * Uses the minor optimized basic multiplication.
     *
     * @param dim     the dimension of the matrices
     * @param density the density of the matrices
     * @return the time it took to perform the multiplication
     */
    private static long singleOptim(int dim, double density) {
        IntMatrix a = createRandom(dim, density), b = createRandom(dim, density);
        long start = nanoTime();
        minorOptimizationMultiplication(a, b);
        long end = nanoTime();
        return end - start;
    }

    /**
     * Measures a single run by creating and multiplying two matrices.
     * Uses the sparse multiplication.
     *
     * @param dim     the dimension of the matrices
     * @param density the density of the matrices
     * @return the time it took to perform the multiplication
     */
    private static long singleSparse(int dim, double density) {
        SparseMatrix a = createRandomSparse(dim, density), b = createRandomSparse(dim, density);
        long start = nanoTime();
        SparseMatrix.multiply(a, b);
        long end = nanoTime();
        return end - start;
    }

    /**
     * Generates a single data point by making multiple runs and averaging the results.
     *
     * @param methodName   the name of the multiplication method to test
     * @param numberOfRuns the number of single runs to make
     * @param size         the dimension of the matrices to create and multiply
     * @param density      the density of the matrices
     * @return the average amount of time taken to perform the multiplication
     * @see DataGenerator#singleBasic(int, double)
     * @see DataGenerator#singleOptim(int, double)
     * @see DataGenerator#singleSparse(int, double)
     */
    private static double genDataPoint(String methodName, int numberOfRuns, int size, double density) {
        long total = 0;
        long time = 0;
        for (int i = 0; i < numberOfRuns; i++) {
            switch (methodName) {
                case "basic":
                case "BM": {
                    time = singleBasic(size, density);
                    break;
                }
                case "minor":
                case "optim":
                case "ON": {
                    time = singleOptim(size, density);
                    break;
                }
                case "sparse":
                case "CSR": {
                    time = singleSparse(size, density);
                    break;
                }
            }
            total += time;
        }
        return ((double) total) / numberOfRuns;
    }

    /**
     * Generates a data set to be put in a line chart.
     * Each point is a combination of input size and average multiplication time.
     * The input size for each point is the product of
     * the startSize and the index of the data point (counting from 1).
     * Thus each 2D point has an x-value that is a multiple of the first data point.
     * This allows tracking the time complexity of a single run.
     *
     * @param startSize          the size of the input for the first data point
     * @param numberOfDataPoints the total number of data points to create
     * @param numberOfRuns       the number of runs to make before calculating an average
     */
    static void genData(String methodName, int numberOfDataPoints, int numberOfRuns, int startSize, double density) {
        System.out.print(formatBar(0, numberOfDataPoints));

        double first = genDataPoint(methodName, numberOfRuns, startSize, density);
        writeFirstDP(first, startSize);
        System.out.print(formatBar(1, numberOfDataPoints));

        for (int i = 1; i < numberOfDataPoints; i++) {
            int size = startSize * (i + 1);
            double dp = genDataPoint(methodName, numberOfRuns, size, density);
            writeDP(dp, size);
            System.out.print(formatBar(i + 1, numberOfDataPoints));
        }

    }

    /** Forces a few calculations to be made and their results discarded while the JVM boots up. */
    static void letJVMLoad() {
        singleBasic(500, 1);
    }

}
