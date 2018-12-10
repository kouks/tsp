package io.pavelkoch.tsp;

import java.util.*;

public class Salesman {

    private final List<City> cities;
    private final int size;
    private double result = 76703.689;
    // private double result = Double.MAX_VALUE;
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
        City[] path = new City[this.size + 1];
        double cost = matrix.reduceAndCost();

        // Make the first city visited and also make it the first and last city in our path.
        this.cities.get(0).setVisited(true);
        path[0] = this.cities.get(0);
        path[this.size] = this.cities.get(0);

        try {
            // Recursively build the search space tree and modify the shortest path.
            this.searchNode(matrix, cost, 1, path);
        } catch (InterruptedException ignored) {
            //
        }
    }

    /**
     * Recursive searchNode for building the search space tree.
     *
     * @param matrix The adjacency matrix
     * @param cost The current bound
     * @param level The current level
     * @param path The current path
     */
    private void searchNode(AdjacencyMatrix matrix, double cost, int level, City[] path) throws InterruptedException {
        // If we reached the time limit, throw an exception to get our of the recursion.
        if ((System.nanoTime() - Main.START_TIME) / 1000000 > Main.MAX_EXECUTION_MILLIS) {
            System.out.println(String.format("Timeout :("));

            throw new InterruptedException();
        }

        // If we reached the leaf of the tree, assess the route and terminate this node.
        if (level == this.size) {
            // Increment iterations counter.
            ITERS++;

            // If the cost is less than our current best, set the cost to our current best.
            if (cost < this.result) {
                // Increment best iterations counter.
                BEST_ITERS++;

                this.result = cost;

                System.out.println(String.format("Iterations: %d", ITERS));
                System.out.println(String.format("Best Iterations: %d", BEST_ITERS));
                System.out.println(Arrays.toString(path));
                System.out.println(String.format(
                        "Found a path [%.3funits] long in [%.2fms].",
                        this.result, (System.nanoTime() - Main.START_TIME) / 1000000
                ));
            }

            return;
        }

        // Save the current cost so that we can reset later.
        double previousCost = cost;

        // Route key idea.
        Map<City, Double> costMap = new HashMap<>();
        Map<City, AdjacencyMatrix> matrixMap = new HashMap<>();

        for (City city : this.cities) {
            // If the city was already visited in the current search space, skip it.
            if (city.wasVisited()) {
                continue;
            }

            // Copy the current matrix, exclude the path being calculated and reduce it to get the additional cost. This
            // is in result our bounding function that allows us to prune nodes in the search space tree.
            AdjacencyMatrix copy = matrix.copy();
            copy.excludePath(path[level - 1].getId(), city.getId());
            cost += matrix.getMatrix()[path[level - 1].getId()][city.getId()] + copy.reduceAndCost();

            // Save the results.
            costMap.put(city, cost);
            matrixMap.put(city, copy);

            // Reset the cost variable.
            cost = previousCost;
        }

        List<City> best = this.findBestContinuation(costMap);

        for (City city : best) {
            // Increment iterations counter.
            ITERS++;

            // If the cost is less than the current result, continue searching the node.
            if (costMap.get(city) < this.result) {
                path[level] = city;
                city.setVisited(true);

                this.searchNode(matrixMap.get(city), costMap.get(city), level + 1, path);
            }

            // After searching the first node, set all the cities that we visited in that node to be
            // unvisited again.
            for (City c : this.cities) {
                c.setVisited(false);
            }

            for (int j = 0; j < level; j++) {
                path[j].setVisited(true);
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
