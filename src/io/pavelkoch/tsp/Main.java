package io.pavelkoch.tsp;

import java.nio.file.Paths;
import java.util.List;

public class Main {

	/**
	 * The input file name.
	 */
	private final static String INPUT_FILE = "train-1.txt";

	/**
	 * The application entry point.
	 *
	 * @param args Console arguments
	 * @throws Exception Whenever something goes wrong
	 */
	public static void main(String[] args) throws Exception {
		// Read the list of cities.
		List<City> cities = new InputFileReader().read(Paths.get(INPUT_FILE));

		// Start the timer.
		double startTime = System.nanoTime();

		// Fire the algorithm.
		double path = new Salesman(cities).travel();

		// Report results.
		System.out.println(String.format(
				"Found a path with [%.3f] length in [%.2f]ms.",
				path, (System.nanoTime() - startTime) / 1000000
		));
	}
}
