package matrices;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * A Compressed Row Storage (CRS) implementation of a sparse square matrix.
 *  http://www.mathcs.emory.edu/~cheung/Courses/561/Syllabus/3-C/sparse.html
 * @author 150009974
 * @version 1.0
 */
public class SparseMatrix {

    private static final Random r = new Random();

    public static SparseMatrix createRandomSparse(int n, double density) {
        SparseMatrix m = new SparseMatrix(n, density);
        int valuesInserted = 0;
        int valuesRemaining;
        int spaceRemaining;
        int value;
        Set<Integer> colIDsInCurrentRow;
        for_each_row:
        for (int i = 0; i < n; i++) {
            // Set this row's start index.
            m.rowStarts[i] = valuesInserted;
            colIDsInCurrentRow = new HashSet<>();
            do {
                // For each element in the row:
                for (int j = 0; j < n; j++) {

                    // Skip indexes that already contain values.
                    if (colIDsInCurrentRow.contains(j)) continue;

                    // Random chance that there is a value here.
                    double prob = r.nextDouble();
                    if (prob > m.density) continue;

                    // Random non-zero value.
                    do value = r.nextInt(5);
                    while (value == 0);

                    // Add the value and mark the index.
                    m.colIds[valuesInserted] = j;
                    colIDsInCurrentRow.add(j);
                    m.values[valuesInserted] = value;
                    valuesInserted++;

                    // Are we done?
                    if (valuesInserted == m.numberOfValues) break for_each_row;
                }

                /*
                 * If there are more values to put than there are spaces below the current row,
                 * repeat the loop.
                 */
                valuesRemaining = m.numberOfValues - valuesInserted;
                spaceRemaining = n * (n - i - 1);
            }
            while (valuesRemaining > spaceRemaining);
        }

        /*
         * If all the values were put before the end of the rows was reached,
         * mark those rows as empty (starting at an index after the range).
         */
        for (int i = n - 1; i > 0; i--) {
            if (m.rowStarts[i] > m.rowStarts[i-1]) break;
            m.rowStarts[i] = m.numberOfValues;
        }

        return m;
    }

    private static void multiplyValueByRow(SparseMatrix a, SparseMatrix b, IntMatrix product,
                                           int valIndInA, int rowIndInA) {
        int rowIndInB = a.colIds[valIndInA];
        int limit;
        // When the last value is at row index of dimension-1.
        if (rowIndInB +1 >= b.dimension)
            limit = b.numberOfValues;
        else limit = b.rowStarts[rowIndInB + 1];

        for (int valueIndInB = b.rowStarts[rowIndInB]; valueIndInB < limit; valueIndInB++) {
            int current = product.get(rowIndInA, b.colIds[valueIndInB]);
            int newValue = current + a.values[valIndInA] * b.values[valueIndInB];
            product.set(rowIndInA, b.colIds[valueIndInB], newValue);
        }
    }

    public static IntMatrix multiply(SparseMatrix a, SparseMatrix b) {
        IntMatrix product = new IntMatrix(a.dimension);
        if (a.dimension == 0) return product;
        for (int rowIndex = 0; rowIndex < a.dimension - 1; rowIndex++)
            for (int valIndex = a.rowStarts[rowIndex]; valIndex < a.rowStarts[rowIndex + 1]; valIndex++)
                multiplyValueByRow(a, b, product, valIndex, rowIndex);
        for (int valIndInA = a.rowStarts[a.dimension - 1]; valIndInA < a.numberOfValues; valIndInA++)
            multiplyValueByRow(a, b, product, valIndInA, a.dimension - 1);

        // For this practical, we don't care about the result, so simple ignore returned value.
        return product;  // Otherwise, use a method to convert the product into a SparseMatrix.
    }

    private int dimension;
    private double density;
    private int numberOfValues;
    private int[] rowStarts;
    private int[] colIds;
    private int[] values;

    private SparseMatrix(int n, double density) {
        this.dimension = n;
        this.density = density;
        this.numberOfValues = (int) Math.floor(density * n * n);
        this.rowStarts = new int[this.dimension];
        this.colIds = new int[this.numberOfValues];
        this.values = new int[this.numberOfValues];
    }

    @Override
    public String toString() {
        return "SparseMatrix{" +
                "\n\tdimension=" + dimension +
                ",\n\tdensity=" + density +
                ",\n\tnumberOfValues=" + numberOfValues +
                ",\n\trowStarts=" + Arrays.toString(rowStarts) +
                ",\n\tcolIds=" + Arrays.toString(colIds) +
                ",\n\tvalues=" + Arrays.toString(values) +
                "\n}";
    }

}
