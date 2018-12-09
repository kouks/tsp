package io.pavelkoch.tsp;

import java.nio.file.Paths;
import java.util.List;

public class Main {

	/**
	 * The input file name.
	 */
	private final static String INPUT_FILE = "test-4.txt";

    /**
     * The input file name.
     */
    public final static double START_TIME = System.nanoTime();

    /**
     * The input file name.
     */
    public final static double MAX_EXECUTION_MILIS = 55000.0;

	/**
	 * The application entry point.
	 *
	 * @param args Console arguments
	 * @throws Exception Whenever something goes wrong
	 */
	public static void main(String[] args) throws Exception {
		// Read the list of cities.
		List<City> cities = new InputFileReader().read(Paths.get(INPUT_FILE));

		// Fire the algorithm.
		double path = new Salesman(cities).travel();

		// Report results.
		System.out.println(String.format(
				"Found a path with [%.3f] length in [%.2f]ms.",
				path, (System.nanoTime() - START_TIME) / 1000000
		));
	}
}
