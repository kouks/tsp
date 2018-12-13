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
        int[] path = this.buildInitialPath();

        System.out.println(Arrays.toString(
                this.annealing(path, 100.0, this.calculatePathLength(path), 1)
        ));
    }

    public int[] annealing(int[] path, double temperature, double cost, int depth) {
        if (temperature == 0.0) {
            return path;
        }

        int[] swapped = this.swapAdjacentCities(Arrays.copyOf(path, this.size));
        double candidateCost = this.calculatePathLength(swapped);

        if (candidateCost < cost || this.chanceFormula(cost, candidateCost, temperature)) {
            return this.annealing(swapped, this.reduceTemperature(temperature, depth), candidateCost, depth);
        }

        return this.annealing(path, this.reduceTemperature(temperature, depth), cost, ++depth);
    }

    private boolean chanceFormula(double cost, double candidateCost, double temperature) {
        return Math.random() < Math.pow(Math.E, ((cost - candidateCost) / (temperature * ((candidateCost - cost) / (2 * Math.PI)))));
    }

    private double reduceTemperature(double temperature, int depth) {
        return temperature - 1; //(Math.log(depth) / Math.log(Math.E));
    }

    private int[] swapAdjacentCities(int[] path) {
        int i = (int) (Math.random() * this.size);
        int j = (i + 1) % this.size;

        // Swap the cities.
        int temp = path[j];
        path[j] = path[i];
        path[i] = temp;

        return path;
    }

    private int[] buildInitialPath() {
        int[] path = new int[this.size];

        for (int i = 0; i < this.size; i++) {
            path[i] = i;
        }

        return path;
    }

    private double calculatePathLength(int[] path) {
        double length = 0.0;

        for (int i = 0; i < this.size; i++) {
            // This also connects the last city to the first one.
            length += path[i].distanceTo(path[(i + 1) % this.size]);
        }

        return length;
    }
}
