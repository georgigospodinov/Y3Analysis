package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static util.CommonlyUsed.NEW_LINE;
import static util.CommonlyUsed.print;

public class Logger {

    private static final int FLUSH_PERIOD = 5000;//ms

    private static BufferedWriter writer;

    /**
     * Used to control the flushing thread.
     *
     * @see Logger#periodicFlush()
     */
    private static boolean contentsUpdated = false;

    /**
     * Used to control the flushing thread.
     *
     * @see Logger#periodicFlush()
     */
    private static boolean running;

    public static void open(String filename) {
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            running = true;
            new Thread(Logger::periodicFlush).start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        try {
            writer.close();
            running = false;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(String line) {
        try {
            writer.write(line + NEW_LINE);
            contentsUpdated = true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(Exception e) {
        log(e.toString());
        StackTraceElement[] stack = e.getStackTrace();
        for (StackTraceElement element : stack)
            log("\t" + element.toString());
    }

    /**
     * Periodically flushes the log.
     * In case the program crashes before the writer was properly closed,
     * there might be some log to read.
     */
    private static void periodicFlush() {
        while (running) {
            try {
                Thread.sleep(FLUSH_PERIOD);
            }
            catch (InterruptedException ignored) {
            }

            if (!contentsUpdated) continue;
            try {
                writer.flush();
            }
            catch (IOException e) {
                print("Could not flush log.");
            }
            contentsUpdated = false;
        }
    }
}
