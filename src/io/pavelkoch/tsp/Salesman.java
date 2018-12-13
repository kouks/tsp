package io.pavelkoch.tsp;

import java.util.*;

public class Salesman {

    private final List<City> cities;
    private final int size;
    private final double temperature = 2000000.0;

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
        this.annealing(
                new ArrayList<>(this.cities),
                this.temperature,
                this.calculatePathLength(this.cities)
        );
    }

    /**
     * Perform the simulated annealing algorithm.
     *
     * @param path The base path
     * @param temperature The starting temperature
     * @param cost The base cost
     */
    public void annealing(List<City> path, double temperature, double cost) {
        double bestCost = Double.MAX_VALUE;
        List<City> bestPath = new ArrayList<>();
        int depth = 1;

        while (temperature > 0.0) {
            // Indexes to swap.
            int i = (int) (Math.random() * this.size);
            int j = (i + 1) % this.size;

            // Swap the cities.
            this.swapCities(i, j, path);

            // Recalculate the cost.
            double candidateCost = this.calculatePathLength(path);

            // If the cost is better or we are lucky enough to swap anyway, update the cost.
            if (candidateCost < cost || this.chanceFormula(cost, candidateCost, temperature)) {
                cost = candidateCost;

                if (cost < bestCost) {
                    bestCost = cost;
                    bestPath = path;
                }
            } else {
                // Otherwise swap cities back.
                this.swapCities(i, j, path);
            }

            // Reduce the temperature.
            temperature = this.reduceTemperature(temperature, ++depth);
        }

        this.report(bestPath, bestCost, depth);
    }

    /**
     * Report results.
     *
     * @param path The best path
     * @param cost The best cost
     * @param depth The final depth
     */
    private void report(List<City> path, double cost, int depth) {
        System.out.println(String.format("Temperature [%.0f]", this.temperature));
        System.out.println(String.format("Depth [%d]", depth));
        System.out.println(Arrays.toString(path.toArray()));
        System.out.println(String.format(
                "Found a path [%.3f units] long in [%.2f ms].",
                cost, (System.nanoTime() - Main.START_TIME) / 1000000
        ));
    }

    /**
     * Swap cities at given indexes.
     *
     * @param i From index
     * @param j To index
     * @param path The path to swap on
     */
    private void swapCities(int i, int j, List<City> path) {
        City temp = path.get(j);
        path.set(j, path.get(i));
        path.set(i, temp);
    }

    /**
     * The choose-worse-path chance formula.
     *
     * @param cost The current cost
     * @param candidateCost The worse cost
     * @param temperature The current temperature
     * @return Whether to swap anyway
     */
    private boolean chanceFormula(double cost, double candidateCost, double temperature) {
        return Math.random() < Math.pow(Math.E, (cost - candidateCost) / temperature);
    }

    /**
     * Formula to reduce the temperature.
     *
     * @param temperature The temperature to be reduced
     * @param depth The current depth
     * @return The new temperature
     */
    private double reduceTemperature(double temperature, int depth) {
        return temperature - 1 / (Math.log(depth));
    }

    /**
     * Helper to calculate path length.
     *
     * @param path The path to calculate
     * @return The final length
     */
    private double calculatePathLength(List<City> path) {
        double length = 0.0;

        for (int i = 0; i < this.size; i++) {
            // This also connects the last city to the first one.
            length += path.get(i).distanceTo(path.get((i + 1) % this.size));
        }

        return length;
    }
}
