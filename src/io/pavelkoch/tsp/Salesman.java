package io.pavelkoch.tsp;

import java.util.*;

public class Salesman {

    private final List<City> cities;
    private final int size;
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
        double[][] matrix = this.generateMatrix();
        List<City[]> pairs = this.generatePairs();

        // Sort city pairs by ascending distance.
        pairs.sort(Comparator.comparingDouble((City[] c) -> matrix[c[0].getId()][c[1].getId()]));

        // Build the EMST.
        List<City[]> tree = this.buildMinimumSpanningTree(pairs);

        // Create the path.
        List<City> path = this.traverseTree(tree);
        double length = this.calculatePathLength(path);

        System.out.println(path);
        System.out.println(String.format(
                "Found path [%.3f units] long in [%.3f ms]",
                length, (System.nanoTime() - Main.START_TIME) / 1000000
        ));
    }

    private double calculatePathLength(List<City> path) {
        double length = 0.0;

        for (int i = 0; i < this.size; i++) {
            length += path.get(i).distanceTo(path.get(i + 1));
        }

        return length;
    }

    private List<City> traverseTree(List<City[]> tree) {
        List<City> path = new ArrayList<>();
        List<City[]> visited = new ArrayList<>();

        // Add the first city to the path.
        path.add(this.cities.get(0));

        while (path.size() < this.size) {

            outer:
            for (City[] pair : tree) {
                if (visited.contains(pair)) {
                    continue;
                }

                for (City city : path) {
                    if (city != pair[0] && city != pair[1]) {
                        continue;
                    }

                    path.add(pair[0] == city ? pair[1] : pair[0]);
                    visited.add(pair);

                    break outer;
                }
            }
        }

        path.add(this.cities.get(0));

        return path;
    }

    // TODO this is shit but I wanna get it working first.
    private List<City[]> buildMinimumSpanningTree(List<City[]> pairs) {
        List<City[]> tree = new ArrayList<>();
        Map<City[], Boolean> used = new HashMap<>();

        // Make all pairs as not used.
        for (City[] pair : pairs) {
            used.put(pair, false);
        }

        // Put the first pair in the tree.
        tree.add(pairs.get(0));
        used.put(pairs.get(0), true);

        // Build the tree until there are N - 1 nodes.
        outer:
        while (true) {
            for (City[] pair : pairs) {
                // Skip all used pairs.
                if (used.get(pair)) {
                    continue;
                }

                // If and only if a city of the pair is in the tree once, add the pair to the tree.
                if (this.hasOneCityInTree(pair, tree)) {
                    tree.add(pair);
                }

                if (tree.size() == this.size - 1) {
                    break outer;
                }
            }
        }

        return tree;
    }

    private boolean hasOneCityInTree(City[] pair, List<City[]> tree) {
        int occurrences = 0;

        // Create a set of all used cities in the tree.
        Set<City> used = new HashSet<>();

        for (City[] node : tree) {
            used.add(node[0]);
            used.add(node[1]);
        }

        for (City vertex : used) {
            if (vertex == pair[0] || vertex == pair[1]) {
                occurrences++;
            }
        }

        return occurrences == 1;
    }

    // TODO this can be reduced.
    private double[][] generateMatrix() {
        double[][] matrix = new double[this.size][this.size];

        for (City origin : this.cities) {
            // Fill the gaps with -1s that we can ignore.
            matrix[origin.getId()][origin.getId()] = -1.0;

            for (City destination : this.cities) {
                // Distances are symmetrical.
                matrix[origin.getId()][destination.getId()] = matrix[destination.getId()][origin.getId()] = origin.distanceTo(destination);
            }
        }

        return matrix;
    }

    private List<City[]> generatePairs() {
        List<City[]> pairs = new ArrayList<>();

        for (int i = 0; i < this.size; i++) {
            for (int j = i + 1; j < this.size; j++) {
                // Only add each pair once, no matter the direction.
                pairs.add(new City[] { this.cities.get(i), this.cities.get(j) });
            }
        }

        return pairs;
    }
}
