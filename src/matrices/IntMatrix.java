package matrices;

import java.util.Random;

/**
 * Represents a Matrix of Integers.
 * Also contains the basic multiplication method.
 *
 * @author Susmit Sarkar
 * @version 1.0
 */
public class IntMatrix {

    private int[][] values;

    /**
     * Creates a square matrix of the given dimension.
     * Fills it with random number according to the given density.
     *
     * @param dim the dimension of the matrix
     * @param density the density of the matrix (a value between 0 and 1 indicating the probability of a non-zero value)
     * @return the generated matrix
     */
    public static IntMatrix createRandom(int dim, double density) {
        IntMatrix m = new IntMatrix(dim);
        Random r = new Random();
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++)
                m.set(i, j, r.nextDouble() >= density ? 0 : r.nextInt());
        return m;
    }

    /** Performs the basic multiplication of the given matrices. */
    public static IntMatrix basicMultiplication(IntMatrix a, IntMatrix b) {
        int dim = a.getDim();
        IntMatrix result = new IntMatrix(dim);

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                int sum = 0;
                for (int k = 0; k < dim; k++) {
                    sum += a.get(i, k) * b.get(k, j);
                }
                result.set(i, j, sum);
            }
        }

        return result;
    }

    /**
     * This is an implementation of matrix multiplication that is similar to the 'basic' one.
     * It is, however, less naive, in that it does not multiply zeros by whole rows.
     *
     */
    public static IntMatrix minorOptimizationMultiplication(IntMatrix a, IntMatrix b) {
        int dim = a.getDim();
        IntMatrix result = new IntMatrix(dim);

        for (int i = 0; i < dim; i++) {
            for (int k = 0; k < dim; k++) {
                // Skip zeros.
                if (a.get(i, k) == 0) continue;

                for (int j = 0; j < dim; j++)
                    result.values[i][j] += a.get(i, k) * b.get(k, j);
            }
        }

        return result;
    }

    /** Creates a square matrix with the given dimension. */
    IntMatrix(int dim) {
        this.values = new int[dim][dim];
    }

    /** Returns the dimension of the matrix. */
    private int getDim() {
        return values.length;
    }

    /** Update the value stored at the specified location. */
    void set(int i, int j, int val) {
        values[i][j] = val;
    }

    /** Get the value stored at the specified location. */
    int get(int i, int j) {
        return values[i][j];
    }

    /** A string representation of the matrix. */
    public String toString() {
        int dim = values.length;
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                b.append(values[i][j]);
                b.append(" ");
            }
            b.append("\n");
        }
        return b.toString();
    }
}
