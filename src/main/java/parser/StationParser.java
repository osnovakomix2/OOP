package parser;

import core.FuelStation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public interface StationParser {
    List<FuelStation> parse(InputStream in);

    default List<FuelStation> parse(Path path) {
        try (InputStream in = Files.newInputStream(path)) {
            return parse(in);
        } catch (IOException e) {
            System.err.print(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}