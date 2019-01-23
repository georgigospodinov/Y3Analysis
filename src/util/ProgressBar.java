package util;

/**
 * Provides a progress bar that can be printed to indicate how much of the data has been generated.
 */
public class ProgressBar {

    /* Format:
    |--------------------| 100% (a/b)
     */

    private static final String PROGRESS_BAR_DASH = "-";
    private static final int PROGRESS_BAR_LENGTH = 20; // this is in dashes
    private static final double PROGRESS_PER_DASH = 100/PROGRESS_BAR_LENGTH;

    public static String formatBar(int workDone, int workTotal) {
        StringBuilder sb = new StringBuilder("\r|");
        double progress = workDone*100.0/workTotal;
        int numberOfDashes = (int) (progress/ PROGRESS_PER_DASH);
        int i;
        for (i = 0; i < numberOfDashes; i++) sb.append(PROGRESS_BAR_DASH);
        while (i < PROGRESS_BAR_LENGTH) {
            sb.append(" ");
            i++;
        }
        sb.append("|\t").append(String.format("%.2f",progress)).append("%\t");
        sb.append("(").append(workDone).append("/").append(workTotal).append(")");
        return sb.toString();
    }
}
