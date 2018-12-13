package io.pavelkoch.tsp;

import java.nio.file.Paths;
import java.util.List;

public class Main {

	/**
	 * The input file name.
	 */
	private final static String INPUT_FILE = "old-train-3.txt";

	/**
	 * The algorithm start time.
	 */
	public final static double START_TIME = System.nanoTime();

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
		new Salesman(cities).travel();
	}
}
