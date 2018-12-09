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
        double cost = 0.0;

        System.out.println(Arrays.deepToString(matrix));
        test(matrix);
        System.out.println(Arrays.deepToString(matrix));

        // Make the first city visited.
        this.visited[0] = true;

        // Recursively build the search space tree and modify the shortest path.
        this.step(matrix, cost, 0, 1, path);

        return this.result;
    }

    /**
     * Recursive step for building the search space tree.
     *
     * @param matrix The adjacency matrix
     * @param cost The current bound
     * @param distance The current distance
     * @param level The current level
     * @param path The current path
     */
    private void step(double[][] matrix, double cost, double distance, int level, int[] path) {
        if (level == this.size) {
            //
        }

        for (int i = 0; i < this.size; i++) {
            //
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

            // Fill the gaps with -1s that we can ignore.
            matrix[i][i] = -1.0;

            for (int j = i + 1; j < this.size; j++) {
                City destination = this.cities.get(j);

                // Distances are symmetrical.
                matrix[i][j] = matrix[j][i] = origin.distanceTo(destination);
            }
        }

        return matrix;
    }

    private void test(double[][] matrix) {
        matrix[0][0] = 123.0;
    }
}
