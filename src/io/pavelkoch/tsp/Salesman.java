package io.pavelkoch.tsp;

import java.util.*;

public class Salesman {

    private final List<City> cities;
    private final int size;
    // private double result = 76703.689;
    private double result = Double.MAX_VALUE;
    private static int ITERS = 0;
    private static int BEST_ITERS = 0;

    /**
     * Class constructor.
     *
     * @param cities The list of cities to travel through
     */
    public Salesman(List<City> cities) {
        this.cities = cities;
        this.size = cities.size();
    }

    /**
     * Entry point for the algorithm.
     */
    public void travel() {
        AdjacencyMatrix matrix = AdjacencyMatrix.fromCities(this.cities);
        Path path = Path.at(this.cities.get(0));
        double cost = matrix.reduceAndCost();

        try {
            // Recursively build the search space tree and modify the shortest path.
            this.searchNode(matrix, path, cost);
        } catch (InterruptedException ignored) {
            //
        }
    }

    /**
     * Recursive searchNode for building the search space tree.
     *
     * @param matrix The adjacency matrix
     * @param cost The current bound
     * @param path The current path
     */
    private void searchNode(AdjacencyMatrix matrix, Path path, double cost) throws InterruptedException {
        // If we reached the time limit, throw an exception to get our of the recursion.
        if ((System.nanoTime() - Main.START_TIME) / 1000000 > Main.MAX_EXECUTION_MILLIS) {
            System.out.println(String.format("Timeout :("));

            throw new InterruptedException();
        }

        // If we reached the leaf of the tree, assess the route and terminate this node.
        if (path.size() == this.size) {
            // Increment iterations counter.
            ITERS++;

            // If the cost is less than our current best, set the cost to our current best.
            if (cost < this.result) {
                // Increment best iterations counter.
                BEST_ITERS++;

                this.result = cost;

                // Output logging.
                System.out.println(String.format("Performed [%d its] at [%.0f it/s]", ITERS, ITERS / ((System.nanoTime() - Main.START_TIME) / 1000000000)));
                System.out.println(String.format("Performed [%d Bits]", BEST_ITERS));
                System.out.println(path);
                System.out.println(String.format(
                        "Found a path [%.3f units] long in [%.2f ms].",
                        this.result, (System.nanoTime() - Main.START_TIME) / 1000000
                ));
            }

            return;
        }

        // Save the current cost so that we can reset later.
        double previousCost = cost;

        // Mapping cost and matrix results for each subsequent node.
        Map<City, Double> costMap = new HashMap<>();
        Map<City, AdjacencyMatrix> matrixMap = new HashMap<>();

        for (City city : this.cities) {
            // If the city was already visited in the current search space, skip it.
            if (path.has(city)) {
                continue;
            }

            // Copy the current matrix, exclude the path being calculated and reduce it to get the additional cost. This
            // is in result our bounding function that allows us to prune nodes in the search space tree.
            AdjacencyMatrix copy = matrix.copy();
            copy.excludePath(path.get(path.size() - 1).getId(), city.getId());
            cost += matrix.getMatrix()[path.get(path.size() - 1).getId()][city.getId()] + copy.reduceAndCost();

            // Save the results.
            costMap.put(city, cost);
            matrixMap.put(city, copy);

            // Reset the cost variable.
            cost = previousCost;
        }

        for (City city : this.findBestContinuation(costMap)) {
            // Increment iterations counter.
            ITERS++;

            // If the cost is less than the current result, continue searching the node.
            if (costMap.get(city) < this.result) {
                this.searchNode(matrixMap.get(city), path.add(city), costMap.get(city));
            }
        }
    }

    /**
     * Find the best continuation based on the cost.
     *
     * @param costMap The data to search trough.
     * @return The city list to continue with.
     */
    private List<City> findBestContinuation(Map<City, Double> costMap) {
        List<City> best = new ArrayList<>(costMap.keySet());
        best.sort(Comparator.comparingDouble(costMap::get));
        return best;
    }
}
