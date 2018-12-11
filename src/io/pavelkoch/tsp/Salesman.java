package io.pavelkoch.tsp;

import java.util.*;

public class Salesman {

    private final List<City> cities;
    private final int size;

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
        double[][] matrix = this.generateAdjacencyMatrix();
        int startNode = 0;
        double[][] memory = new double[this.size][(int) Math.pow(2, this.size)];

        this.setup(matrix, memory, startNode);
        this.solve(matrix, memory, startNode);

        double cost = this.findCost(matrix, memory, startNode);
        int[] tour = this.findTour(matrix, memory, startNode);

        System.out.println(Arrays.toString(tour));
        System.out.println(String.format("Found tour [%.3f units] long in[%.3f ms]", cost, (System.nanoTime() - Main.START_TIME) / 1000000));

        //System.out.println(Arrays.deepToString(memory));
    }

    public double[][] generateAdjacencyMatrix() {
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
        return matrix;
    }

    private void setup(double[][] matrix, double[][] memory, int startNode) {
        for (int i = 0; i < this.size; i++) {
            if (i == startNode) {
                continue;
            }

            memory[i][1 << startNode | 1 << i] = matrix[startNode][i];
        }
    }

    private void solve(double[][] matrix, double[][] memory, int startNode) {
        for (int r = 3; r <= this.size; r++) {
            for (int subset : this.combinations(r, this.size)) {
                if (this.notIn(startNode, subset)) {
                    continue;
                }

                for (int next = 0; next < this.size; next++) {
                    if (next == startNode || this.notIn(next, subset)) {
                        continue;
                    }

                    int state = subset ^ (1 << next);
                    double minDistance = Double.MAX_VALUE;

                    for (int end = 0; end < this.size; end++) {
                        if (end == startNode || end == next || this.notIn(end, subset)) {
                            continue;
                        }

                        double newDistance = memory[end][state] + matrix[end][next];

                        if (newDistance < minDistance) {
                            minDistance = newDistance;
                        }
                    }

                    memory[next][subset] = minDistance;
                }
            }
        }
    }

    private boolean notIn(int i, int subset) {
        return ((1 << i) & subset) == 0;
    }

    private List<Integer> combinations(int r, int n) {
        List<Integer> subsets = new ArrayList<>();
        this.combinations(0, 0, r, n, subsets);
        return subsets;
    }

    private void combinations(int set, int at, int r, int n, List<Integer> subsets) {
        if (r == 0) {
            subsets.add(set);
            return;
        }

        for (int i = at; i < n; i++) {
            set = set | (1 << i);
            this.combinations(set, i + 1, r - 1, n, subsets);
            set = set & ~(1 << i);
        }
    }

    private double findCost(double[][] matrix, double[][] memory, int startNode) {
        int endState = (1 << this.size) - 1;
        double minCost = Double.MAX_VALUE;

        for (int end = 0; end < this.size; end++) {
            if (end == startNode) {
                continue;
            }

            double tourCost = memory[end][endState] + matrix[end][startNode];

            if (tourCost < minCost) {
                minCost = tourCost;
            }
        }

        return minCost;
    }

    private int[] findTour(double[][] matrix, double[][] memory, int startNode) {
        int lastIndex = startNode;
        int state = (1 << this.size) - 1;
        int[] tour = new int[this.size + 1];

        for (int i = this.size - 1; i >= 1; i--) {
            int index = -1;

            for (int j = 0; j < this.size; j++) {
                if (j == startNode || this.notIn(j, state)) {
                    continue;
                }

                if (index == -1) {
                    index = j;
                }

                double previousDistance = memory[index][state] + matrix[index][lastIndex];
                double newDistance = memory[j][state] + matrix[j][lastIndex];

                if (newDistance < previousDistance) {
                    index = j;
                }
            }

            tour[i] = index;
            state = state ^ (1 << index);
            lastIndex = index;
        }

        tour[0] = tour[this.size] = startNode;
        return tour;
    }
}
