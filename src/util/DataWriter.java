package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static util.CommonlyUsed.NEW_LINE;
import static util.CommonlyUsed.SEPARATOR;
import static util.Logger.log;

/**
 * This class writes the data to a file.
 * A static field saves the value of the first data point for comparison purposes.
 *
 * @version 1.0
 */
public class DataWriter {

    private static final String HEADER = "matrix size" + SEPARATOR + "time to multiply" + SEPARATOR + "compared to first" + NEW_LINE;

    private static BufferedWriter writer;
    /**
     * Saves the value of the first data point.
     * If this is not null, then there has a been a data point already.
     * Its value is used to explain the relations of the data points.
     */
    private static Double firstDP = null;

    public static void open(String filename) {
        try {
            writer = new BufferedWriter(new FileWriter(filename));
        }
        catch (IOException e) {
            log(e);
        }
    }

    /**
     * Writes the header and
     * calls {@link DataWriter#writeDP(double, int)} to write the first data point to the file.
     *
     * @param dp   the data point to write
     * @param size the size of the matrix for this data point
     * @see DataWriter#writeDP(double, int)
     */
    public static void writeFirstDP(double dp, int size) {
        firstDP = dp;
        try {
            writer.write(HEADER);
            writer.flush();
        }
        catch (IOException e) {
            log(e);
        }

        writeDP(dp, size);
    }

    /**
     * Writes a single data point to the file.
     * The format is: matrix size,data point,ration between this data point and first data point.
     *
     * @param dp   the data point to write
     * @param size the size of the matrix for this data point
     * @see DataWriter#firstDP
     */
    public static synchronized void writeDP(double dp, int size) {
        String formatted = String.format("%d%s%.5f%s%.2f%s", size, SEPARATOR, dp, SEPARATOR, dp / firstDP, NEW_LINE);
        try {
            writer.write(formatted);
            writer.flush();
        }
        catch (IOException e) {
            log(e);
        }
    }

    public static void close() {
        try {
            writer.close();
        }
        catch (IOException e) {
            log(e);
        }
    }
}
