package io.pavelkoch.tsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Path {

    private List<City> cities;

    private Path(List<City> cities) {
        this.cities = cities;
    }

    public static Path at(City city) {
        return new Path(new ArrayList<>() {{ add(city); }});
    }

    public Path add(City city) {
        List<City> cities = new ArrayList<>(this.cities);
        cities.add(city);

        return new Path(cities);
    }

    public City last() {
        return this.cities.get(this.cities.size() - 1);
    }

    public int size() {
        return this.cities.size();
    }

    public int getCityId(int i) {
        return this.cities.get(i).getId();
    }

    public boolean has(City city) {
        return this.cities.contains(city);
    }

    @Override
    public String toString() {
        return Arrays.toString(this.cities.toArray());
    }
}
