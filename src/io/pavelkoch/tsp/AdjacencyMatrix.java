package io.pavelkoch.tsp;

import java.util.Arrays;
import java.util.List;

public class AdjacencyMatrix {

    private double[][] matrix;
    private int size;

    /**
     * Class constructor.
     *
     * @param matrix The matrix to work with.
     */
    private AdjacencyMatrix(double[][] matrix) {
        this.matrix = matrix;
        this.size = matrix.length;
    }

    /**
     * @return The raw matrix.
     */
    public double[][] getMatrix() {
        return matrix;
    }

    /**
     * Create a new Matrix instance from the list of cities.
     *
     * @param cities The list of cities
     * @return New Matrix
     */
    public static AdjacencyMatrix fromCities(List<City> cities) {
        int size = cities.size();
        double[][] matrix = new double[size][size];

        for (int i = 0; i < size; i++) {
            City origin = cities.get(i);

            // Fill the gaps with -1s that we can ignore.
            matrix[i][i] = -1.0;

            for (int j = i + 1; j < size; j++) {
                City destination = cities.get(j);

                // Distances are symmetrical.
                matrix[i][j] = matrix[j][i] = origin.distanceTo(destination);
            }
        }

        return new AdjacencyMatrix(matrix);
    }

    /**
     * This method reduces the matrix so that there is at least 1 zero in each column and row. It also returns the cost
     * of this reduction, which is a sum of all subtractions that we had to perform.
     *
     * @return The cost of reduction of the matrix
     */
    public double reduceAndCost() {
        double cost = 0.0;

        for (Orientation o : Orientation.values()) {
            for (int i = 0; i < this.size; i++) {
                double lowest = this.getLowestValue(i, o);

                for (int j = 0; j < this.size; j++) {

                    if (o == Orientation.ROW) {
                        // Skip -1 values.
                        if (this.matrix[i][j] == -1.0) {
                            continue;
                        }

                        this.matrix[i][j] -= lowest;
                    } else {
                        // Skip -1 values.
                        if (this.matrix[j][i] == -1.0) {
                            continue;
                        }

                        this.matrix[j][i] -= lowest;
                    }
                }

                cost += lowest;
            }
        }

        return cost;
    }

    /**
     * After searching for another node, we need to exclude the calculated path.
     *
     * @param from The from index.
     * @param to The to index.
     */
    public void excludePath(int from, int to) {
        this.matrix[from][to] = this.matrix[to][from] = -1;

        for (int i = 0; i < this.size; i++) {
            this.matrix[from][i] = -1;
            this.matrix[i][to] = -1;
        }
    }

    /**
     * Create a copy of the matrix instance in the given state.
     *
     * @return The copy
     */
    public AdjacencyMatrix copy() {
        double[][] copy = new double[this.size][this.size];

        for (int i = 0; i < this.size; i++) {
            copy[i] = Arrays.copyOf(this.matrix[i], this.size);
        }

        return new AdjacencyMatrix(copy);
    }

    /**
     * Return the lowest value in either a column or a row.
     *
     * @param n The index of the column or the row
     * @return The lowest value
     */
    private double getLowestValue(int n, Orientation orientation) {
        double lowest = Double.MAX_VALUE;

        for (int i = 0; i < this.size; i++) {
            double value = orientation == Orientation.ROW ? this.matrix[n][i] : this.matrix[i][n];

            // Skip -1 values.
            if (value == -1.0) {
                continue;
            }

            lowest = value < lowest ? value : lowest;
        }

        return lowest == Double.MAX_VALUE ? 0.0 : lowest;
    }

    /**
     * The orientation enum for calculations.
     */
    private enum Orientation {
        ROW,
        COLUMN
    }
}
