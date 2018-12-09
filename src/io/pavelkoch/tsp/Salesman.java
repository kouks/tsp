package io.pavelkoch.tsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Salesman {

    private final List<City> cities;
    private final int size;
    private boolean[] visited;
    private double result = Double.MAX_VALUE;

    /**
     * Class constructor.
     *
     * @param cities The list of cities to travel through
     */
    public Salesman(List<City> cities) {
        this.cities = cities;
        this.size = cities.size();
        this.visited = new boolean[this.size];
    }

    /**
     * Entry point for the algorithm.
     *
     * @return The optional path length, hopefully
     */
    public double travel() {
        double[][] matrix = this.generateAdjacencyMatrix();
        int[] path = new int[this.size + 1];
        double bound = 0.0;

        for (int i = 0; i < this.size; i++) {
            bound += this.findNthShortestDistanceFrom(matrix, i, 1) + this.findNthShortestDistanceFrom(matrix, i, 2);
        }

        System.out.println(Arrays.deepToString(matrix));

        // Make the first city visited.
        this.visited[0] = true;

        // Recursively build the search space tree and modify the shortest path.
        this.step(matrix, bound, 0, 1, path);

        return this.result;
    }

    /**
     * Recursive step for building the search space tree.
     *
     * @param matrix The adjacency matrix
     * @param bound The current bound
     * @param distance The current distance
     * @param level The current level
     * @param path The current path
     */
    private void step(double[][] matrix, double bound, double distance, int level, int[] path) {
        // If we reached the last level, consider updating the shortest path.
        if (level == this.size) {
            double distanceBack = matrix[path[level - 1]][path[0]];
            System.out.println(String.format("%s to %s is %.3f", path[level - 1], path[0], distanceBack));

            // We can't return to self.
            if (distanceBack == 0.0) {
                return;
            }

            double candidate = distance + distanceBack;

            // If our candidate is shorter, set it as result.
            if (candidate < this.result) {
                this.result = candidate;

                System.out.println(this.result);
                System.out.println(Arrays.toString(path));
            }

            return;
        }

        for (int i = 0; i < this.size; i++) {
            double distanceToNext = matrix[path[level - 1]][i];

            // Skip any city that points to itself or has already been visited.
            if (distanceToNext == 0.0 || this.visited[i]) {
                continue;
            }

            System.out.println(String.format("%s to %s is %.3f", path[level - 1], i, distanceToNext));

            // Store the bound for reset.
            double previousBound = bound;

            // Add the distance to the next city.
            distance += distanceToNext;

            // We now need to adjust the bound. Note that calculating the bound is different for the first level.
            bound -= level == 1
                    ? (this.findNthShortestDistanceFrom(matrix, path[level - 1], 1) + this.findNthShortestDistanceFrom(matrix, i, 1)) / 2
                    : (this.findNthShortestDistanceFrom(matrix, path[level - 1], 2) + this.findNthShortestDistanceFrom(matrix, i, 1)) / 2;

            System.out.println(String.format("Distance: [%.3f] Bound: [%.3f] Result: [%.3f]", distanceToNext, bound, this.result));

            // If the bound is shorter than our current result, continue searching the node.
            if (bound + distance < this.result) {

                // Mark the city as visited.
                path[level] = i;
                visited[i] = true;

                // Recursively call the method for the current node.
                this.step(matrix, bound, distance, level + 1, path);
            }

            // Now we have to reset some of the variables before advancing to the next node.
            distance -= distanceToNext;
            bound = previousBound;

            // Un-visit some cities.
            this.visited = new boolean[this.size];

            for (int j = 0; j < level; j++) {
                visited[path[j]] = true;
            }
        }
    }

    /**
     * Generate an adjacency matrix.
     *
     * @return The adjacency matrix
     */
    private double[][] generateAdjacencyMatrix() {
        double[][] matrix = new double[this.size][this.size];

        for (int i = 0; i < this.size; i++) {
            City origin = this.cities.get(i);

            for (int j = i + 1; j < this.size; j++) {
                City destination = this.cities.get(j);

                // Distances are symmetrical.
                matrix[i][j] = matrix[j][i] = origin.distanceTo(destination);
            }
        }

        return matrix;
    }

    /**
     * Find the nth shortest path from the given city.
     *
     * @param matrix The adjacency matrix
     * @param city The city in question
     * @param n First or second shortest path
     * @return The path length
     */
    private double findNthShortestDistanceFrom(double[][] matrix, int city, int n) {
        List<Double> paths = new ArrayList<>();

        for (double path : matrix[city]) {
            paths.add(path);
        }

        paths.sort(Double::compare);

        System.out.println(Arrays.toString(paths.toArray()));

        // The shortest path is index 1 as the list includes a distance to the same city.
        return paths.get(n);
    }
}
