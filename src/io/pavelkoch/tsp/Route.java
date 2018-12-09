package io.pavelkoch.tsp;

public class Route {

    private final City origin;
    private final City destination;

    public Route(City origin, City destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public static Route between(City origin, City destination) {
        return new Route(origin, destination);
    }

    public City getOrigin() {
        return this.origin;
    }

    public City getDestination() {
        return this.destination;
    }

    public double getDistance() {
        return this.origin.distanceTo(this.destination);
    }

    @Override
    public String toString() {
        return String.format(
                "(%s => %s :: %.0f)",
                this.origin, this.destination, this.getDistance()
        );
    }
}
