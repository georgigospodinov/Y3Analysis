package main;

import util.DataWriter;
import util.Logger;

import static main.DataGenerator.genData;
import static main.DataGenerator.letJVMLoad;
import static util.CommonlyUsed.print;
import static util.Logger.log;

public class Main {

    private static final int METHOD_NAME_INDEX = 0;
    private static final int FILENAME_INDEX = 1;
    private static final int NUMBER_OF_DATA_POINTS_INDEX = 2;
    private static final int NUMBER_OF_RUNS_INDEX = 3;
    private static final int START_SIZE_INDEX = 4;
    private static final int DENSITY_INDEX = 5;

    private static String methodToTest;
    private static String filename;
    private static int numberOfDataPoints;
    private static int numberOfRuns;
    private static int startSize;
    private static Double density = null;
    private static boolean error = false;

    private static int wrappedParse(String s) {
        try {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            print(e.getMessage());
            error = true;
            log(e);
        }
        return -1;
    }

    private static void parseInput(String[] args) {
        methodToTest = args[METHOD_NAME_INDEX];
        filename = args[FILENAME_INDEX];
        numberOfDataPoints = wrappedParse(args[NUMBER_OF_DATA_POINTS_INDEX]);
        numberOfRuns = wrappedParse(args[NUMBER_OF_RUNS_INDEX]);
        startSize = wrappedParse(args[START_SIZE_INDEX]);

        try {
            density = Double.parseDouble(args[DENSITY_INDEX]);
        }
        catch (NumberFormatException e) {
            print(e.getMessage());
            error = true;
            log(e);
        }
    }

    public static void main(String[] args) {
        if (args.length != 6) {
            print("Not enough arguments!");
            print("USage: java main.Main <method to test> <filename> <number of data points> <number of runs> <start size> [<density>]");
            return;
        }

        Logger.open("log.txt");
        parseInput(args);
        if (error) {
            print("Could not parse arguments. See log for extra details!");
            return;
        }

        print("Loading JVM...");
        letJVMLoad();
        print("method=" + methodToTest, "filename=" + filename,
                "numberOfDataPoints=" + numberOfDataPoints, "numberOfRuns=" + numberOfRuns,
                "startSize=" + startSize, "density=" + density);
        DataWriter.open(filename);
        genData(methodToTest, numberOfDataPoints, numberOfRuns, startSize, density);
        DataWriter.close();

        print("\nClosing logger...");
        Logger.close();
    }
}