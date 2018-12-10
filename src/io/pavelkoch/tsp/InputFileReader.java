package io.pavelkoch.tsp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class InputFileReader {

    /**
     * Read the provided input file.
     *
     * @return The parsed list of City instances
     * @throws IOException If something goes wrong, Sherlock
     */
    public List<City> read(Path path) throws IOException {
        return Files.lines(path)
                .filter((String line) -> !line.equals(""))
                .map(this::convertLineToCity)
                .collect(Collectors.toList());
    }

    /**
     * Convert a data line from the input file to a City instance.
     *
     * @param line The line to be parsed
     * @return The City instance
     */
    private City convertLineToCity(String line) {
        String[] data = line.trim().replaceAll(" +", " ").split(" ");

        return new City(
                Integer.parseInt(data[0]) - 1,
                Integer.parseInt(data[1]),
                Integer.parseInt(data[2])
        );
    }
}
