package io.pavelkoch.tsp;

public class City {
    /**
     * The city id.
     */
    private int id;

    /**
     * The city x coordinate.
     */
    private final int x;

    /**
     * The city x coordinate.
     */
    private final int y;

    /**
     * The city x coordinate.
     */
    private boolean visited = false;

    /**
     * Class constructor.
     *
     * @param x The city x coordinate
     * @param y The city y coordinate
     */
    public City(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    /**
     * @return The city id
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return The city x coordinate
     */
    public int getX() {
        return this.x;
    }

    /**
     * @return The city y coordinate
     */
    public int getY() {
        return this.y;
    }

    /**
     * @return Whether the city was visited
     */
    public boolean wasVisited() {
        return this.visited;
    }

    /**
     * @param visited Whether the city was visited
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
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
        return this.id + "";
    }
}
