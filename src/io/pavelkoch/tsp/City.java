package io.pavelkoch.tsp;

public class City {
    /**
     * The city x coordinate.
     */
    private final int x;

    /**
     * The city x coordinate.
     */
    private final int y;

    /**
     * Class constructor.
     *
     * @param x The city x coordinate.
     * @param y The city y coordinate.
     */
    public City(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return The city x coordinate.
     */
    public int getX() {
        return this.x;
    }

    /**
     * @return The city y coordinate.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Calculate the pythagorean distance between two given cities.
     *
     * @param destination The destination city
     * @return The pythagorean distance
     */
    public double distanceTo(City destination) {
        return Math.sqrt(
                Math.pow(this.x - destination.getX(), 2) +
                Math.pow(this.y - destination.getY(), 2)
        );
    }

    /**
     * @return The city description
     */
    @Override
    public String toString() {
        return String.format("{%s, %s}", this.x, this.y);
    }
}
