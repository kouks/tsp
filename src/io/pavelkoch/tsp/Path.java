package io.pavelkoch.tsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Path {
    /**
     * The list of cities this path goes through.
     */
    private List<City> cities;

    /**
     * Class constructor.
     *
     * @param cities The list of cities this path goes through
     */
    private Path(List<City> cities) {
        this.cities = cities;
    }

    /**
     * Start a new path at a certain city.
     *
     * @param city The city to start at
     */
    public static Path at(City city) {
        return new Path(new ArrayList<>() {{ add(city); }});
    }

    /**
     * Add a new city to the path, it is important that we return a new instance.
     *
     * @param city The city to be added
     * @return A new Path instance
     */
    public Path add(City city) {
        List<City> cities = new ArrayList<>(this.cities);
        cities.add(city);

        return new Path(cities);
    }

    /**
     * @return The path size
     */
    public int size() {
        return this.cities.size();
    }

    /**
     * Return the city at a provided index.
     *
     * @param i The index provided
     * @return The city found
     */
    public City get(int i) {
        return this.cities.get(i);
    }

    /**
     * Finds whether a city is in the path.
     *
     * @param city The city in question
     * @return Whether the city was found
     */
    public boolean has(City city) {
        return this.cities.contains(city);
    }

    /**
     * @return The path description
     */
    @Override
    public String toString() {
        return Arrays.toString(this.cities.toArray());
    }
}
