package io.pavelkoch.tsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Salesman {

    private final List<City> cities;
    private final int size;
    private boolean[] visited;
    private double boundary = Double.MAX_VALUE;

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
        System.out.println(Arrays.deepToString(matrix));
        double cost = this.reduceMatrix(matrix);

        System.out.println(Arrays.deepToString(matrix));
        System.out.println(String.format("Cost: [%.3f]", cost));

        // Make the first city visited.
        this.visited[0] = true;

        // Recursively build the search space tree and modify the shortest path.
        this.step(matrix, cost, 1, path);

        return this.boundary;
    }

    /**
     * Recursive step for building the search space tree.
     *
     * @param matrix The adjacency matrix
     * @param cost The current bound
     * @param level The current level
     * @param path The current path
     */
    private void step(double[][] matrix, double cost, int level, int[] path) {
        if (level == this.size) {
            System.out.println(cost);

            if (cost < this.boundary) {
                this.boundary = cost;
                System.out.println(Arrays.toString(path));
            }

            return;
        }

        double previousCost = cost;

        for (int i = 0; i < this.size; i++) {
            if (visited[i] || path[level - 1] == i) {
                continue;
            }

            double[][] copy = this.copyMatrix(matrix);

            double distance = copy[path[level - 1]][i];

            this.modifyMatrix(copy, path[level - 1], i);

            cost += distance + this.reduceMatrix(copy);

            if (cost < this.boundary) {
                path[level] = i;
                visited[i] = true;

                this.step(copy, cost, level + 1, path);
            }

            cost = previousCost;

            visited = new boolean[this.size];
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

    private double reduceMatrix(double[][] matrix) {
        double cost = 0.0;

        for (int i = 0; i < this.size; i++) {
            double lowest = this.lowest(matrix, i, "row");

            for (int j = 0; j < this.size; j++) {

                // Skip -1 values.
                if (matrix[i][j] == -1.0) {
                    continue;
                }

                matrix[i][j] -= lowest;
            }

            cost += lowest;
        }

        for (int i = 0; i < this.size; i++) {
            double lowest = this.lowest(matrix, i, "col");

            for (int j = 0; j < this.size; j++) {

                // Skip -1 values.
                if (matrix[j][i] == -1.0) {
                    continue;
                }

                matrix[j][i] -= lowest;
            }

            cost += lowest;
        }

        return cost;
    }

    private double lowest(double[][] matrix, int n, String orientation) {
        double lowest = Double.MAX_VALUE;

        for (int i = 0; i < this.size; i++) {
            double value = orientation.equals("row") ? matrix[n][i] : matrix[i][n];

            if (value == -1.0) {
                continue;
            }

            lowest = value < lowest ? value : lowest;
        }

        return lowest == Double.MAX_VALUE ? 0.0 : lowest;
    }

    private void modifyMatrix(double[][] matrix, int from, int to) {
        matrix[from][to] = matrix[to][from] = -1;

        for (int i = 0; i < this.size; i++) {
            matrix[from][i] = -1;
            matrix[i][to] = -1;
        }
    }

    private double[][] copyMatrix(double[][] matrix) {
        double[][] copy = new double[this.size][this.size];

        for (int i = 0; i < this.size; i++) {
            copy[i] = Arrays.copyOf(matrix[i], this.size);
        }

        return copy;
    }
}
